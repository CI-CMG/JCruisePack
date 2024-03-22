package edu.colorado.cires.cruisepack.app.service.pack;

import java.beans.PropertyChangeListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class StopJobPublisher {
  
  private final ApplicationEventPublisher publisher;

  public StopJobPublisher(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }
  
  public void publish(PropertyChangeListener source, String packageId) {
    publisher.publishEvent(new StopJobEvent(source, packageId));
  }
}
