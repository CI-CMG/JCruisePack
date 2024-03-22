package edu.colorado.cires.cruisepack.app.service.pack;

import edu.colorado.cires.cruisepack.app.service.PackJob;
import java.beans.PropertyChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PackQueuePublisher {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(PackQueuePublisher.class);
  
  private final QueueValidationService validationService;
  private final ApplicationEventPublisher publisher;

  public PackQueuePublisher(QueueValidationService validationService, ApplicationEventPublisher publisher) {
    this.validationService = validationService;
    this.publisher = publisher;
  }

  public void publish(PropertyChangeListener source, String processId, Runnable executeBefore, Runnable executeAfter) {
    validationService.validate().ifPresent(
        packJobs -> packJobs.peek(
            packJob -> publisher.publishEvent(new PackingJobEvent(
                source,
                new PackingJob(
                    packJob,
                    processId,
                    executeBefore,
                    executeAfter
                )
            ))
        ).forEach(this::logQueuedJob)
    );
  }
  
  public void publishOne(PropertyChangeListener source, String processId, PackJob packJob, Runnable executeBefore, Runnable executeAfter) {
    publisher.publishEvent(new PackingJobEvent(
        source,
        new PackingJob(
            packJob,
            processId,
            executeBefore,
            executeAfter
        )
    ));
  }

  private void logQueuedJob(PackJob packJob) {
    LOGGER.info("Processing job: {}", packJob.getPackageId());
  }

}
