package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.multibeam;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.MultibeamDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.MultibeamDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MultibeamDatasetPanelFactory extends
    DatasetPanelFactory<MultibeamDatasetInstrumentModel, MultibeamDatasetInstrumentController, MultibeamDatasetPanel> {


  @Autowired
  public MultibeamDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return MultibeamDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected MultibeamDatasetInstrumentModel createModel() {
    return new MultibeamDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected MultibeamDatasetInstrumentController createController(MultibeamDatasetInstrumentModel model) {
    return new MultibeamDatasetInstrumentController(model);
  }

  @Override
  protected MultibeamDatasetPanel createView(MultibeamDatasetInstrumentModel model, MultibeamDatasetInstrumentController controller) {
    return new MultibeamDatasetPanel(model, controller, instrumentDatastore);
  }
}
