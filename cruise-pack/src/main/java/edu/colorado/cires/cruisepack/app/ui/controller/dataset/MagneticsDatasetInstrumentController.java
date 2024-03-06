package edu.colorado.cires.cruisepack.app.ui.controller.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;


public class MagneticsDatasetInstrumentController extends BaseDatasetInstrumentController<MagneticsAdditionalFieldsModel> {

  public MagneticsDatasetInstrumentController(BaseDatasetInstrumentModel<MagneticsAdditionalFieldsModel> model) {
    super(model);
  }

  public void setCorrectionModel(DropDownItem correctionModel) {
    model.getAdditionalFieldsModel().setCorrectionModel(correctionModel);
  }

  public void setSampleRate(String sampleRate) {
    model.getAdditionalFieldsModel().setSampleRate(sampleRate);
  }

  public void setTowDistance(String towDistance) {
    model.getAdditionalFieldsModel().setTowDistance(towDistance);
  }

  public void setSensorDepth(String sensorDepth) {
    model.getAdditionalFieldsModel().setSensorDepth(sensorDepth);
  }
}
