package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.ConstraintValidator;
import org.junit.jupiter.api.Test;

abstract class DropDownItemValidatorTest<T extends ConstraintValidator<?, DropDownItem>> extends ConstraintValidatorTest<DropDownItem, T> {
  
  protected abstract DropDownItem getDefaultItem();
  
  @Test
  void testValidator() {
    assertTrue(validator.isValid(new DropDownItem("1", "value 1"), VALIDATOR_CONTEXT));
    assertFalse(validator.isValid(getDefaultItem(), VALIDATOR_CONTEXT));
  }

}
