package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class PackageModel extends PropertyChangeModel {

  private String cruiseId = "";

  public String getCruiseId() {
    return cruiseId;
  }

  public void setCruiseId(String cruiseId) {
    String oldCruiseId = this.cruiseId;
    if (!Objects.equals(cruiseId, oldCruiseId)) {
      this.cruiseId = cruiseId;
      fireChangeEvent(Events.UPDATE_CRUISE_ID, oldCruiseId, cruiseId);
    }
  }
}
