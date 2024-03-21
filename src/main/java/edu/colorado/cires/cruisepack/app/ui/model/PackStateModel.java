package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;

public class PackStateModel extends PropertyChangeModel {
  
  private float progress;
  private float progressIncrement;
  private boolean processing;

  private void setProgress(float progress) {
    float oldValue = this.progress;
    this.progress = progress;
    
    int roundedOldValue = (int) Math.floor(oldValue);
    int roundedNewValue = (int) Math.floor(progress);
    
    if (roundedOldValue != roundedNewValue) {
      fireChangeEvent(Events.UPDATE_PROGRESS, roundedOldValue, roundedNewValue);
    }
  }

  public void incrementProgress() {
    setProgress(this.progress + progressIncrement);
  }
  
  public void setProgressIncrement(float progressIncrement) {
    this.progressIncrement = progressIncrement;
  }

  public boolean isProcessing() {
    return processing;
  }

  public void setProcessing(boolean processing) {
    this.processing = processing;
    if (!processing) {
      setProgressIncrement(0f);
      setProgress(0f);
    }
  }
}
