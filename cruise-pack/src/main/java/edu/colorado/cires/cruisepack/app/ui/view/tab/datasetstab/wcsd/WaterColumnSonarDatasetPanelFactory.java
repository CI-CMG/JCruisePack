package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.wcsd;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.WaterColumnCalibrationStateDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.WaterColumnSonarDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModelFactory;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaterColumnSonarDatasetPanelFactory extends
    DatasetPanelFactory<BaseDatasetInstrumentModel<WaterColumnAdditionalFieldsModel>, WaterColumnSonarDatasetInstrumentController, WaterColumnSonarDatasetPanel> {

  private final WaterColumnCalibrationStateDatastore waterColumnCalibrationStateDatastore;

  @Autowired
  public WaterColumnSonarDatasetPanelFactory(InstrumentDatastore instrumentDatastore, WaterColumnCalibrationStateDatastore waterColumnCalibrationStateDatastore) {
    super(instrumentDatastore);
    this.waterColumnCalibrationStateDatastore = waterColumnCalibrationStateDatastore;
  }

  @Override
  protected BaseDatasetInstrumentModel<WaterColumnAdditionalFieldsModel> createModel(InstrumentGroupName groupName) {
    BaseDatasetInstrumentModel<WaterColumnAdditionalFieldsModel> model = new BaseDatasetInstrumentModel<>(groupName.getShortName()) {};
    model.setAdditionalFieldsModel(new WaterColumnAdditionalFieldsModel());
    return model;
  }

  @Override
  protected BaseDatasetInstrumentModel<WaterColumnAdditionalFieldsModel> createModel(InstrumentGroupName groupName, Instrument instrument) {
    BaseDatasetInstrumentModel<WaterColumnAdditionalFieldsModel> model = createModel(groupName);
//    model.setDataPath(); TODO
    model.setComments(instrument.getDataComment());
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));
    model.setAdditionalFieldsModel(
        AdditionalFieldsModelFactory.waterColumn(
            instrument.getOtherFields(),
            waterColumnCalibrationStateDatastore.getCalibrationStateDropDowns(),
            WaterColumnCalibrationStateDatastore.UNSELECTED_CALIBRATION_STATE
        )
    );
    model.setProcessingLevel(instrument.getStatus());
    if (instrument.getReleaseDate() != null) {
      model.setPublicReleaseDate(LocalDate.parse(instrument.getReleaseDate()));
    }
    return model;
  }

  @Override
  protected WaterColumnSonarDatasetInstrumentController createController(BaseDatasetInstrumentModel<WaterColumnAdditionalFieldsModel> model) {
    return new WaterColumnSonarDatasetInstrumentController(model);
  }

  @Override
  protected WaterColumnSonarDatasetPanel createView(BaseDatasetInstrumentModel<WaterColumnAdditionalFieldsModel> model, WaterColumnSonarDatasetInstrumentController controller) {
    return new WaterColumnSonarDatasetPanel(
      model,
      controller,
      instrumentDatastore,
      waterColumnCalibrationStateDatastore.getCalibrationStateDropDowns()
    );
  }

}
