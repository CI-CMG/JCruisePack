package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

abstract class ConstraintValidatorTest<O, T extends ConstraintValidator<? extends Annotation, O>> {
  
  protected static ConstraintValidatorContext VALIDATOR_CONTEXT = mock(ConstraintValidatorContext.class);
  
  protected abstract T createValidator();
  
  protected T validator;
  
  @BeforeEach
  void beforeEach() {
    validator = createValidator();
    reset(VALIDATOR_CONTEXT);
  }
  
  @AfterEach
  void afterEach() {
    reset(VALIDATOR_CONTEXT);
  }

}
