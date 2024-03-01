package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import org.springframework.stereotype.Component;

@Component
public class FooterControlModel extends PropertyChangeModel {

  private boolean stopButtonEnabled = false;
  private boolean saveButtonEnabled = true;
  private boolean packageButtonEnabled = true;
  private boolean saveWarningDialogueVisible = false;
  private boolean saveExitAppDialogueVisible = false;
  private boolean saveOrUpdateDialogVisible = false;

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

  public boolean isSaveWarningDialogueVisible() {
      return saveWarningDialogueVisible;
  }

  public void setSaveWarningDialogueVisible(boolean saveWarningDialogueVisible) {
      setIfChanged(Events.UPDATE_SAVE_WARNING_DIALOGUE_VISIBLE, saveWarningDialogueVisible, () -> this.saveWarningDialogueVisible, (v) -> this.saveWarningDialogueVisible = v);
  }

  public boolean isSaveExitAppDialogueVisible() {
      return saveExitAppDialogueVisible;
  }

  public void setSaveExitAppDialogueVisible(boolean saveExitAppDialogueVisible) {
      setIfChanged(Events.UPDATE_SAVE_EXIT_APP_DIALOGUE_VISIBLE, saveExitAppDialogueVisible, () -> this.saveExitAppDialogueVisible, (v) -> this.saveExitAppDialogueVisible = v);
  }

  public void emitPackageId(String packageId) {
    fireChangeEvent(Events.EMIT_PACKAGE_ID, null, packageId);
  }

  public void setSaveOrUpdateDialogVisible(boolean saveOrUpdateDialogVisible) {
    setIfChanged(Events.UPDATE_SAVE_OR_UPDATE_DIALOG_VISIBLE, saveOrUpdateDialogVisible, () -> this.saveOrUpdateDialogVisible, (v) -> this.saveOrUpdateDialogVisible = v);
  }
}
