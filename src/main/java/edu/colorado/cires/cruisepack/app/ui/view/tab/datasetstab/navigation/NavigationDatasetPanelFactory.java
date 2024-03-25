package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.navigation;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.NavigationDatumDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.NavigationDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModelFactory;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.AdditionalFieldsPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class NavigationDatasetPanelFactory extends DatasetPanelFactory<NavigationAdditionalFieldsModel, NavigationDatasetInstrumentController> {

  private final NavigationDatumDatastore navigationDatumDatastore;

  protected NavigationDatasetPanelFactory(InstrumentDatastore instrumentDatastore, NavigationDatumDatastore navigationDatumDatastore) {
    super(instrumentDatastore);
    this.navigationDatumDatastore = navigationDatumDatastore;
  }

  @Override
  protected NavigationAdditionalFieldsModel createAdditionalFieldsModel() {
    return new NavigationAdditionalFieldsModel();
  }

  @Override
  protected NavigationAdditionalFieldsModel createAdditionalFieldsModel(Instrument instrument) {
    return AdditionalFieldsModelFactory.navigation(
        instrument.getOtherFields(),
        navigationDatumDatastore.getNavigationDatumDropDowns()
    );
  }

  @Override
  protected AdditionalFieldsPanel<NavigationAdditionalFieldsModel, NavigationDatasetInstrumentController> createAdditionalFieldsView(
      BaseDatasetInstrumentModel<NavigationAdditionalFieldsModel> model, NavigationDatasetInstrumentController controller) {
    return new NavigationDatasetPanel(model.getAdditionalFieldsModel(), controller, navigationDatumDatastore.getNavigationDatumDropDowns());
  }

  @Override
  protected NavigationDatasetInstrumentController createController(BaseDatasetInstrumentModel<NavigationAdditionalFieldsModel> model) {
    return new NavigationDatasetInstrumentController(model);
  }
}
