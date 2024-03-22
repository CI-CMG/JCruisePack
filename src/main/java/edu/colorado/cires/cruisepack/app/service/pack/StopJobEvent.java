package edu.colorado.cires.cruisepack.app.service.pack;

import org.springframework.context.ApplicationEvent;

class StopJobEvent extends ApplicationEvent {
  
  private final String packageId;

  public StopJobEvent(Object source, String packageId) {
    super(source);
    this.packageId = packageId;
  }

  public String getPackageId() {
    return packageId;
  }
}
