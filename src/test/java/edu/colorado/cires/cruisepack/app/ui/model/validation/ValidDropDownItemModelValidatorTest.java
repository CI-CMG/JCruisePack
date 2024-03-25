package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.cruisepack.app.ui.model.DropDownItemModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidDropDownItemModel.ValidDropDownItemModelValidator;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import org.junit.jupiter.api.Test;

class ValidDropDownItemModelValidatorTest extends ConstraintValidatorTest<DropDownItemModel, ValidDropDownItemModelValidator> {
  
  @Test
  void testValidator() {
    DropDownItemModel model = new DropDownItemModel(new DropDownItem("1", "value 1"));
    assertTrue(validator.isValid(model, VALIDATOR_CONTEXT));
    model.setItem(new DropDownItem("", "test"));
    assertFalse(validator.isValid(model, VALIDATOR_CONTEXT));
  }

  @Override
  protected ValidDropDownItemModelValidator createValidator() {
    return new ValidDropDownItemModelValidator();
  }
}
