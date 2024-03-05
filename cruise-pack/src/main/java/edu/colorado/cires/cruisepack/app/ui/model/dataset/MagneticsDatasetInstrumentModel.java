package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.datastore.MagneticsCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidMagneticsCorrectionModelDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;

public class MagneticsDatasetInstrumentModel extends BaseDatasetInstrumentModel {
  public static final String UPDATE_CORRECTION_MODEL = "UPDATE_CORRECTION_MODEL";
  public static final String UPDATE_CORRECTION_MODEL_ERROR = "UPDATE_CORRECTION_MODEL_ERROR";
  public static final String UPDATE_SAMPLE_RATE = "UPDATE_SAMPLE_RATE";
  public static final String UPDATE_SAMPLE_RATE_ERROR = "UPDATE_SAMPLE_RATE_ERROR";
  public static final String UPDATE_TOW_DISTANCE = "UPDATE_TOW_DISTANCE";
  public static final String UPDATE_TOW_DISTANCE_ERROR = "UPDATE_TOW_DISTANCE_ERROR";
  public static final String UPDATE_SENSOR_DEPTH = "UPDATE_SENSOR_DEPTH";
  public static final String UPDATE_SENSOR_DEPTH_ERROR = "UPDATE_SENSOR_DEPTH_ERROR";

  @ValidMagneticsCorrectionModelDropDownItem
  private DropDownItem correctionModel = MagneticsCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL;
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
  public void clearErrors() {
    setPublicReleaseDateError(null);
    setDataPathError(null);
    setInstrumentError(null);
    setProcessingLevelError(null);
    setCommentsError(null);
    setCorrectionModelError(null);
    setSampleRateError(null);
    setTowDistanceError(null);
    setSensorDepthError(null);
  }

  @Override
  protected HashMap<String, Object> getAdditionalFields() {
    HashMap<String, Object> map = new HashMap<>(0);
    map.put("correction_model", correctionModel.getValue());
    map.put("sample_rate", sampleRate);
    map.put("tow_distance", towDistance);
    map.put("sensor_depth", sensorDepth);
    return map;
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
