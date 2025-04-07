package edu.colorado.cires.cruisepack.app.datastore;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.service.DatabaseObjectMapperFactory;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.data.Ship;
import edu.colorado.cires.cruisepack.data.ShipData;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
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
    Path shipFile = dataDir.resolve("ships.json");
    if (!Files.isRegularFile(shipFile)) {
      throw new IllegalStateException("Unable to read " + shipFile);
    }
    ShipData shipData;
    try {
      shipData = DatabaseObjectMapperFactory.getObjectMapper().readValue(shipFile.toFile(), ShipData.class);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to parse " + shipFile, e);
    }
    shipDropDowns = new ArrayList<>(shipData.getShips().size() + 1);
    shipDropDowns.add(UNSELECTED_SHIP);
    shipData.getShips().stream()
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

  public String getShipUuidForName(String name) {
    return shipDropDowns.stream().filter(dd -> name.equals(dd.getValue())).findFirst().map(DropDownItem::getId).orElse(null);
  }
}
