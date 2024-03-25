package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.datastore.MagneticsCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidMagneticsCorrectionModelDropDownItem.ValidMagneticsCorrectionModelDropDownItemValidator;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

class ValidMagneticsCorrectionModelDropDownItemValidatorTest extends DropDownItemValidatorTest<ValidMagneticsCorrectionModelDropDownItemValidator> {

  @Override
  protected ValidMagneticsCorrectionModelDropDownItemValidator createValidator() {
    return new ValidMagneticsCorrectionModelDropDownItemValidator();
  }

  @Override
  protected DropDownItem getDefaultItem() {
    return MagneticsCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL;
  }
}
