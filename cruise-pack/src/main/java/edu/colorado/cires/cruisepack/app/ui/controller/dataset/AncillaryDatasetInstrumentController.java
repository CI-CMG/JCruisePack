package edu.colorado.cires.cruisepack.app.ui.controller.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.dataset.AncillaryDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;


public class AncillaryDatasetInstrumentController extends BaseDatasetInstrumentController<AncillaryDatasetInstrumentModel> {

  public AncillaryDatasetInstrumentController(AncillaryDatasetInstrumentModel model) {
    super(model);
  }

  public void setInstrument(DropDownItem instrument) {
    model.setInstrument(instrument);
  }

  public void setComments(String comments) {
    model.setComments(comments);
  }
}
