package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import org.springframework.stereotype.Component;

@Component
public class FooterControlModel extends PropertyChangeModel {

  private boolean stopButtonEnabled = false;
  private boolean saveButtonEnabled = true;
  private boolean packageButtonEnabled = true;

  public boolean isStopButtonEnabled() {
    return stopButtonEnabled;
  }

  public void setStopButtonEnabled(boolean stopButtonEnabled) {
    setIfChanged(Events.UPDATE_STOP_BUTTON_ENABLED, stopButtonEnabled, () -> this.stopButtonEnabled, (nv) -> this.stopButtonEnabled = nv);
  }

  public boolean isSaveButtonEnabled() {
    return saveButtonEnabled;
  }

  public void setSaveButtonEnabled(boolean saveButtonEnabled) {
    setIfChanged(Events.UPDATE_SAVE_BUTTON_ENABLED, saveButtonEnabled, () -> this.saveButtonEnabled, (nv) -> this.saveButtonEnabled = nv);
  }

  public boolean isPackageButtonEnabled() {
    return packageButtonEnabled;
  }

  public void setPackageButtonEnabled(boolean packageButtonEnabled) {
    setIfChanged(Events.UPDATE_PACKAGE_BUTTON_ENABLED, packageButtonEnabled, () -> this.packageButtonEnabled, (nv) -> this.packageButtonEnabled = nv);
  }
}
