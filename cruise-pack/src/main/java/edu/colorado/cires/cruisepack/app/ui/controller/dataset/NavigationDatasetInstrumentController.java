package edu.colorado.cires.cruisepack.app.ui.controller.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;


public class NavigationDatasetInstrumentController extends BaseDatasetInstrumentController<NavigationDatasetInstrumentModel> {

  public NavigationDatasetInstrumentController(NavigationDatasetInstrumentModel model) {
    super(model);
  }

  public void setInstrument(DropDownItem instrument) {
    model.setInstrument(instrument);
  }

  public void setProcessingLevel(String processingLevel) {
    model.setProcessingLevel(processingLevel);
  }

  public void setComments(String comments) {
    model.setComments(comments);
  }

  public void setNavDatum(DropDownItem navDatum) {
    model.setNavDatum(navDatum);
  }
}
