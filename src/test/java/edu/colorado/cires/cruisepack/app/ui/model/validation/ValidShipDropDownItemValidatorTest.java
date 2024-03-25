package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidShipDropDownItem.ValidShipDropDownItemValidator;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

class ValidShipDropDownItemValidatorTest extends DropDownItemValidatorTest<ValidShipDropDownItemValidator> {

  @Override
  protected ValidShipDropDownItemValidator createValidator() {
    return new ValidShipDropDownItemValidator();
  }

  @Override
  protected DropDownItem getDefaultItem() {
    return ShipDatastore.UNSELECTED_SHIP;
  }
}
