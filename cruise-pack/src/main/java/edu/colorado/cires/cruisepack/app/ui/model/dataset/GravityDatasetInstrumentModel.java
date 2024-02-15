package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

public class GravityDatasetInstrumentModel extends BaseDatasetInstrumentModel {

  public static final String UPDATE_INSTRUMENT = "UPDATE_INSTRUMENT";
  public static final String UPDATE_PROCESSING_LEVEL = "UPDATE_PROCESSING";
  public static final String UPDATE_COMMENTS = "UPDATE_COMMENTS";
  public static final String UPDATE_CORRECTION_MODEL = "UPDATE_CORRECTION_MODEL";
  public static final String UPDATE_DEPARTURE_TIE = "UPDATE_DEPARTURE_TIE";
  public static final String UPDATE_ARRIVAL_TIE = "UPDATE_ARRIVAL_TIE";
  public static final String UPDATE_OBSERVATION_RATE = "UPDATE_OBSERVATION_RATE";
  public static final String UPDATE_DRIFT_PER_DAY = "UPDATE_DRIFT_PER_DAY";

  // TODO move this to datasource
  private DropDownItem instrument;
  private String processingLevel = "Raw";
  private String comments;

  private DropDownItem correctionModel;
  private String observationRate; //TODO make this only accept floating point numbers
  private String departureTie; //TODO make this only accept floating point numbers
  private String arrivalTie; //TODO make this only accept floating point numbers
  private String driftPerDay; //TODO make this only accept floating point numbers

  public DropDownItem getInstrument() {
    return instrument;
  }

  public void setInstrument(DropDownItem instrument) {
    setIfChanged(UPDATE_INSTRUMENT, instrument, () -> this.instrument, (nv) -> this.instrument = nv);
  }

  public String getProcessingLevel() {
    return processingLevel;
  }

  public void setProcessingLevel(String processingLevel) {
    setIfChanged(UPDATE_PROCESSING_LEVEL, processingLevel, () -> this.processingLevel, (nv) -> this.processingLevel = nv);
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    setIfChanged(UPDATE_COMMENTS, comments, () -> this.comments, (nv) -> this.comments = nv);
  }

  public DropDownItem getCorrectionModel() {
    return correctionModel;
  }

  public void setCorrectionModel(DropDownItem correctionModel) {
    setIfChanged(UPDATE_CORRECTION_MODEL, correctionModel, () -> this.correctionModel, (nv) -> this.correctionModel = nv);
  }

  public String getObservationRate() {
    return observationRate;
  }

  public void setObservationRate(String observationRate) {
    setIfChanged(UPDATE_OBSERVATION_RATE, observationRate, () -> this.observationRate, (nv) -> this.observationRate = nv);
  }

  public String getDepartureTie() {
    return departureTie;
  }

  public void setDepartureTie(String departureTie) {
    setIfChanged(UPDATE_DEPARTURE_TIE, departureTie, () -> this.departureTie, (nv) -> this.departureTie = nv);
  }

  public String getArrivalTie() {
    return arrivalTie;
  }

  public void setArrivalTie(String arrivalTie) {
    setIfChanged(UPDATE_ARRIVAL_TIE, arrivalTie, () -> this.arrivalTie, (nv) -> this.arrivalTie = nv);
  }

  public String getDriftPerDay() {
    return driftPerDay;
  }

  public void setDriftPerDay(String driftPerDay) {
    setIfChanged(UPDATE_DRIFT_PER_DAY, driftPerDay, () -> this.driftPerDay, (nv) -> this.driftPerDay = nv);
  }
}
