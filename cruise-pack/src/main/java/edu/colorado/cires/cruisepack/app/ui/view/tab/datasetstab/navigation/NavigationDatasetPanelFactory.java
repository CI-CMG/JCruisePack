package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.navigation;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.NavigationDatumDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.NavigationDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NavigationDatasetPanelFactory extends
    DatasetPanelFactory<NavigationDatasetInstrumentModel, NavigationDatasetInstrumentController, NavigationDatasetPanel> {

  private final NavigationDatumDatastore navigationDatumDatastore;

  @Autowired
  public NavigationDatasetPanelFactory(InstrumentDatastore instrumentDatastore, NavigationDatumDatastore navigationDatumDatastore) {
    super(instrumentDatastore);
    this.navigationDatumDatastore = navigationDatumDatastore;
  }

  @Override
  protected NavigationDatasetInstrumentModel createModel(String instrumentShortName) {
    return new NavigationDatasetInstrumentModel(NavigationDatasetPanel.INSTRUMENT_SHORT_CODE);
  }

  @Override
  protected NavigationDatasetInstrumentModel createModel(Instrument instrument) {
    NavigationDatasetInstrumentModel model = createModel(instrument.getShortName());
//    model.setDataPath(); TODO
    model.setComments(instrument.getDataComment());
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));
    setValueIfExists(
        "nav_datum",
        instrument.getOtherFields(),
        String.class,
        (v) -> navigationDatumDatastore.getNavigationDatumDropDowns().stream()
            .filter(dd -> dd.getValue().equals(v))
            .findFirst()
            .orElse(NavigationDatumDatastore.UNSELECTED_NAVIGATION_DATUM),
        model::setNavDatum
    );
    model.setProcessingLevel(instrument.getStatus());
    if (instrument.getReleaseDate() != null) {
      model.setPublicReleaseDate(LocalDate.parse(instrument.getReleaseDate()));
    }
    return model;
  }

  @Override
  protected NavigationDatasetInstrumentController createController(NavigationDatasetInstrumentModel model) {
    return new NavigationDatasetInstrumentController(model);
  }

  @Override
  protected NavigationDatasetPanel createView(NavigationDatasetInstrumentModel model, NavigationDatasetInstrumentController controller) {
    return new NavigationDatasetPanel(model, controller, instrumentDatastore, navigationDatumDatastore.getNavigationDatumDropDowns());
  }
}
