package edu.colorado.cires.cruisepack.app.ui.model;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class FooterControlModelTest extends PropertyChangeModelTest<FooterControlModel> {

  @Override
  protected FooterControlModel createModel() {
    return new FooterControlModel();
  }

  @Test
  void setStopButtonEnabled() {
    assertPropertyChange(Events.UPDATE_STOP_BUTTON_ENABLED, model::isStopButtonEnabled, model::setStopButtonEnabled, true, false, false);
  }

  @Test
  void setSaveButtonEnabled() {
    assertPropertyChange(Events.UPDATE_SAVE_BUTTON_ENABLED, model::isSaveButtonEnabled, model::setSaveButtonEnabled, false, true, true);
  }

  @Test
  void setPackageButtonEnabled() {
    assertPropertyChange(Events.UPDATE_PACKAGE_BUTTON_ENABLED, model::isPackageButtonEnabled, model::setPackageButtonEnabled, false, true, true);
  }

  @Test
  void setSaveWarningDialogueVisible() {
    assertPropertyChange(Events.UPDATE_SAVE_WARNING_DIALOGUE_VISIBLE, model::isSaveWarningDialogueVisible, model::setSaveWarningDialogueVisible, true, false, false);
  }

  @Test
  void setSaveExitAppDialogueVisible() {
    assertPropertyChange(Events.UPDATE_SAVE_EXIT_APP_DIALOGUE_VISIBLE, model::isSaveExitAppDialogueVisible, model::setSaveExitAppDialogueVisible, true, false, false);
  }

  @Test
  void emitPackageId() {
    model.emitPackageId("packageId-1");
    assertChangeEvent(Events.EMIT_PACKAGE_ID, null, "packageId-1");

    clearMap();
    
    model.emitPackageId("packageId-2");
    assertChangeEvent(Events.EMIT_PACKAGE_ID, null, "packageId-2");
  }

  @Test
  void setSaveOrUpdateDialogVisible() {
    assertPropertyChange(Events.UPDATE_SAVE_OR_UPDATE_DIALOG_VISIBLE, model::isSaveOrUpdateDialogVisible, model::setSaveOrUpdateDialogVisible, true, false, false);
  }

  @Test
  void setPackageIdCollisionDialogVisible() {
    assertPropertyChange(Events.UPDATE_PACKAGE_ID_COLLISION_DIALOG_VISIBLE, model::isPackageIdCollisionDialogVisible, model::setPackageIdCollisionDialogVisible, true, false, false);
  }

  @Test
  void setJobErrors() {
    assertPropertyChange(Events.UPDATE_JOB_ERRORS, model::getJobErrors, model::setJobErrors, "value1", "value2", null);
  }

  @Test
  void setWarningMessages() {
    assertPropertyChange(
        Events.UPDATE_JOB_WARNINGS,
        model::getWarningMessages,
        model::setWarningMessages,
        List.of(
            "error-1", "error-2"
        ),
        List.of(
            "error-3", "error-4"
        ),
        Collections.emptyList()
    );
  }

  @Test
  void clearIgnoreWarningMessage() {
    model.addIgnoreWarningMessage("error-1");
    model.addIgnoreWarningMessage("error-2");

    assertEquals(List.of(
        "error-1", "error-2"
    ), model.getIgnoredWarningMessages());
    
    model.clearIgnoreWarningMessage();
    
    assertEquals(Collections.emptyList(), model.getIgnoredWarningMessages());
  }
}