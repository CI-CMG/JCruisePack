package edu.colorado.cires.cruisepack.app.service.pack;

import java.beans.PropertyChangeListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ClearJobsPublisher {
  
  private final ApplicationEventPublisher publisher;

  public ClearJobsPublisher(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }
  
  public void publish(PropertyChangeListener source) {
    publisher.publishEvent(new ClearJobsEvent(source));
  }
}
