package edu.colorado.cires.cruisepack.app.datastore;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.xml.ship.ShipData;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipDatastore {

  private final ServiceProperties serviceProperties;
  private ShipData shipData;

  @Autowired
  public ShipDatastore(ServiceProperties serviceProperties) {
    this.serviceProperties = serviceProperties;
  }

  @PostConstruct
  public void init() {
//    Path workDir = Paths.get(serviceProperties.getWorkDir());
//    Path dataDir = workDir.resolve("data");
//    Path shipFile = dataDir.resolve("ships.xml");
//    if (!Files.isRegularFile(shipFile)) {
//      throw new IllegalStateException("Unable to read " + shipFile);
//    }
//    try (Reader reader = Files.newBufferedReader(shipFile, StandardCharsets.UTF_8)) {
//      shipData = (ShipData) JAXBContext.newInstance(ShipData.class).createUnmarshaller().unmarshal(reader);
//    } catch (IOException | JAXBException e) {
//      throw new IllegalStateException("Unable to parse " + shipFile, e);
//    }
  }
}
