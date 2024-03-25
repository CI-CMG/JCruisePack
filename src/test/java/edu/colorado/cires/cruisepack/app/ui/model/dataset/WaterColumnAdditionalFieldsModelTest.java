package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.cruisepack.app.datastore.WaterColumnCalibrationStateDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModelTest;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;

class WaterColumnAdditionalFieldsModelTest extends PropertyChangeModelTest<WaterColumnAdditionalFieldsModel> {

  @Override
  protected WaterColumnAdditionalFieldsModel createModel() {
    return new WaterColumnAdditionalFieldsModel();
  }

  @Test
  void setCalibrationState() {
    assertPropertyChange(
        WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_STATE,
        model::getCalibrationState,
        model::setCalibrationState,
        new DropDownItem("1", "value 1"),
        new DropDownItem("2", "value 2"),
        WaterColumnCalibrationStateDatastore.UNSELECTED_CALIBRATION_STATE
    );
  }

  @Test
  void setCalibrationReportPath() {
    assertPropertyChange(
        WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_REPORT_PATH,
        model::getCalibrationReportPath,
        model::setCalibrationReportPath,
        Paths.get("path1"),
        Paths.get("path2"),
        null
    );
  }

  @Test
  void setCalibrationDataPath() {
    assertPropertyChange(
        WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_DATA_PATH,
        model::getCalibrationDataPath,
        model::setCalibrationDataPath,
        Paths.get("path1"),
        Paths.get("path2"),
        null
    );
  }

  @Test
  void setCalibrationDate() {
    assertPropertyChange(
        WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_DATE,
        model::getCalibrationDate,
        model::setCalibrationDate,
        LocalDate.now(),
        LocalDate.now().plusDays(1),
        null
    );
  }

  @Test
  void clearErrors() {
    String calibrationStateError = "calibration state error";
    String calibrationReportPathError = "calibration report path error";
    String calibrationDataPathError = "calibration data path error";
    String calibrationDateError = "calibration date error";
    
    model.setCalibrationStateError(calibrationStateError);
    model.setCalibrationReportPathError(calibrationReportPathError);
    model.setCalibrationDataPathError(calibrationDataPathError);
    model.setCalibrationDateError(calibrationDateError);
    
    clearEvents();
    
    model.clearErrors();
    
    assertChangeEvent(WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_STATE_ERROR, calibrationStateError, null);
    assertNull(model.getCalibrationStateError());
    assertChangeEvent(WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_REPORT_PATH_ERROR, calibrationReportPathError, null);
    assertNull(model.getCalibrationReportPathError());
    assertChangeEvent(WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_DATA_PATH_ERROR, calibrationDataPathError, null);
    assertNull(model.getCalibrationDataPathError());
    assertChangeEvent(WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_DATE_ERROR, calibrationDateError, null);
    assertNull(model.getCalibrationDateError());
  }

  @Test
  void setError() {
    String calibrationStateError = "calibration state error";
    String calibrationReportPathError = "calibration report path error";
    String calibrationDataPathError = "calibration data path error";
    String calibrationDateError = "calibration date error";
    
    model.setError("calibrationState", calibrationStateError);
    model.setError("calibrationReportPath", calibrationReportPathError);
    model.setError("calibrationDataPath", calibrationDataPathError);
    model.setError("calibrationDate", calibrationDateError);

    assertChangeEvent(WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_STATE_ERROR, null, calibrationStateError);
    assertEquals(calibrationStateError, model.getCalibrationStateError());
    assertChangeEvent(WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_REPORT_PATH_ERROR, null, calibrationReportPathError);
    assertEquals(calibrationReportPathError, model.getCalibrationReportPathError());
    assertChangeEvent(WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_DATA_PATH_ERROR, null, calibrationDataPathError);
    assertEquals(calibrationDataPathError, model.getCalibrationDataPathError());
    assertChangeEvent(WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_DATE_ERROR, null, calibrationDateError);
    assertEquals(calibrationDateError, model.getCalibrationDateError());
  }

  @Test
  void transform() {
    LocalDate date = LocalDate.now();
    
    model.setCalibrationState(new DropDownItem("1", "value 1"));
    Path calibrationReportPath = Paths.get("calibration report path");
    model.setCalibrationReportPath(calibrationReportPath);
    Path calibrationDataPath = Paths.get("calibration data path");
    model.setCalibrationDataPath(calibrationDataPath);
    model.setCalibrationDate(date);
    
    assertEquals(
        Map.of(
            "calibration_state", "value 1",
            "calibration_report_path", calibrationReportPath,
            "calibration_data_path", calibrationDataPath,
            "calibration_date", date.toString()
        ),
        model.transform()
    );
  }

  @Test
  void setCalibrationStateError() {
    assertPropertyChange(
        WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_STATE_ERROR,
        model::getCalibrationStateError,
        model::setCalibrationStateError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setCalibrationReportPathError() {
    assertPropertyChange(
        WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_REPORT_PATH_ERROR,
        model::getCalibrationReportPathError,
        model::setCalibrationReportPathError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setCalibrationDataPathError() {
    assertPropertyChange(
        WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_DATA_PATH_ERROR,
        model::getCalibrationDataPathError,
        model::setCalibrationDataPathError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setCalibrationDateError() {
    assertPropertyChange(
        WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_DATE_ERROR,
        model::getCalibrationDateError,
        model::setCalibrationDateError,
        "value1",
        "value2",
        null
    );
  }
}