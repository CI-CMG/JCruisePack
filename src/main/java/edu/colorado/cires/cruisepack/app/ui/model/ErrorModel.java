package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import org.springframework.stereotype.Component;

@Component
public class ErrorModel extends PropertyChangeModel {
  
  public void emitErrorMessage(String message) {
    fireChangeEvent(Events.EMIT_ERROR_MESSAGE, null, message);
  }

}
