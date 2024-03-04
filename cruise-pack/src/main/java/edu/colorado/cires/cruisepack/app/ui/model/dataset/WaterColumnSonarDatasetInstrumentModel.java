package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.WaterColumnCalibrationStateDatastore;
import edu.colorado.cires.cruisepack.app.service.InstrumentStatus;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathExists;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsFile;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidInstrumentDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidWaterColumnCalibrationStateDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class WaterColumnSonarDatasetInstrumentModel extends BaseDatasetInstrumentModel {

  public static final String UPDATE_INSTRUMENT = "UPDATE_INSTRUMENT";
  public static final String UPDATE_INSTRUMENT_ERROR = "UPDATE_INSTRUMENT_ERROR";
  public static final String UPDATE_PROCESSING_LEVEL = "UPDATE_PROCESSING_LEVEL";
  public static final String UPDATE_PROCESSING_LEVEL_ERROR = "UPDATE_PROCESSING_LEVEL_ERROR";
  public static final String UPDATE_COMMENTS = "UPDATE_COMMENTS";
  public static final String UPDATE_COMMENTS_ERROR = "UPDATE_COMMENTS_ERROR";
  public static final String UPDATE_CALIBRATION_STATE = "UPDATE_CALIBRATION_STATE";
  public static final String UPDATE_CALIBRATION_STATE_ERROR = "UPDATE_CALIBRATION_STATE_ERROR";
  public static final String UPDATE_CALIBRATION_REPORT_PATH = "UPDATE_CALIBRATION_REPORT_PATH";
  public static final String UPDATE_CALIBRATION_REPORT_PATH_ERROR = "UPDATE_CALIBRATION_REPORT_PATH_ERROR";
  public static final String UPDATE_CALIBRATION_DATA_PATH = "UPDATE_CALIBRATION_DATA_PATH";
  public static final String UPDATE_CALIBRATION_DATA_PATH_ERROR = "UPDATE_CALIBRATION_DATA_PATH_ERROR";
  public static final String UPDATE_CALIBRATION_DATE = "UPDATE_CALIBRATION_DATE";
  public static final String UPDATE_CALIBRATION_DATE_ERROR = "UPDATE_CALIBRATION_DATE_ERROR";

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
  @ValidWaterColumnCalibrationStateDropDownItem
  private DropDownItem calibrationState = WaterColumnCalibrationStateDatastore.UNSELECTED_CALIBRATION_STATE;
  private String calibrationStateError = null;
  @NotNull
  @PathExists
  @PathIsFile
  private Path calibrationReportPath;
  private String calibrationReportPathError = null;
  @NotNull
  @PathExists
  @PathIsFile
  private Path calibrationDataPath;
  private String calibrationDataPathError = null;
  @NotNull
  private LocalDate calibrationDate;
  private String calibrationDateError = null;

  public WaterColumnSonarDatasetInstrumentModel(String instrumentGroupShortCode) {
    super(instrumentGroupShortCode);
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

  @Override
  protected HashMap<String, Object> getAdditionalFields() {
    HashMap<String, Object> map = new HashMap<>(0);
    map.put("calibration_state", calibrationState.getValue());
    map.put("calibration_report_path", calibrationReportPath);
    map.put("calibration_data_path", calibrationDataPath);
    map.put("calibration_date", calibrationDate.toString());
    return map;
  }

  public void setComments(String comments) {
    setIfChanged(UPDATE_COMMENTS, comments, () -> this.comments, (nv) -> this.comments = nv);
  }

  public DropDownItem getCalibrationState() {
    return calibrationState;
  }

  public void setCalibrationState(DropDownItem calibrationState) {
    setIfChanged(UPDATE_CALIBRATION_STATE, calibrationState, () -> this.calibrationState, (nv) -> this.calibrationState = nv);
  }

  public Path getCalibrationReportPath() {
    return calibrationReportPath;
  }

  public void setCalibrationReportPath(Path calibrationReportPath) {
    setIfChanged(UPDATE_CALIBRATION_REPORT_PATH, calibrationReportPath, () -> this.calibrationReportPath, (nv) -> this.calibrationReportPath = nv);
  }

  public Path getCalibrationDataPath() {
    return calibrationDataPath;
  }

  public void setCalibrationDataPath(Path calibrationDataPath) {
    setIfChanged(UPDATE_CALIBRATION_DATA_PATH, calibrationDataPath, () -> this.calibrationDataPath, (nv) -> this.calibrationDataPath = nv);
  }

  public LocalDate getCalibrationDate() {
    return calibrationDate;
  }

  public void setCalibrationDate(LocalDate calibrationDate) {
    setIfChanged(UPDATE_CALIBRATION_DATE, calibrationDate, () -> this.calibrationDate, (nv) -> this.calibrationDate = nv);
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

  @Override
  public void clearErrors() {
    setPublicReleaseDateError(null);
    setDataPathError(null);
    setInstrumentError(null);
    setProcessingLevelError(null);
    setCommentsError(null);
    setCalibrationDateError(null);
    setCalibrationStateError(null);
    setCalibrationDataPathError(null);
    setCalibrationReportPathError(null);
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

  private void setCalibrationStateError(String message) {
    setIfChanged(UPDATE_CALIBRATION_STATE_ERROR, message, () -> this.calibrationStateError, (e) -> this.calibrationStateError = e);
  }

  private void setCalibrationReportPathError(String message) {
    setIfChanged(UPDATE_CALIBRATION_REPORT_PATH_ERROR, message, () -> this.calibrationReportPathError, (e) -> this.calibrationReportPathError = e);
  }

  private void setCalibrationDataPathError(String message) {
    setIfChanged(UPDATE_CALIBRATION_DATA_PATH_ERROR, message, () -> this.calibrationDataPathError, (e) -> this.calibrationDataPathError = e);
  }

  private void setCalibrationDateError(String message) {
    setIfChanged(UPDATE_CALIBRATION_DATE_ERROR, message, () -> this.calibrationDateError, (e) -> this.calibrationDateError = e);
  }

  @Override
  protected void setErrors(String propertyPath, String message) {
    if (propertyPath.endsWith("instrument")) {
      setInstrumentError(message);
    } else if (propertyPath.endsWith("processingLevel")) {
      setProcessingLevelError(message);
    } else if (propertyPath.endsWith("comments")) {
      setCommentsError(message);
    } else if (propertyPath.endsWith("calibrationState")) {
      setCalibrationStateError(message);
    } else if (propertyPath.endsWith("calibrationReportPath")) {
      setCalibrationReportPathError(message);
    } else if (propertyPath.endsWith("calibrationDataPath")) {
      setCalibrationDataPathError(message);
    } else if (propertyPath.endsWith("calibrationDate")) {
      setCalibrationDateError(message);
    }
  }

}
