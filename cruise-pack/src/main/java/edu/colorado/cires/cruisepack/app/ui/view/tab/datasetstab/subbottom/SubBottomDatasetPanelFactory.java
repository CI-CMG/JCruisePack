package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.subbottom;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.SubBottomDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.SubBottomDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubBottomDatasetPanelFactory extends
    DatasetPanelFactory<SubBottomDatasetInstrumentModel, SubBottomDatasetInstrumentController, SubBottomDatasetPanel> {


  @Autowired
  public SubBottomDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return SubBottomDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected SubBottomDatasetInstrumentModel createModel() {
    return new SubBottomDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected SubBottomDatasetInstrumentController createController(SubBottomDatasetInstrumentModel model) {
    return new SubBottomDatasetInstrumentController(model);
  }

  @Override
  protected SubBottomDatasetPanel createView(SubBottomDatasetInstrumentModel model, SubBottomDatasetInstrumentController controller) {
    return new SubBottomDatasetPanel(model, controller, instrumentDatastore);
  }
}
