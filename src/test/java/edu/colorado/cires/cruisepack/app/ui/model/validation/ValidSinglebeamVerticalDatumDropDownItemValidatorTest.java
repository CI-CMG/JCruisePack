package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.datastore.SinglebeamVerticalDatumDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidSinglebeamVerticalDatumDropDownItem.ValidSinglebeamVerticalDatumDropDownItemValidator;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

class ValidSinglebeamVerticalDatumDropDownItemValidatorTest extends DropDownItemValidatorTest<ValidSinglebeamVerticalDatumDropDownItemValidator> {

  @Override
  protected ValidSinglebeamVerticalDatumDropDownItemValidator createValidator() {
    return new ValidSinglebeamVerticalDatumDropDownItemValidator();
  }

  @Override
  protected DropDownItem getDefaultItem() {
    return SinglebeamVerticalDatumDatastore.UNSELECTED_VERTICAL_DATUM;
  }
}
