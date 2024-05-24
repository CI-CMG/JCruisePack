package edu.colorado.cires.cruisepack.app.service.pack.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.service.PackJobUtils;
import edu.colorado.cires.cruisepack.app.ui.model.queue.QueueModel;
import edu.colorado.cires.cruisepack.app.ui.view.queue.PackJobPanel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class QueuePersistenceService {
  
  private static final String QUEUE_FILE_NAME = "queue.json";
  
  private final QueueModel queueModel;
  private final ServiceProperties serviceProperties;
  private final ObjectMapper objectMapper;
  private final CruiseDataDatastore cruiseDataDatastore;
  private final SeaDatastore seaDatastore;
  private final PortDatastore portDatastore;
  private final PersonDatastore personDatastore;
  private final InstrumentDatastore instrumentDatastore;
  
  @Autowired
  public QueuePersistenceService(QueueModel queueModel, ServiceProperties serviceProperties, ObjectMapper objectMapper,
      CruiseDataDatastore cruiseDataDatastore, SeaDatastore seaDatastore, PortDatastore portDatastore, PersonDatastore personDatastore,
      InstrumentDatastore instrumentDatastore) {
    this.queueModel = queueModel;
    this.serviceProperties = serviceProperties;
    this.objectMapper = objectMapper;
    this.cruiseDataDatastore = cruiseDataDatastore;
    this.seaDatastore = seaDatastore;
    this.portDatastore = portDatastore;
    this.personDatastore = personDatastore;
    this.instrumentDatastore = instrumentDatastore;
  }
  
  @PostConstruct
  public void init() {
    Path queuePath = getQueuePath();
    if (!queuePath.toFile().exists()) {
      return;
    }
    
    try (FileInputStream inputStream = new FileInputStream(queuePath.toFile())) {
      objectMapper.readValue(inputStream, Queue.class).packageIds().stream()
          .map(cruiseDataDatastore::getByPackageId)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .map(cruiseData -> PackJobUtils.create(
              cruiseData, seaDatastore, portDatastore, personDatastore, instrumentDatastore
          ))
          .map(PackJobPanel::new)
          .forEach(p -> new Thread(() -> queueModel.addToQueue(p)).start());
    } catch (IOException e) {
      throw new IllegalStateException(String.format(
          "Failed to read queue file: %s", queuePath
      ), e);
    }
  }
  
  @PreDestroy
  public void teardown() {
    Path queuePath = getQueuePath();
    String failureMessage = String.format(
        "Failed to write queue file: %s", queuePath
    );
    if (!queuePath.toFile().exists()) {
      try {
        FileUtils.createParentDirectories(queuePath.toFile());
      } catch (IOException e) {
        throw new IllegalStateException(failureMessage, e);
      }
    }
    
    try (OutputStream outputStream = new FileOutputStream(queuePath.toFile())) {
      objectMapper.writeValue(outputStream, new Queue(queueModel.getQueue().stream()
          .map(PackJob::getPackageId)
          .toList()));
    } catch (IOException e) {
      throw new IllegalStateException(failureMessage, e);
    }


  }
  
  private Path getQueuePath() {
    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path localDataDir = workDir.resolve("local-data");
    return localDataDir.resolve(QUEUE_FILE_NAME);
  }
}
