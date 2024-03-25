package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsXLSXFormat.PathIsXLSXFormatValidator;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class PathIsXLSXFormatValidatorTest extends ConstraintValidatorTest<Path, PathIsXLSXFormatValidator> {
  
  @Test
  void testValidator() {
    assertTrue(validator.isValid(Paths.get("/path/to/file.xlsx"), VALIDATOR_CONTEXT));
    assertFalse(validator.isValid(Paths.get("/path/to/file.txt"), VALIDATOR_CONTEXT));
    assertTrue(validator.isValid(null, VALIDATOR_CONTEXT));
  } 

  @Override
  protected PathIsXLSXFormatValidator createValidator() {
    return new PathIsXLSXFormatValidator();
  }
}
