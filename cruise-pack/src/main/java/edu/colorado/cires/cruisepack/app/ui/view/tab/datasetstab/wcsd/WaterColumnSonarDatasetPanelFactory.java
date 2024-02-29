package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.wcsd;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.WaterColumnCalibrationStateDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.WaterColumnSonarDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaterColumnSonarDatasetPanelFactory extends
    DatasetPanelFactory<WaterColumnSonarDatasetInstrumentModel, WaterColumnSonarDatasetInstrumentController, WaterColumnSonarDatasetPanel> {

  private final WaterColumnCalibrationStateDatastore waterColumnCalibrationStateDatastore;

  @Autowired
  public WaterColumnSonarDatasetPanelFactory(InstrumentDatastore instrumentDatastore, WaterColumnCalibrationStateDatastore waterColumnCalibrationStateDatastore) {
    super(instrumentDatastore);
    this.waterColumnCalibrationStateDatastore = waterColumnCalibrationStateDatastore;
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return WaterColumnSonarDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  public String getInstrumentGroupName() {
    return "Water Column Sonar";
  }

  @Override
  protected WaterColumnSonarDatasetInstrumentModel createModel() {
    return new WaterColumnSonarDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected WaterColumnSonarDatasetInstrumentModel createModel(Instrument instrument) {
    WaterColumnSonarDatasetInstrumentModel model = createModel();
//    model.setDataPath(); TODO
    model.setComments(instrument.getDataComment());
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));

    Map<String, Object> otherFields = instrument.getOtherFields();
    
    setValueIfExists(
        "calibrationState",
        otherFields,
        String.class,
        (v) -> waterColumnCalibrationStateDatastore.getCalibrationStateDropDowns()
            .stream()
            .filter(dd -> dd.getValue().equals(v))
            .findFirst()
            .orElse(WaterColumnCalibrationStateDatastore.UNSELECTED_CALIBRATION_STATE),
        model::setCalibrationState
    );
    setValueIfExists(
        "calibrationDate",
        otherFields,
        String.class,
        (v) -> v == null ? null : LocalDate.parse(v),
        model::setCalibrationDate
    );
    setValueIfExists(
        "calibrationDataPath",
        otherFields,
        String.class,
        Paths::get,
        model::setCalibrationDataPath
    );
    setValueIfExists(
        "calibrationReportPath",
        otherFields,
        String.class,
        Paths::get,
        model::setCalibrationReportPath
    );
    model.setProcessingLevel(instrument.getStatus());
    if (instrument.getReleaseDate() != null) {
      model.setPublicReleaseDate(LocalDate.parse(instrument.getReleaseDate()));
    }
    return model;
  }

  @Override
  protected WaterColumnSonarDatasetInstrumentController createController(WaterColumnSonarDatasetInstrumentModel model) {
    return new WaterColumnSonarDatasetInstrumentController(model);
  }

  @Override
  protected WaterColumnSonarDatasetPanel createView(WaterColumnSonarDatasetInstrumentModel model,
      WaterColumnSonarDatasetInstrumentController controller) {
    return new WaterColumnSonarDatasetPanel(
      model,
      controller,
      instrumentDatastore,
      waterColumnCalibrationStateDatastore.getCalibrationStateDropDowns()
    );
  }

}
