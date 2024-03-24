package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import java.util.List;
import org.junit.jupiter.api.Test;

class ErrorModelTest extends PropertyChangeModelTest<ErrorModel> {

  @Override
  protected ErrorModel createModel() {
    return new ErrorModel();
  }

  @Test
  void emitErrorMessage() {
    List<String> errors = List.of(
        "error 1", "error 2"
    );
    
    errors.forEach(model::emitErrorMessage);
    
    assertChangeEvents(
        Events.EMIT_ERROR_MESSAGE,
        errors.stream()
            .map(e -> (String) null)
            .toList(),
        errors,
        (v) -> v
    );
  }
}