package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import org.springframework.stereotype.Component;

@Component
public class PackProgressModel extends PropertyChangeModel {
  
  private int progress;

  public void setProgress(int progress) {
    setIfChanged(Events.UPDATE_PROGRESS, progress, () -> this.progress, (p) -> this.progress = p);
  }
}
