package edu.colorado.cires.cruisepack.app.service.pack;

import java.beans.PropertyChangeListener;
import org.springframework.context.ApplicationEvent;

class ClearJobsEvent extends ApplicationEvent {
  public ClearJobsEvent(PropertyChangeListener source) {
    super(source);
  }
}
