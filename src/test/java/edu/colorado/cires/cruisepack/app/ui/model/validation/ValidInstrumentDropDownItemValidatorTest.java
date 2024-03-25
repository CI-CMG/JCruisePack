package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidInstrumentDropDownItem.ValidInstrumentDropDownItemValidator;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

class ValidInstrumentDropDownItemValidatorTest extends DropDownItemValidatorTest<ValidInstrumentDropDownItemValidator> {

  @Override
  protected ValidInstrumentDropDownItemValidator createValidator() {
    return new ValidInstrumentDropDownItemValidator();
  }

  @Override
  protected DropDownItem getDefaultItem() {
    return InstrumentDatastore.UNSELECTED_INSTRUMENT;
  }
}
