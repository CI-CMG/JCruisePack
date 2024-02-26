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

public class MagneticsDatasetInstrumentModel extends BaseDatasetInstrumentModel {

  public static final String UPDATE_INSTRUMENT = "UPDATE_INSTRUMENT";
  public static final String UPDATE_INSTRUMENT_ERROR = "UPDATE_INSTRUMENT_ERROR";
  public static final String UPDATE_PROCESSING_LEVEL = "UPDATE_PROCESSING_LEVEL";
  public static final String UPDATE_PROCESSING_LEVEL_ERROR = "UPDATE_PROCESSING_LEVEL_ERROR";
  public static final String UPDATE_COMMENTS = "UPDATE_COMMENTS";
  public static final String UPDATE_COMMENTS_ERROR = "UPDATE_COMMENTS_ERROR";
  public static final String UPDATE_CORRECTION_MODEL = "UPDATE_CORRECTION_MODEL";
  public static final String UPDATE_CORRECTION_MODEL_ERROR = "UPDATE_CORRECTION_MODEL_ERROR";
  public static final String UPDATE_SAMPLE_RATE = "UPDATE_SAMPLE_RATE";
  public static final String UPDATE_SAMPLE_RATE_ERROR = "UPDATE_SAMPLE_RATE_ERROR";
  public static final String UPDATE_TOW_DISTANCE = "UPDATE_TOW_DISTANCE";
  public static final String UPDATE_TOW_DISTANCE_ERROR = "UPDATE_TOW_DISTANCE_ERROR";
  public static final String UPDATE_SENSOR_DEPTH = "UPDATE_SENSOR_DEPTH";
  public static final String UPDATE_SENSOR_DEPTH_ERROR = "UPDATE_SENSOR_DEPTH_ERROR";

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
  private String sampleRate;
  private String sampleRateError = null;
  @NotBlank
  private String towDistance;
  private String towDistanceError = null;
  @NotBlank
  private String sensorDepth;
  private String sensorDepthError = null;

  public MagneticsDatasetInstrumentModel(String instrumentGroupShortCode) {
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

  public String getSampleRate() {
    return sampleRate;
  }

  public void setSampleRate(String sampleRate) {
    setIfChanged(UPDATE_SAMPLE_RATE, sampleRate, () -> this.sampleRate, (nv) -> this.sampleRate = nv);
  }

  public String getTowDistance() {
    return towDistance;
  }

  public void setTowDistance(String towDistance) {
    setIfChanged(UPDATE_TOW_DISTANCE, towDistance, () -> this.towDistance, (nv) -> this.towDistance = nv);
  }

  public String getSensorDepth() {
    return sensorDepth;
  }

  public void setSensorDepth(String sensorDepth) {
    setIfChanged(UPDATE_SENSOR_DEPTH, sensorDepth, () -> this.sensorDepth, (nv) -> this.sensorDepth = nv);
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

  private void setSampleRateError(String message) {
    setIfChanged(UPDATE_SAMPLE_RATE_ERROR, message, () -> this.sampleRateError, (e) -> this.sampleRateError = e);
  }

  private void setTowDistanceError(String message) {
    setIfChanged(UPDATE_TOW_DISTANCE_ERROR, message, () -> this.towDistanceError, (e) -> this.towDistanceError = e);
  }

  private void setSensorDepthError(String message) {
    setIfChanged(UPDATE_SENSOR_DEPTH_ERROR, message, () -> this.sensorDepthError, (e) -> this.sensorDepthError = e);
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
    } else if (propertyPath.endsWith("sampleRate")) {
      setSampleRateError(message);
    } else if (propertyPath.endsWith("towDistance")) {
      setTowDistanceError(message);
    } else if (propertyPath.endsWith("sensorDepth")) {
      setSensorDepthError(message);
    }
  }
}
