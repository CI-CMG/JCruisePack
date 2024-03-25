package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.datastore.ProjectDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidProjectDropDownItem.ValidProjectDropDownItemValidator;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

class ValidProjectDropDownItemValidatorTest extends DropDownItemValidatorTest<ValidProjectDropDownItemValidator> {

  @Override
  protected ValidProjectDropDownItemValidator createValidator() {
    return new ValidProjectDropDownItemValidator();
  }

  @Override
  protected DropDownItem getDefaultItem() {
    return ProjectDatastore.UNSELECTED_PROJECT;
  }
}
