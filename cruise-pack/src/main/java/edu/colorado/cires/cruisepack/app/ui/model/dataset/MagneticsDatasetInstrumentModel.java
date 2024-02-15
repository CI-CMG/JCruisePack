package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

public class MagneticsDatasetInstrumentModel extends BaseDatasetInstrumentModel {

  public static final String UPDATE_INSTRUMENT = "UPDATE_INSTRUMENT";
  public static final String UPDATE_PROCESSING_LEVEL = "UPDATE_PROCESSING";
  public static final String UPDATE_COMMENTS = "UPDATE_COMMENTS";
  public static final String UPDATE_CORRECTION_MODEL = "UPDATE_CORRECTION_MODEL";
  public static final String UPDATE_SAMPLE_RATE = "UPDATE_SAMPLE_RATE";
  public static final String UPDATE_TOW_DISTANCE = "UPDATE_TOW_DISTANCE";
  public static final String UPDATE_SENSOR_DEPTH = "UPDATE_SENSOR_DEPTH";

  // TODO move this to datasource
  private DropDownItem instrument;
  private String processingLevel = "Raw";
  private String comments;

  private DropDownItem correctionModel;
  private String sampleRate;
  private String towDistance;
  private String sensorDepth;

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
}
