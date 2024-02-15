package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ctd;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.CtdDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.CtdDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CtdDatasetPanelFactory extends DatasetPanelFactory<CtdDatasetInstrumentModel, CtdDatasetInstrumentController, CtdDatasetPanel> {


  @Autowired
  public CtdDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return CtdDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected CtdDatasetInstrumentModel createModel() {
    return new CtdDatasetInstrumentModel();
  }

  @Override
  protected CtdDatasetInstrumentController createController(CtdDatasetInstrumentModel model) {
    return new CtdDatasetInstrumentController(model);
  }

  @Override
  protected CtdDatasetPanel createView(CtdDatasetInstrumentModel model, CtdDatasetInstrumentController controller) {
    return new CtdDatasetPanel(model, controller, instrumentDatastore);
  }

}
