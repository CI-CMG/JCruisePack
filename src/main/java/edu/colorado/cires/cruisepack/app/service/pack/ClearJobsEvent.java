package edu.colorado.cires.cruisepack.app.service.pack;

import java.beans.PropertyChangeListener;
import org.springframework.context.ApplicationEvent;

class ClearJobsEvent extends ApplicationEvent {
  
  private final Runnable executeAfter;
  
  public ClearJobsEvent(PropertyChangeListener source, Runnable executeAfter) {
    super(source);
    this.executeAfter = executeAfter;
  }

  public Runnable getExecuteAfter() {
    return executeAfter;
  }
}
