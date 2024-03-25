package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.datastore.GravityCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidGravityCorrectionModelDropDownItem.ValidGravityCorrectionModelDropDownItemValidator;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

class ValidGravityCorrectionModelDropDownItemValidatorTest extends DropDownItemValidatorTest<ValidGravityCorrectionModelDropDownItemValidator> {
  
  @Override
  protected DropDownItem getDefaultItem() {
    return GravityCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL;
  }

  @Override
  protected ValidGravityCorrectionModelDropDownItemValidator createValidator() {
    return new ValidGravityCorrectionModelDropDownItemValidator();
  }
}
