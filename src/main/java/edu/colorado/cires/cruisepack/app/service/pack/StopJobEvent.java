package edu.colorado.cires.cruisepack.app.service.pack;

import org.springframework.context.ApplicationEvent;

class StopJobEvent extends ApplicationEvent {
  
  private final String processId;

  public StopJobEvent(Object source, String processId) {
    super(source);
    this.processId = processId;
  }

  public String getProcessId() {
    return processId;
  }
}
