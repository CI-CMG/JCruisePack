package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.wcsd;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.WaterColumnSonarDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaterColumnSonarDatasetPanelFactory extends
    DatasetPanelFactory<WaterColumnSonarDatasetInstrumentModel, WaterColumnSonarDatasetInstrumentController, WaterColumnSonarDatasetPanel> {


  @Autowired
  public WaterColumnSonarDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return WaterColumnSonarDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected WaterColumnSonarDatasetInstrumentModel createModel() {
    return new WaterColumnSonarDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected WaterColumnSonarDatasetInstrumentController createController(WaterColumnSonarDatasetInstrumentModel model) {
    return new WaterColumnSonarDatasetInstrumentController(model);
  }

  @Override
  protected WaterColumnSonarDatasetPanel createView(WaterColumnSonarDatasetInstrumentModel model,
      WaterColumnSonarDatasetInstrumentController controller) {
    return new WaterColumnSonarDatasetPanel(model, controller, instrumentDatastore);
  }

}
