package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FooterControlModel extends PropertyChangeModel {

  private boolean stopButtonEnabled = false;
  private boolean saveButtonEnabled = true;
  private boolean packageButtonEnabled = true;
  private boolean saveWarningDialogueVisible = false;
  private boolean saveExitAppDialogueVisible = false;
  private boolean saveOrUpdateDialogVisible = false;
  private boolean packageIdCollisionDialogVisible = false;
  private String jobErrors = null;

  private List<String> warningMessages = new ArrayList<>(0);
  private List<String> ignoredWarningMessages = new ArrayList<>(0);
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
  
  public void setPackageIdCollisionDialogVisible(boolean packageIdCollisionDialogVisible) {
    setIfChanged(Events.UPDATE_PACKAGE_ID_COLLISION_DIALOG_VISIBLE, packageIdCollisionDialogVisible, () -> this.packageIdCollisionDialogVisible, (v) -> this.packageIdCollisionDialogVisible = v);
  }
  
  public void setJobErrors(String jobErrors) {
    setIfChanged(Events.UPDATE_JOB_ERRORS, jobErrors, () -> this.jobErrors, (v) -> this.jobErrors = v);
  }
  
  public void setWarningMessages(List<String> warningMessages) {
    setIfChanged(Events.UPDATE_JOB_WARNINGS, warningMessages, () -> new ArrayList<String>(0), (m) -> this.warningMessages = m);
  }
  
  public void addIgnoreWarningMessage(String warningMessage) {
    ignoredWarningMessages.add(warningMessage);
  }
  
  public void clearIgnoreWarningMessage() {
    ignoredWarningMessages.clear();
  }
  
  public List<String> getIgnoredWarningMessages() {
    return ignoredWarningMessages;
  }
}
