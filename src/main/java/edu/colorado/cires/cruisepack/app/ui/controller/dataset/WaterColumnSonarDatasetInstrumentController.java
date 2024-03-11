package edu.colorado.cires.cruisepack.app.ui.controller.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.nio.file.Path;
import java.time.LocalDate;


public class WaterColumnSonarDatasetInstrumentController extends BaseDatasetInstrumentController<WaterColumnAdditionalFieldsModel> {

  public WaterColumnSonarDatasetInstrumentController(BaseDatasetInstrumentModel<WaterColumnAdditionalFieldsModel> model) {
    super(model);
  }

  public void setCalibrationState(DropDownItem calibrationState) {
    model.getAdditionalFieldsModel().setCalibrationState(calibrationState);
  }


  public void setCalibrationReportPath(Path calibrationReportPath) {
    model.getAdditionalFieldsModel().setCalibrationReportPath(calibrationReportPath);
  }

  public void setCalibrationDataPath(Path calibrationDataPath) {
    model.getAdditionalFieldsModel().setCalibrationDataPath(calibrationDataPath);
  }

  public void setCalibrationDate(LocalDate calibrationDate) {
    model.getAdditionalFieldsModel().setCalibrationDate(calibrationDate);
  }
}
