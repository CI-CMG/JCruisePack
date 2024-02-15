package edu.colorado.cires.cruisepack.app.ui.controller.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;


public class MagneticsDatasetInstrumentController extends BaseDatasetInstrumentController<MagneticsDatasetInstrumentModel> {

  public MagneticsDatasetInstrumentController(MagneticsDatasetInstrumentModel model) {
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

  public void setCorrectionModel(DropDownItem correctionModel) {
    model.setCorrectionModel(correctionModel);
  }

  public void setSampleRate(String sampleRate) {
    model.setSampleRate(sampleRate);
  }

  public void setTowDistance(String towDistance) {
    model.setTowDistance(towDistance);
  }

  public void setSensorDepth(String sensorDepth) {
    model.setSensorDepth(sensorDepth);
  }
}
