package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.other;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.OtherDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.OtherDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OtherDatasetPanelFactory extends DatasetPanelFactory<OtherDatasetInstrumentModel, OtherDatasetInstrumentController, OtherDatasetPanel> {


  @Autowired
  public OtherDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return OtherDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected OtherDatasetInstrumentModel createModel() {
    return new OtherDatasetInstrumentModel();
  }

  @Override
  protected OtherDatasetInstrumentController createController(OtherDatasetInstrumentModel model) {
    return new OtherDatasetInstrumentController(model);
  }

  @Override
  protected OtherDatasetPanel createView(OtherDatasetInstrumentModel model, OtherDatasetInstrumentController controller) {
    return new OtherDatasetPanel(model, controller, instrumentDatastore);
  }

}
