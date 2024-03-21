package edu.colorado.cires.cruisepack.app.service.pack;

import java.beans.PropertyChangeListener;
import org.springframework.context.ApplicationEvent;

class PackingJobEvent extends ApplicationEvent {
  private final PackingJob packingJob; 

  public PackingJobEvent(PropertyChangeListener source, PackingJob packingJob) {
    super(source);
    this.packingJob = packingJob;
  }

  public PackingJob getPackingJob() {
    return packingJob;
  }
}
