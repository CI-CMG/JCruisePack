package edu.colorado.cires.cruisepack.app.datastore;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.port.Port;
import edu.colorado.cires.cruisepack.xml.port.PortData;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PortDatastore {

  private final ServiceProperties serviceProperties;
  private List<DropDownItem> portDropDowns;

  @Autowired
  public PortDatastore(ServiceProperties serviceProperties) {
    this.serviceProperties = serviceProperties;
  }

  @PostConstruct
  public void init() {
    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path dataDir = workDir.resolve("data");
    Path portFile = dataDir.resolve("ports.xml");
    if (!Files.isRegularFile(portFile)) {
      throw new IllegalStateException("Unable to read " + portFile);
    }
    PortData shipData;
    try (Reader reader = Files.newBufferedReader(portFile, StandardCharsets.UTF_8)) {
      shipData = (PortData) JAXBContext.newInstance(PortData.class).createUnmarshaller().unmarshal(reader);
    } catch (IOException | JAXBException e) {
      throw new IllegalStateException("Unable to parse " + portFile, e);
    }
    portDropDowns = shipData.getPorts().getPorts().stream()
        .filter(Port::isUse)
        .sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()))
        .map(ship -> new DropDownItem(ship.getUuid(), ship.getName()))
        .collect(Collectors.toList());
  }

  public List<DropDownItem> getPortDropDowns() {
    return portDropDowns;
  }
}
