package edu.colorado.cires.cruisepack.app.ui.controller.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;


public class SinglebeamDatasetInstrumentController extends BaseDatasetInstrumentController<SinglebeamAdditionalFieldsModel> {

  public SinglebeamDatasetInstrumentController(BaseDatasetInstrumentModel<SinglebeamAdditionalFieldsModel> model) {
    super(model);
  }

  public void setVerticalDatum(DropDownItem verticalDatum) {
    model.getAdditionalFieldsModel().setVerticalDatum(verticalDatum);
  }

  public void setObsRate(String obsRate) {
    model.getAdditionalFieldsModel().setObsRate(obsRate);
  }

  public void setSoundVelocity(String soundVelocity) {
    model.getAdditionalFieldsModel().setSoundVelocity(soundVelocity);
  }
}
