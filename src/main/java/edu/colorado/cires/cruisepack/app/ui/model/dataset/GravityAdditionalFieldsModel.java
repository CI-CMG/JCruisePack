package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.datastore.GravityCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.IsFloatingPointNumber;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidGravityCorrectionModelDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

public class GravityAdditionalFieldsModel extends AdditionalFieldsModel {
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
  @IsFloatingPointNumber
  private String observationRate;
  private String observationRateError = null;
  @NotBlank
  @IsFloatingPointNumber
  private String departureTie;
  private String departureTieError = null;
  @NotBlank
  @IsFloatingPointNumber
  private String arrivalTie;
  private String arrivalTieError = null;
  @NotBlank
  @IsFloatingPointNumber
  private String driftPerDay;
  private String driftPerDayError = null;

  @Override
  public void clearErrors() {
    setCorrectionModelError(null);
    setObservationRateError(null);
    setDepartureTieError(null);
    setArrivalTieError(null);
    setDriftPerDayError(null);
  }

  @Override
  protected void setError(String propertyPath, String message) {
    if (propertyPath.endsWith("correctionModel")) {
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

  @Override
  public Map<String, Object> transform() {
    HashMap<String, Object> map = new HashMap<>(0);
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

  public void setCorrectionModelError(String message) {
    setIfChanged(UPDATE_CORRECTION_MODEL_ERROR, message, () -> this.correctionModelError, (e) -> this.correctionModelError = e);
  }

  public void setObservationRateError(String message) {
    setIfChanged(UPDATE_OBSERVATION_RATE_ERROR, message, () -> this.observationRateError, (e) -> this.observationRateError = e);
  }

  public void setDepartureTieError(String message) {
    setIfChanged(UPDATE_DEPARTURE_TIE_ERROR, message, () -> this.departureTieError, (e) -> this.departureTieError = e);
  }

  public void setArrivalTieError(String message) {
    setIfChanged(UPDATE_ARRIVAL_TIE_ERROR, message, () -> this.arrivalTieError, (e) -> this.arrivalTieError = e);
  }

  public void setDriftPerDayError(String message) {
    setIfChanged(UPDATE_DRIFT_PER_DAY_ERROR, message, () -> this.driftPerDayError, (e) -> this.driftPerDayError = e);
  }

  public String getCorrectionModelError() {
    return correctionModelError;
  }

  public String getObservationRateError() {
    return observationRateError;
  }

  public String getDepartureTieError() {
    return departureTieError;
  }

  public String getArrivalTieError() {
    return arrivalTieError;
  }

  public String getDriftPerDayError() {
    return driftPerDayError;
  }
}
