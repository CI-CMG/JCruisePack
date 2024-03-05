package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.datastore.WaterColumnCalibrationStateDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathExists;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsFile;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidWaterColumnCalibrationStateDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;

public class WaterColumnSonarDatasetInstrumentModel extends BaseDatasetInstrumentModel {
  public static final String UPDATE_CALIBRATION_STATE = "UPDATE_CALIBRATION_STATE";
  public static final String UPDATE_CALIBRATION_STATE_ERROR = "UPDATE_CALIBRATION_STATE_ERROR";
  public static final String UPDATE_CALIBRATION_REPORT_PATH = "UPDATE_CALIBRATION_REPORT_PATH";
  public static final String UPDATE_CALIBRATION_REPORT_PATH_ERROR = "UPDATE_CALIBRATION_REPORT_PATH_ERROR";
  public static final String UPDATE_CALIBRATION_DATA_PATH = "UPDATE_CALIBRATION_DATA_PATH";
  public static final String UPDATE_CALIBRATION_DATA_PATH_ERROR = "UPDATE_CALIBRATION_DATA_PATH_ERROR";
  public static final String UPDATE_CALIBRATION_DATE = "UPDATE_CALIBRATION_DATE";
  public static final String UPDATE_CALIBRATION_DATE_ERROR = "UPDATE_CALIBRATION_DATE_ERROR";

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

  @Override
  protected HashMap<String, Object> getAdditionalFields() {
    HashMap<String, Object> map = new HashMap<>(0);
    map.put("calibration_state", calibrationState.getValue());
    map.put("calibration_report_path", calibrationReportPath);
    map.put("calibration_data_path", calibrationDataPath);
    map.put("calibration_date", calibrationDate.toString());
    return map;
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
