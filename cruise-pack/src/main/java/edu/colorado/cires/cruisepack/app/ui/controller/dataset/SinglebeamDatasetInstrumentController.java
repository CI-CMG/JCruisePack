package edu.colorado.cires.cruisepack.app.ui.controller.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;


public class SinglebeamDatasetInstrumentController extends BaseDatasetInstrumentController<SinglebeamDatasetInstrumentModel> {

  public SinglebeamDatasetInstrumentController(SinglebeamDatasetInstrumentModel model) {
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

  public void setVerticalDatum(DropDownItem verticalDatum) {
    model.setVerticalDatum(verticalDatum);
  }

  public void setObsRate(String obsRate) {
    model.setObsRate(obsRate);
  }

  public void setSoundVelocity(String soundVelocity) {
    model.setSoundVelocity(soundVelocity);
  }
}
