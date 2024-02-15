package edu.colorado.cires.cruisepack.app.ui.controller.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;


public class GravityDatasetInstrumentController extends BaseDatasetInstrumentController<GravityDatasetInstrumentModel> {

  public GravityDatasetInstrumentController(GravityDatasetInstrumentModel model) {
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

  public void setObservationRate(String observationRate) {
    model.setObservationRate(observationRate);
  }

  public void setDepartureTie(String departureTie) {
    model.setDepartureTie(departureTie);
  }

  public void setArrivalTie(String arrivalTie) {
    model.setArrivalTie(arrivalTie);
  }

  public void setDriftPerDay(String driftPerDay) {
    model.setDriftPerDay(driftPerDay);
  }
}
