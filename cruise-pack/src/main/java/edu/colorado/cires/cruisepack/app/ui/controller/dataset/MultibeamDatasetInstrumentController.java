package edu.colorado.cires.cruisepack.app.ui.controller.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.dataset.MultibeamDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;


public class MultibeamDatasetInstrumentController extends BaseDatasetInstrumentController<MultibeamDatasetInstrumentModel> {

  public MultibeamDatasetInstrumentController(MultibeamDatasetInstrumentModel model) {
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
}
