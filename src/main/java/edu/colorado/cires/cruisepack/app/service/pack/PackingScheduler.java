package edu.colorado.cires.cruisepack.app.service.pack;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.MetadataService;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.ui.model.ErrorModel;
import java.beans.PropertyChangeListener;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class PackingScheduler {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(PackingScheduler.class);
  
  private final Queue<PackerExecutor> packExecutions = new ArrayBlockingQueue<>(100);
  
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
    PackerExecutor executor = createExecutor(
        packingJobEvent.getPackingJob()
    );
    executor.addChangeListener((PropertyChangeListener) packingJobEvent.getSource());
    
    packExecutions.add(executor);
    
    if (packExecutions.size() == 1) {
      startPacking(packExecutions.peek());
    }
  }
  
  private void startPacking(PackerExecutor packerExecutor) {
    packerExecutor.startPacking();
  }
  
  public void stopJob(String processId) {
    PackerExecutor executor = packExecutions.stream()
        .filter(packerExecutor -> packerExecutor.getProcessId().equals(processId))
        .findFirst().orElse(null);
    if (executor != null) {
      executor.stopPacking();
      packExecutions.remove(executor);
    }
  }
  
  public void clearJobs() {
    packExecutions.stream()
        .map(PackerExecutor::getProcessId)
        .forEach(this::stopJob);
  }
  
  private PackerExecutor createExecutor(PackingJob packingJob) {
    PackJob packJob = packingJob.packJob();
    return new PackerExecutor(
        metadataService,
        instrumentDatastore,
        Paths.get(serviceProperties.getWorkDir()),
        packingJob.executeBefore(),
        () -> {
          stopJob(packingJob.processId());
          packingJob.executeAfter().accept(!packExecutions.isEmpty());

          PackerExecutor executor = packExecutions.peek();
          if (executor != null) {
            startPacking(executor);
          }
        },
        packingJob.processId(),
        packJob
    );
  }
}
