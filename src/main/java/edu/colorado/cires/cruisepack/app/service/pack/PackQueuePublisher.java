package edu.colorado.cires.cruisepack.app.service.pack;

import edu.colorado.cires.cruisepack.app.service.PackJob;
import java.beans.PropertyChangeListener;
import java.util.function.Consumer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PackQueuePublisher {

  private final ApplicationEventPublisher publisher;

  public PackQueuePublisher(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  public void publish(PropertyChangeListener source, String processId, PackJob packJob, Runnable executeBefore, Consumer<Boolean> executeAfter) {
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

}
