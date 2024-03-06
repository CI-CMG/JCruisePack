package edu.colorado.cires.cruisepack.app.ui.controller.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;


public class GravityDatasetInstrumentController extends BaseDatasetInstrumentController<BaseDatasetInstrumentModel<GravityAdditionalFieldsModel>> {

  public GravityDatasetInstrumentController(BaseDatasetInstrumentModel<GravityAdditionalFieldsModel> model) {
    super(model);
  }

  public void setCorrectionModel(DropDownItem correctionModel) {
    model.getAdditionalFieldsModel().setCorrectionModel(correctionModel);
  }

  public void setObservationRate(String observationRate) {
    model.getAdditionalFieldsModel().setObservationRate(observationRate);
  }

  public void setDepartureTie(String departureTie) {
    model.getAdditionalFieldsModel().setDepartureTie(departureTie);
  }

  public void setArrivalTie(String arrivalTie) {
    model.getAdditionalFieldsModel().setArrivalTie(arrivalTie);
  }

  public void setDriftPerDay(String driftPerDay) {
    model.getAdditionalFieldsModel().setDriftPerDay(driftPerDay);
  }
}
