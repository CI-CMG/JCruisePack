package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import org.springframework.stereotype.Component;

@Component
public class PackStateModel extends PropertyChangeModel {
  
  private int progress;
  private boolean processing;

  public void setProgress(int progress) {
    setIfChanged(Events.UPDATE_PROGRESS, progress, () -> this.progress, (p) -> this.progress = p);
  }

  public int getProgress() {
    return progress;
  }

  public boolean isProcessing() {
    return processing;
  }

  public void setProcessing(boolean processing) {
    this.processing = processing;
  }
}
