package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.datastore.WaterColumnCalibrationStateDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathExists;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsFile;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidWaterColumnCalibrationStateDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidWaterColumnFields;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@ValidWaterColumnFields
public class WaterColumnAdditionalFieldsModel extends AdditionalFieldsModel {
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
  @PathExists
  @PathIsFile
  private Path calibrationReportPath;
  private String calibrationReportPathError = null;
  @PathExists
  @PathIsFile
  private Path calibrationDataPath;
  private String calibrationDataPathError = null;
  private LocalDate calibrationDate;
  private String calibrationDateError = null;

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
    setCalibrationDateError(null);
    setCalibrationStateError(null);
    setCalibrationDataPathError(null);
    setCalibrationReportPathError(null);
  }

  @Override
  protected void setError(String propertyPath, String message) {
    if (propertyPath.endsWith("calibrationState")) {
      setCalibrationStateError(message);
    } else if (propertyPath.endsWith("calibrationReportPath")) {
      setCalibrationReportPathError(message);
    } else if (propertyPath.endsWith("calibrationDataPath")) {
      setCalibrationDataPathError(message);
    } else if (propertyPath.endsWith("calibrationDate")) {
      setCalibrationDateError(message);
    }
  }

  @Override
  public Map<String, Object> transform() {
    HashMap<String, Object> map = new HashMap<>(0);
    map.put("calibration_state", calibrationState.getValue());
    map.put("calibration_date", calibrationDate == null ? null : calibrationDate.toString());
    map.put("calibration_report_path", calibrationReportPath);
    map.put("calibration_data_path", calibrationDataPath);
    return map;
  }

  public void setCalibrationStateError(String message) {
    setIfChanged(UPDATE_CALIBRATION_STATE_ERROR, message, () -> this.calibrationStateError, (e) -> this.calibrationStateError = e);
  }

  public void setCalibrationReportPathError(String message) {
    setIfChanged(UPDATE_CALIBRATION_REPORT_PATH_ERROR, message, () -> this.calibrationReportPathError, (e) -> this.calibrationReportPathError = e);
  }

  public void setCalibrationDataPathError(String message) {
    setIfChanged(UPDATE_CALIBRATION_DATA_PATH_ERROR, message, () -> this.calibrationDataPathError, (e) -> this.calibrationDataPathError = e);
  }

  public void setCalibrationDateError(String message) {
    setIfChanged(UPDATE_CALIBRATION_DATE_ERROR, message, () -> this.calibrationDateError, (e) -> this.calibrationDateError = e);
  }

  public String getCalibrationStateError() {
    return calibrationStateError;
  }

  public String getCalibrationReportPathError() {
    return calibrationReportPathError;
  }

  public String getCalibrationDataPathError() {
    return calibrationDataPathError;
  }

  public String getCalibrationDateError() {
    return calibrationDateError;
  }
}
