package edu.colorado.cires.cruisepack.app.datastore;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.sea.Sea;
import edu.colorado.cires.cruisepack.xml.sea.SeaData;
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
public class SeaDatastore {

  public static final DropDownItem UNSELECTED_SEA = new DropDownItem("", "Select Sea Name");

  private final ServiceProperties serviceProperties;
  private List<DropDownItem> seaDropDowns;

  @Autowired
  public SeaDatastore(ServiceProperties serviceProperties) {
    this.serviceProperties = serviceProperties;
  }

  @PostConstruct
  public void init() {
    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path dataDir = workDir.resolve("data");
    Path seaFile = dataDir.resolve("seas.xml");
    if (!Files.isRegularFile(seaFile)) {
      throw new IllegalStateException("Unable to read " + seaFile);
    }
    SeaData seaData;
    try (Reader reader = Files.newBufferedReader(seaFile, StandardCharsets.UTF_8)) {
      seaData = (SeaData) JAXBContext.newInstance(SeaData.class).createUnmarshaller().unmarshal(reader);
    } catch (IOException | JAXBException e) {
      throw new IllegalStateException("Unable to parse " + seaFile, e);
    }
    seaDropDowns = new ArrayList<>(seaData.getSeas().getSeas().size() + 1);
    seaDropDowns.add(UNSELECTED_SEA);
    seaData.getSeas().getSeas().stream()
        .filter(Sea::isUse)
        .sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()))
        .map(ship -> new DropDownItem(ship.getUuid(), ship.getName()))
        .forEach(seaDropDowns::add);
  }

  public List<DropDownItem> getSeaDropDowns() {
    return seaDropDowns;
  }
}
