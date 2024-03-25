package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.datastore.WaterColumnCalibrationStateDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidWaterColumnCalibrationStateDropDownItem.ValidWaterColumnCalibrationStateDropDownItemValidator;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

class ValidWaterColumnCalibrationStateDropDownItemTest extends DropDownItemValidatorTest<ValidWaterColumnCalibrationStateDropDownItemValidator> {

  @Override
  protected ValidWaterColumnCalibrationStateDropDownItemValidator createValidator() {
    return new ValidWaterColumnCalibrationStateDropDownItemValidator();
  }

  @Override
  protected DropDownItem getDefaultItem() {
    return WaterColumnCalibrationStateDatastore.UNSELECTED_CALIBRATION_STATE;
  }
}
