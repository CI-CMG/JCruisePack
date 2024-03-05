package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.datastore.GravityCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidGravityCorrectionModelDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;

public class GravityDatasetInstrumentModel extends BaseDatasetInstrumentModel {
  public static final String UPDATE_CORRECTION_MODEL = "UPDATE_CORRECTION_MODEL";
  public static final String UPDATE_CORRECTION_MODEL_ERROR = "UPDATE_CORRECTION_MODEL_ERROR";
  public static final String UPDATE_DEPARTURE_TIE = "UPDATE_DEPARTURE_TIE";
  public static final String UPDATE_DEPARTURE_TIE_ERROR = "UPDATE_DEPARTURE_TIE_ERROR";
  public static final String UPDATE_ARRIVAL_TIE = "UPDATE_ARRIVAL_TIE";
  public static final String UPDATE_ARRIVAL_TIE_ERROR = "UPDATE_ARRIVAL_TIE_ERROR";
  public static final String UPDATE_OBSERVATION_RATE = "UPDATE_OBSERVATION_RATE";
  public static final String UPDATE_OBSERVATION_RATE_ERROR = "UPDATE_OBSERVATION_RATE_ERROR";
  public static final String UPDATE_DRIFT_PER_DAY = "UPDATE_DRIFT_PER_DAY";
  public static final String UPDATE_DRIFT_PER_DAY_ERROR = "UPDATE_DRIFT_PER_DAY_ERROR";

  @ValidGravityCorrectionModelDropDownItem
  private DropDownItem correctionModel = GravityCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL;
  private String correctionModelError = null;
  @NotBlank
  private String observationRate; //TODO make this only accept floating point numbers
  private String observationRateError = null;
  @NotBlank
  private String departureTie; //TODO make this only accept floating point numbers
  private String departureTieError = null;
  @NotBlank
  private String arrivalTie; //TODO make this only accept floating point numbers
  private String arrivalTieError = null;
  @NotBlank
  private String driftPerDay; //TODO make this only accept floating point numbers
  private String driftPerDayError = null;

  public GravityDatasetInstrumentModel(String instrumentGroupShortCode) {
    super(instrumentGroupShortCode);
  }

  @Override
  public void clearErrors() {
    setDataPathError(null);
    setPublicReleaseDateError(null);
    setInstrumentError(null);
    setProcessingLevelError(null);
    setCommentsError(null);
    setCorrectionModelError(null);
    setObservationRateError(null);
    setDepartureTieError(null);
    setArrivalTieError(null);
    setDriftPerDayError(null);
  }

  @Override
  protected HashMap<String, Object> getAdditionalFields() {
    HashMap<String, Object> map = new HashMap<>();
    map.put("correction_model", correctionModel.getValue());
    map.put("observation_rate", observationRate);
    map.put("departure_tie", departureTie);
    map.put("arrival_tie", arrivalTie);
    map.put("drift_per_day", driftPerDay);
    return map;
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

  private void setCorrectionModelError(String message) {
    setIfChanged(UPDATE_CORRECTION_MODEL_ERROR, message, () -> this.correctionModelError, (e) -> this.correctionModelError = e);
  }

  private void setObservationRateError(String message) {
    setIfChanged(UPDATE_OBSERVATION_RATE_ERROR, message, () -> this.observationRateError, (e) -> this.observationRateError = e);
  }

  private void setDepartureTieError(String message) {
    setIfChanged(UPDATE_DEPARTURE_TIE_ERROR, message, () -> this.departureTieError, (e) -> this.departureTieError = e);
  }

  private void setArrivalTieError(String message) {
    setIfChanged(UPDATE_ARRIVAL_TIE_ERROR, message, () -> this.arrivalTieError, (e) -> this.arrivalTieError = e);
  }

  private void setDriftPerDayError(String message) {
    setIfChanged(UPDATE_DRIFT_PER_DAY_ERROR, message, () -> this.driftPerDayError, (e) -> this.driftPerDayError = e);
  }

  @Override
  protected void setErrors(String propertyPath, String message) {
    if (propertyPath.endsWith("instrument")) {
      setInstrumentError(message);
    } else if (propertyPath.endsWith("processingLevel")) {
      setProcessingLevelError(message);
    } else if (propertyPath.endsWith("comments")) {
      setCommentsError(message);
    } else if (propertyPath.endsWith("correctionModel")) {
      setCorrectionModelError(message);
    } else if (propertyPath.endsWith("observationRate")) {
      setObservationRateError(message);
    } else if (propertyPath.endsWith("departureTie")) {
      setDepartureTieError(message);
    } else if (propertyPath.endsWith("arrivalTie")) {
      setArrivalTieError(message);
    } else if (propertyPath.endsWith("driftPerDay")) {
      setDriftPerDayError(message);
    }
  }
}
