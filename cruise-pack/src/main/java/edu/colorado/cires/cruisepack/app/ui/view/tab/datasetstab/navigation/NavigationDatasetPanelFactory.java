package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.navigation;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.NavigationDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NavigationDatasetPanelFactory extends
    DatasetPanelFactory<NavigationDatasetInstrumentModel, NavigationDatasetInstrumentController, NavigationDatasetPanel> {


  @Autowired
  public NavigationDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return NavigationDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected NavigationDatasetInstrumentModel createModel() {
    return new NavigationDatasetInstrumentModel();
  }

  @Override
  protected NavigationDatasetInstrumentController createController(NavigationDatasetInstrumentModel model) {
    return new NavigationDatasetInstrumentController(model);
  }

  @Override
  protected NavigationDatasetPanel createView(NavigationDatasetInstrumentModel model, NavigationDatasetInstrumentController controller) {
    return new NavigationDatasetPanel(model, controller, instrumentDatastore);
  }
}
