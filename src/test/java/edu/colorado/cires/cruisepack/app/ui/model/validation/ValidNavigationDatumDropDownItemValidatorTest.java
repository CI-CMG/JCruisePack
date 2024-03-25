package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.datastore.NavigationDatumDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidNavigationDatumDropDownItem.ValidNavigationDatumDropDownItemValidator;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

class ValidNavigationDatumDropDownItemValidatorTest extends DropDownItemValidatorTest<ValidNavigationDatumDropDownItemValidator> {

  @Override
  protected ValidNavigationDatumDropDownItemValidator createValidator() {
    return new ValidNavigationDatumDropDownItemValidator();
  }

  @Override
  protected DropDownItem getDefaultItem() {
    return NavigationDatumDatastore.UNSELECTED_NAVIGATION_DATUM;
  }
}
