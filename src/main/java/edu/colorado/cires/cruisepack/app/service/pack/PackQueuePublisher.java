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

  public void publish(PropertyChangeListener source, Runnable executeBefore, Runnable executeAfter) {
    validationService.validate().ifPresent(
        packJobs -> packJobs.peek(
            packJob -> publisher.publishEvent(new PackingJobEvent(
                source,
                new PackingJob(
                    packJob,
                    executeBefore,
                    executeAfter
                )
            ))
        ).forEach(this::logQueuedJob)
    );
  }

  private void logQueuedJob(PackJob packJob) {
    LOGGER.info("Processing job: {}", packJob.getPackageId());
  }

}
