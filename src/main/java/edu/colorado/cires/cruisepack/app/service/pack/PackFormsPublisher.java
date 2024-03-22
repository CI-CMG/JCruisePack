package edu.colorado.cires.cruisepack.app.service.pack;

import edu.colorado.cires.cruisepack.app.service.PackagingValidationService;
import java.beans.PropertyChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PackFormsPublisher {
  
  private final PackagingValidationService validationService;
  private final ApplicationEventPublisher publisher;

  @Autowired
  public PackFormsPublisher(PackagingValidationService validationService, ApplicationEventPublisher publisher) {
    this.validationService = validationService;
    this.publisher = publisher;
  }
  
  public void publish(PropertyChangeListener source, String processId, Runnable executeBefore, Runnable executeAfter) {
    validationService.validate()
        .ifPresent(packJob ->
            publisher.publishEvent(new PackingJobEvent(
                source,
                new PackingJob(
                    packJob,
                    processId,
                    executeBefore,
                    executeAfter
                )
            ))
        );
  }
}
