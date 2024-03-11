package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.magnetics;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.MagneticsCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.service.metadata.InstrumentMetadata;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.MagneticsDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModelFactory;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.AdditionalFieldsPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class MagneticsDatasetPanelFactory extends DatasetPanelFactory<MagneticsAdditionalFieldsModel, MagneticsDatasetInstrumentController> {

  private final MagneticsCorrectionModelDatastore magneticsCorrectionModelDatastore;

  @Autowired
  protected MagneticsDatasetPanelFactory(InstrumentDatastore instrumentDatastore, MagneticsCorrectionModelDatastore magneticsCorrectionModelDatastore) {
    super(instrumentDatastore);
    this.magneticsCorrectionModelDatastore = magneticsCorrectionModelDatastore;
  }


  @Override
  protected MagneticsAdditionalFieldsModel createAdditionalFieldsModel() {
    return new MagneticsAdditionalFieldsModel();
  }

  @Override
  protected MagneticsAdditionalFieldsModel createAdditionalFieldsModel(Instrument instrument) {
    return AdditionalFieldsModelFactory.magnetics(
        instrument.getOtherFields(),
        magneticsCorrectionModelDatastore.getCorrectionModelDropDowns(),
        MagneticsCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL
    );
  }

  @Override
  protected AdditionalFieldsPanel<MagneticsAdditionalFieldsModel, MagneticsDatasetInstrumentController> createAdditionalFieldsView(
      BaseDatasetInstrumentModel<MagneticsAdditionalFieldsModel> model, MagneticsDatasetInstrumentController controller) {
    return new MagneticsDatasetPanel(model.getAdditionalFieldsModel(), controller, magneticsCorrectionModelDatastore.getCorrectionModelDropDowns());
  }

  @Override
  protected MagneticsDatasetInstrumentController createController(BaseDatasetInstrumentModel<MagneticsAdditionalFieldsModel> model) {
    return new MagneticsDatasetInstrumentController(model);
  }
}
