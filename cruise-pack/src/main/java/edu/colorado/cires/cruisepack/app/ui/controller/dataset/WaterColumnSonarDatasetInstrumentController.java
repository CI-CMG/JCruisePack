package edu.colorado.cires.cruisepack.app.ui.controller.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.nio.file.Path;
import java.time.LocalDate;


public class WaterColumnSonarDatasetInstrumentController extends BaseDatasetInstrumentController<WaterColumnSonarDatasetInstrumentModel> {

  public WaterColumnSonarDatasetInstrumentController(WaterColumnSonarDatasetInstrumentModel model) {
    super(model);
  }

  public void setInstrument(DropDownItem instrument) {
    model.setInstrument(instrument);
  }

  public void setProcessingLevel(String processingLevel) {
    model.setProcessingLevel(processingLevel);
  }

  public void setComments(String comments) {
    model.setComments(comments);
  }

  public void setCalibrationState(DropDownItem calibrationState) {
    model.setCalibrationState(calibrationState);
  }


  public void setCalibrationReportPath(Path calibrationReportPath) {
    model.setCalibrationReportPath(calibrationReportPath);
  }

  public void setCalibrationDataPath(Path calibrationDataPath) {
    model.setCalibrationDataPath(calibrationDataPath);
  }

  public void setCalibrationDate(LocalDate calibrationDate) {
    model.setCalibrationDate(calibrationDate);
  }
}
