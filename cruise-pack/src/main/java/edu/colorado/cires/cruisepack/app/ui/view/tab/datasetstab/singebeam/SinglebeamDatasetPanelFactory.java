package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.singebeam;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.SinglebeamDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SinglebeamDatasetPanelFactory extends DatasetPanelFactory<SinglebeamDatasetInstrumentModel, SinglebeamDatasetInstrumentController, SinglebeamDatasetPanel> {


  @Autowired
  public SinglebeamDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return SinglebeamDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected SinglebeamDatasetInstrumentModel createModel() {
    return new SinglebeamDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected SinglebeamDatasetInstrumentController createController(SinglebeamDatasetInstrumentModel model) {
    return new SinglebeamDatasetInstrumentController(model);
  }

  @Override
  protected SinglebeamDatasetPanel createView(SinglebeamDatasetInstrumentModel model, SinglebeamDatasetInstrumentController controller) {
    return new SinglebeamDatasetPanel(model, controller, instrumentDatastore);
  }

}
