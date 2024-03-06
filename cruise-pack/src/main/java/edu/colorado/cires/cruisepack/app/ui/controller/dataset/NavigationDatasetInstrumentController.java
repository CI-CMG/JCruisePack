package edu.colorado.cires.cruisepack.app.ui.controller.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;


public class NavigationDatasetInstrumentController extends BaseDatasetInstrumentController<BaseDatasetInstrumentModel<NavigationAdditionalFieldsModel>> {

  public NavigationDatasetInstrumentController(BaseDatasetInstrumentModel<NavigationAdditionalFieldsModel> model) {
    super(model);
  }

  public void setNavDatum(DropDownItem navDatum) {
    model.getAdditionalFieldsModel().setNavDatum(navDatum);
  }
}
