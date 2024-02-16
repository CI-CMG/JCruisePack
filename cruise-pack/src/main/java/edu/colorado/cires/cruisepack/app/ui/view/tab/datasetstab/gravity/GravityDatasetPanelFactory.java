package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.gravity;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.GravityDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GravityDatasetPanelFactory extends
    DatasetPanelFactory<GravityDatasetInstrumentModel, GravityDatasetInstrumentController, GravityDatasetPanel> {

  @Autowired
  public GravityDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return GravityDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected GravityDatasetInstrumentModel createModel() {
    return new GravityDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected GravityDatasetInstrumentController createController(GravityDatasetInstrumentModel model) {
    return new GravityDatasetInstrumentController(model);
  }

  @Override
  protected GravityDatasetPanel createView(GravityDatasetInstrumentModel model, GravityDatasetInstrumentController controller) {
    return new GravityDatasetPanel(model, controller, instrumentDatastore);
  }

}
