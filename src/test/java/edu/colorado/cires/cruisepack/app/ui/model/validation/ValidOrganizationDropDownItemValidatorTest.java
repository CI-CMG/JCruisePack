package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidOrganizationDropDownItem.ValidOrganizationDropDownItemValidator;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

class ValidOrganizationDropDownItemValidatorTest extends DropDownItemValidatorTest<ValidOrganizationDropDownItemValidator> {

  @Override
  protected ValidOrganizationDropDownItemValidator createValidator() {
    return new ValidOrganizationDropDownItemValidator();
  }

  @Override
  protected DropDownItem getDefaultItem() {
    return OrganizationDatastore.UNSELECTED_ORGANIZATION;
  }
}
