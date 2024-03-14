package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import org.springframework.stereotype.Component;

@Component
public class PackStateModel extends PropertyChangeModel {
  
  private int progress;
  private Thread thread;

  public void setProgress(int progress) {
    setIfChanged(Events.UPDATE_PROGRESS, progress, () -> this.progress, (p) -> this.progress = p);
  }

  public void setThread(Thread thread) {
    this.thread = thread;
  }
  
  public void stopThread() {
    if (thread != null) {
      thread.interrupt();
    }
  }
}
