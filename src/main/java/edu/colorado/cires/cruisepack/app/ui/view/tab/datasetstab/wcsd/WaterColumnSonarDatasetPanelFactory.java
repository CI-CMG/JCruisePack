package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.wcsd;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.WaterColumnCalibrationStateDatastore;
import edu.colorado.cires.cruisepack.app.service.WaterColumnTemplateService;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.WaterColumnSonarDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModelFactory;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.AdditionalFieldsPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class WaterColumnSonarDatasetPanelFactory extends DatasetPanelFactory<WaterColumnAdditionalFieldsModel, WaterColumnSonarDatasetInstrumentController> {

  private final WaterColumnCalibrationStateDatastore waterColumnCalibrationStateDatastore;
  private final WaterColumnTemplateService waterColumnTemplateService;

  @Autowired
  protected WaterColumnSonarDatasetPanelFactory(InstrumentDatastore instrumentDatastore,
      WaterColumnCalibrationStateDatastore waterColumnCalibrationStateDatastore, WaterColumnTemplateService waterColumnTemplateService) {
    super(instrumentDatastore);
    this.waterColumnCalibrationStateDatastore = waterColumnCalibrationStateDatastore;
    this.waterColumnTemplateService = waterColumnTemplateService;
  }


  @Override
  protected WaterColumnAdditionalFieldsModel createAdditionalFieldsModel() {
    return new WaterColumnAdditionalFieldsModel();
  }

  @Override
  protected WaterColumnAdditionalFieldsModel createAdditionalFieldsModel(Instrument instrument) {
    return AdditionalFieldsModelFactory.waterColumn(
        instrument.getOtherFields(),
        waterColumnCalibrationStateDatastore.getCalibrationStateDropDowns()
    );
  }

  @Override
  protected AdditionalFieldsPanel<WaterColumnAdditionalFieldsModel, WaterColumnSonarDatasetInstrumentController> createAdditionalFieldsView(
      BaseDatasetInstrumentModel<WaterColumnAdditionalFieldsModel> model, WaterColumnSonarDatasetInstrumentController controller) {
    return new WaterColumnSonarDatasetPanel(model.getAdditionalFieldsModel(), controller, waterColumnCalibrationStateDatastore.getCalibrationStateDropDowns());
  }

  @Override
  protected WaterColumnSonarDatasetInstrumentController createController(BaseDatasetInstrumentModel<WaterColumnAdditionalFieldsModel> model) {
    return new WaterColumnSonarDatasetInstrumentController(model, waterColumnTemplateService);
  }
}
