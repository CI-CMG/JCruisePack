package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.InstrumentStatus;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidInstrumentDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class GravityDatasetInstrumentModel extends BaseDatasetInstrumentModel {

  public static final String UPDATE_INSTRUMENT = "UPDATE_INSTRUMENT";
  public static final String UPDATE_INSTRUMENT_ERROR = "UPDATE_INSTRUMENT_ERROR";
  public static final String UPDATE_PROCESSING_LEVEL = "UPDATE_PROCESSING_LEVEL";
  public static final String UPDATE_PROCESSING_LEVEL_ERROR = "UPDATE_PROCESSING_LEVEL_ERROR";
  public static final String UPDATE_COMMENTS = "UPDATE_COMMENTS";
  public static final String UPDATE_COMMENTS_ERROR = "UPDATE_COMMENTS_ERROR";
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

  // TODO move this to datasource
  @ValidInstrumentDropDownItem
  private DropDownItem instrument = InstrumentDatastore.UNSELECTED_INSTRUMENT;
  private String instrumentError = null;
  @NotBlank
  private String processingLevel = "Raw";
  private String processingLevelError = null;
  @NotBlank
  private String comments;
  private String commentsError = null;
  @NotNull @ValidInstrumentDropDownItem
  private DropDownItem correctionModel;
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
  public Optional<DropDownItem> getSelectedInstrument() {
    if (instrument == null || StringUtils.isBlank(instrument.getId())) {
      return Optional.empty();
    }
    return Optional.of(instrument);
  }

  @Override
  protected InstrumentStatus getSelectedInstrumentProcessingLevel() {
    return InstrumentStatus.forValue(processingLevel);
  }

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

  private void setInstrumentError(String message) {
    setIfChanged(UPDATE_INSTRUMENT_ERROR, message, () -> this.instrumentError, (e) -> this.instrumentError = e);
  }

  private void setProcessingLevelError(String message) {
    setIfChanged(UPDATE_PROCESSING_LEVEL_ERROR, message, () -> this.processingLevelError, (e) -> this.processingLevelError = e);
  }

  private void setCommentsError(String message) {
    setIfChanged(UPDATE_COMMENTS_ERROR, message, () -> this.commentsError, (e) -> this.commentsError = e);
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
