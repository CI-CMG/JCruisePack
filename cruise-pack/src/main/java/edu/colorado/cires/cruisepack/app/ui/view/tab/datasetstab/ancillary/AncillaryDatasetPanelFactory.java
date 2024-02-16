package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ancillary;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.AncillaryDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.AncillaryDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AncillaryDatasetPanelFactory extends
    DatasetPanelFactory<AncillaryDatasetInstrumentModel, AncillaryDatasetInstrumentController, AncillaryDatasetPanel> {


  @Autowired
  public AncillaryDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return AncillaryDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected AncillaryDatasetInstrumentModel createModel() {
    return new AncillaryDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected AncillaryDatasetInstrumentController createController(AncillaryDatasetInstrumentModel model) {
    return new AncillaryDatasetInstrumentController(model);
  }

  @Override
  protected AncillaryDatasetPanel createView(AncillaryDatasetInstrumentModel model, AncillaryDatasetInstrumentController controller) {
    return new AncillaryDatasetPanel(model, controller, instrumentDatastore);
  }

}
