package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.BaseDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import org.springframework.stereotype.Component;

@Component
public class DefaultDatasetPanelFactory extends DatasetPanelFactory<AdditionalFieldsModel, BaseDatasetInstrumentController<AdditionalFieldsModel>> {

  protected DefaultDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  protected AdditionalFieldsModel createAdditionalFieldsModel() {
    return null;
  }

  @Override
  protected AdditionalFieldsModel createAdditionalFieldsModel(Instrument instrument) {
    return null;
  }

  @Override
  protected AdditionalFieldsPanel<AdditionalFieldsModel, BaseDatasetInstrumentController<AdditionalFieldsModel>> createAdditionalFieldsView(
      BaseDatasetInstrumentModel<AdditionalFieldsModel> model, BaseDatasetInstrumentController<AdditionalFieldsModel> controller) {
    return null;
  }

  @Override
  protected BaseDatasetInstrumentController<AdditionalFieldsModel> createController(BaseDatasetInstrumentModel<AdditionalFieldsModel> model) {
    return new BaseDatasetInstrumentController<>(model) {};
  }


}
