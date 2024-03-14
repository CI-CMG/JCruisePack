package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import org.springframework.stereotype.Component;

@Component
public class PackStateModel extends PropertyChangeModel {
  
  private Double progress = 0D;
  private Double progressIncrement = null;
  private boolean processing;

  private void setProgress(Double progress) {
    setIfChanged(Events.UPDATE_PROGRESS, progress, () -> this.progress, (p) -> this.progress = p);
  }
  
  public void incrementProgress() {
    if (progressIncrement == null) {
      setProgress(null);
    } else {
      setProgress(progress + progressIncrement);
    }
  }

  public void setProgressIncrement(Double progressIncrement) {
    this.progressIncrement = progressIncrement;
  }

  public boolean isProcessing() {
    return processing;
  }

  public void setProcessing(boolean processing) {
    this.processing = processing;
    if (!processing) {
      setProgressIncrement(null);
      setProgress(0D);
    }
  }
}
