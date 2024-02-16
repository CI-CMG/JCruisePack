package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.xbt;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.XbtDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.XbtDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class XbtDatasetPanelFactory extends DatasetPanelFactory<XbtDatasetInstrumentModel, XbtDatasetInstrumentController, XbtDatasetPanel> {


  @Autowired
  public XbtDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return XbtDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected XbtDatasetInstrumentModel createModel() {
    return new XbtDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected XbtDatasetInstrumentController createController(XbtDatasetInstrumentModel model) {
    return new XbtDatasetInstrumentController(model);
  }

  @Override
  protected XbtDatasetPanel createView(XbtDatasetInstrumentModel model, XbtDatasetInstrumentController controller) {
    return new XbtDatasetPanel(model, controller, instrumentDatastore);
  }

}
