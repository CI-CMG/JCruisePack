package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.navigation;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.NavigationDatumDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.NavigationDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModelFactory;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NavigationDatasetPanelFactory extends
    DatasetPanelFactory<BaseDatasetInstrumentModel<NavigationAdditionalFieldsModel>, NavigationDatasetInstrumentController, NavigationDatasetPanel> {

  private final NavigationDatumDatastore navigationDatumDatastore;

  @Autowired
  public NavigationDatasetPanelFactory(InstrumentDatastore instrumentDatastore, NavigationDatumDatastore navigationDatumDatastore) {
    super(instrumentDatastore);
    this.navigationDatumDatastore = navigationDatumDatastore;
  }

  @Override
  protected BaseDatasetInstrumentModel<NavigationAdditionalFieldsModel> createModel(InstrumentGroupName groupName) {
    BaseDatasetInstrumentModel<NavigationAdditionalFieldsModel> model = new BaseDatasetInstrumentModel<NavigationAdditionalFieldsModel>(groupName.getShortName()) {
    };
    
    model.setAdditionalFieldsModel(new NavigationAdditionalFieldsModel());
    return model;
  }

  @Override
  protected BaseDatasetInstrumentModel<NavigationAdditionalFieldsModel> createModel(InstrumentGroupName groupName, Instrument instrument) {
    BaseDatasetInstrumentModel<NavigationAdditionalFieldsModel> model = createModel(groupName);
//    model.setDataPath(); TODO
    model.setComments(instrument.getDataComment());
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));
    model.setAdditionalFieldsModel(
        AdditionalFieldsModelFactory.navigation(
            instrument.getOtherFields(),
            navigationDatumDatastore.getNavigationDatumDropDowns(),
            NavigationDatumDatastore.UNSELECTED_NAVIGATION_DATUM
        )
    );
    model.setProcessingLevel(instrument.getStatus());
    if (instrument.getReleaseDate() != null) {
      model.setPublicReleaseDate(LocalDate.parse(instrument.getReleaseDate()));
    }
    return model;
  }

  @Override
  protected NavigationDatasetInstrumentController createController(BaseDatasetInstrumentModel<NavigationAdditionalFieldsModel> model) {
    return new NavigationDatasetInstrumentController(model);
  }

  @Override
  protected NavigationDatasetPanel createView(BaseDatasetInstrumentModel<NavigationAdditionalFieldsModel> model, NavigationDatasetInstrumentController controller) {
    return new NavigationDatasetPanel(model, controller, instrumentDatastore, navigationDatumDatastore.getNavigationDatumDropDowns());
  }
}
