package edu.colorado.cires.cruisepack.app.datastore;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.ship.Ship;
import edu.colorado.cires.cruisepack.xml.ship.ShipData;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipDatastore {

  public static final DropDownItem UNSELECTED_SHIP = new DropDownItem("", "Select Ship Name");


  private final ServiceProperties serviceProperties;
  private List<DropDownItem> shipDropDowns;

  @Autowired
  public ShipDatastore(ServiceProperties serviceProperties) {
    this.serviceProperties = serviceProperties;
  }

  @PostConstruct
  public void init() {
    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path dataDir = workDir.resolve("data");
    Path shipFile = dataDir.resolve("ships.xml");
    if (!Files.isRegularFile(shipFile)) {
      throw new IllegalStateException("Unable to read " + shipFile);
    }
    ShipData shipData;
    try (Reader reader = Files.newBufferedReader(shipFile, StandardCharsets.UTF_8)) {
      shipData = (ShipData) JAXBContext.newInstance(ShipData.class).createUnmarshaller().unmarshal(reader);
    } catch (IOException | JAXBException e) {
      throw new IllegalStateException("Unable to parse " + shipFile, e);
    }
    shipDropDowns = new ArrayList<>(shipData.getShips().getShips().size() + 1);
    shipDropDowns.add(UNSELECTED_SHIP);
    shipData.getShips().getShips().stream()
        .filter(Ship::isUse)
        .sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()))
        .map(ship -> new DropDownItem(ship.getUuid(), ship.getName()))
        .forEach(shipDropDowns::add);
  }

  public List<DropDownItem> getShipDropDowns() {
    return shipDropDowns;
  }

  public String getShipNameForUuid(String uuid) {
    return shipDropDowns.stream().filter(dd -> uuid.equals(dd.getId())).findFirst().map(DropDownItem::getValue).orElse(null);
  }
}
