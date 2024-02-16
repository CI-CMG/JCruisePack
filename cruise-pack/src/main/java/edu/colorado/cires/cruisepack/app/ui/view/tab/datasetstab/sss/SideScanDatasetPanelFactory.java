package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.sss;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.SideScanDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.SideScanDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SideScanDatasetPanelFactory extends
    DatasetPanelFactory<SideScanDatasetInstrumentModel, SideScanDatasetInstrumentController, SideScanDatasetPanel> {


  @Autowired
  public SideScanDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return SideScanDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected SideScanDatasetInstrumentModel createModel() {
    return new SideScanDatasetInstrumentModel();
  }

  @Override
  protected SideScanDatasetInstrumentController createController(SideScanDatasetInstrumentModel model) {
    return new SideScanDatasetInstrumentController(model);
  }

  @Override
  protected SideScanDatasetPanel createView(SideScanDatasetInstrumentModel model, SideScanDatasetInstrumentController controller) {
    return new SideScanDatasetPanel(model, controller, instrumentDatastore);
  }

}
