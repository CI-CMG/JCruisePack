package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidPortDropDownItem.ValidPortDropDownItemValidator;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

class ValidPortDropDownItemValidatorTest extends DropDownItemValidatorTest<ValidPortDropDownItemValidator> {

  @Override
  protected ValidPortDropDownItemValidator createValidator() {
    return new ValidPortDropDownItemValidator();
  }

  @Override
  protected DropDownItem getDefaultItem() {
    return PortDatastore.UNSELECTED_PORT;
  }
}
