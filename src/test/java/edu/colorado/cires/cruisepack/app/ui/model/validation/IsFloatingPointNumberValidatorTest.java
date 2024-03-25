package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.cruisepack.app.ui.model.validation.IsFloatingPointNumber.IsFloatingPointNumberValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class IsFloatingPointNumberValidatorTest extends ConstraintValidatorTest<String, IsFloatingPointNumberValidator> {
  
  @ParameterizedTest
  @CsvSource({
      "true,-1",
      "true,1",
      "false,1.",
      "false,-1.",
      "true,1.0",
      "true,-1.0",
      "true,.1",
      "true,-.1",
      "true,.00000001",
      "true,-.00000001",
      "true,",
      "false,inf"
  })
  void testValidator(boolean passExpected, String value) {
    assertEquals(passExpected, validator.isValid(value, VALIDATOR_CONTEXT));
  }

  @Override
  protected IsFloatingPointNumberValidator createValidator() {
    return new IsFloatingPointNumberValidator();
  }
}
