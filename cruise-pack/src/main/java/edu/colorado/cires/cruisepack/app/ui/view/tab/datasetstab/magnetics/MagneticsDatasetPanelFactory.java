package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.magnetics;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.MagneticsDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MagneticsDatasetPanelFactory extends
    DatasetPanelFactory<MagneticsDatasetInstrumentModel, MagneticsDatasetInstrumentController, MagneticsDatasetPanel> {

  @Autowired
  public MagneticsDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return MagneticsDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected MagneticsDatasetInstrumentModel createModel() {
    return new MagneticsDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected MagneticsDatasetInstrumentController createController(MagneticsDatasetInstrumentModel model) {
    return new MagneticsDatasetInstrumentController(model);
  }

  @Override
  protected MagneticsDatasetPanel createView(MagneticsDatasetInstrumentModel model, MagneticsDatasetInstrumentController controller) {
    return new MagneticsDatasetPanel(model, controller, instrumentDatastore);
  }
}
