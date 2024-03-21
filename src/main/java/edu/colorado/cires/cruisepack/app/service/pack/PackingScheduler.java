package edu.colorado.cires.cruisepack.app.service.pack;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.MetadataService;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.ui.model.ErrorModel;
import java.beans.PropertyChangeListener;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class PackingScheduler {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(PackingScheduler.class);
  
  private final Map<String, PackerExecutor> packExecutions = new ConcurrentHashMap<>(0);
  
  private final ErrorModel errorModel;
  private final MetadataService metadataService;
  private final ServiceProperties serviceProperties;
  private final InstrumentDatastore instrumentDatastore;

  @Autowired
  public PackingScheduler(ErrorModel errorModel, MetadataService metadataService, ServiceProperties serviceProperties,
      InstrumentDatastore instrumentDatastore) {
    this.errorModel = errorModel;
    this.metadataService = metadataService;
    this.serviceProperties = serviceProperties;
    this.instrumentDatastore = instrumentDatastore;
  }
  
  public void scheduleJob(PackingJobEvent packingJobEvent) {
    PackingJob packingJob = packingJobEvent.getPackingJob();
    PackJob packJob = packingJob.packJob();
    String packageId = packJob.getPackageId();
    
    PackerExecutor executor = packExecutions.get(packageId);
    if (executor != null) {
      LOGGER.error("Pack job already submitted: {}", packageId);
      errorModel.emitErrorMessage(String.format(
          "Failed to submit job. Pack job already submitted: %s", packageId
      ));
    }
    executor = createExecutor(packingJob);
    executor.addChangeListener((PropertyChangeListener) packingJobEvent.getSource());
    
    packExecutions.put(packageId, executor);
    
    executor.startPacking(packJob);
  }
  
  public void stopJob(String packageId) {
    PackerExecutor packerExecutor = packExecutions.get(packageId);
    if (packerExecutor != null) {
      packerExecutor.stopPacking();
      packExecutions.remove(packageId);
    }
  }
  
  public void clearJobs() {
    packExecutions.keySet()
        .forEach(this::stopJob);
  }
  
  private PackerExecutor createExecutor(PackingJob packingJob) {
    return new PackerExecutor(
        metadataService,
        instrumentDatastore,
        Paths.get(serviceProperties.getWorkDir()),
        packingJob.executeBefore(),
        () -> {
          packingJob.executeAfter().run();
          stopJob(packingJob.packJob().getPackageId());
        }
    );
  }
}
