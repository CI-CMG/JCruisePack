package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidSeaDropDownItem.ValidSeaDropDownItemValidator;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

class ValidSeaDropDownItemValidatorTest extends DropDownItemValidatorTest<ValidSeaDropDownItemValidator> {

  @Override
  protected ValidSeaDropDownItemValidator createValidator() {
    return new ValidSeaDropDownItemValidator();
  }

  @Override
  protected DropDownItem getDefaultItem() {
    return SeaDatastore.UNSELECTED_SEA;
  }
}
