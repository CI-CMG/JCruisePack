package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidPersonDropDownItem.ValidPersonDropDownItemValidator;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

class ValidPersonDropDownItemValidatorTest extends DropDownItemValidatorTest<ValidPersonDropDownItemValidator> {

  @Override
  protected ValidPersonDropDownItemValidator createValidator() {
    return new ValidPersonDropDownItemValidator();
  }

  @Override
  protected DropDownItem getDefaultItem() {
    return PersonDatastore.UNSELECTED_PERSON;
  }
}
