package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.gravity;

import edu.colorado.cires.cruisepack.app.datastore.GravityCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.service.metadata.InstrumentMetadata;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.GravityDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModelFactory;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.AdditionalFieldsPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GravityDatasetPanelFactory extends DatasetPanelFactory<GravityAdditionalFieldsModel, GravityDatasetInstrumentController> {

  private final GravityCorrectionModelDatastore gravityCorrectionModelDatastore;

  @Autowired
  public GravityDatasetPanelFactory(InstrumentDatastore instrumentDatastore, GravityCorrectionModelDatastore gravityCorrectionModelDatastore) {
    super(instrumentDatastore);
    this.gravityCorrectionModelDatastore = gravityCorrectionModelDatastore;
  }


  @Override
  protected GravityAdditionalFieldsModel createAdditionalFieldsModel() {
    return new GravityAdditionalFieldsModel();
  }

  @Override
  protected GravityAdditionalFieldsModel createAdditionalFieldsModel(Instrument instrument) {
    return AdditionalFieldsModelFactory.gravity(
        instrument.getOtherFields(),
        gravityCorrectionModelDatastore.getCorrectionModelDropDowns(),
        GravityCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL
    );
  }

  @Override
  protected AdditionalFieldsPanel<GravityAdditionalFieldsModel, GravityDatasetInstrumentController> createAdditionalFieldsView(
      BaseDatasetInstrumentModel<GravityAdditionalFieldsModel> model, GravityDatasetInstrumentController controller) {
    return new GravityDatasetPanel(model.getAdditionalFieldsModel(), controller, gravityCorrectionModelDatastore.getCorrectionModelDropDowns());
  }

  @Override
  protected GravityDatasetInstrumentController createController(BaseDatasetInstrumentModel<GravityAdditionalFieldsModel> model) {
    return new GravityDatasetInstrumentController(model);
  }
}
