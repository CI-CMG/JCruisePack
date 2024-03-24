package edu.colorado.cires.cruisepack.app.ui.model;

public class PackStateModel extends PropertyChangeModel {
  
  private final String processId;
  
  private float progress;
  private float progressIncrement;
  private boolean processing;

  public PackStateModel(String processId) {
    this.processId = processId;
  }

  private void setProgress(float progress) {
    float oldValue = this.progress;
    this.progress = progress;
    
    int roundedOldValue = (int) Math.floor(oldValue);
    int roundedNewValue = (int) Math.floor(progress);
    
    if (roundedOldValue != roundedNewValue) {
      fireChangeEvent(String.format(
          "UPDATE_PROGRESS_%s", processId
      ), roundedOldValue, roundedNewValue);
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

  public float getProgress() {
    return progress;
  }

  public float getProgressIncrement() {
    return progressIncrement;
  }
}
