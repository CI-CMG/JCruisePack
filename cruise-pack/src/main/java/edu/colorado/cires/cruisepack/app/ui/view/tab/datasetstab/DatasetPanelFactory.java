package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.BaseDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;

public abstract class DatasetPanelFactory<M extends BaseDatasetInstrumentModel, C extends BaseDatasetInstrumentController<M>, V extends DatasetPanel<M, C>> {

  protected final InstrumentDatastore instrumentDatastore;

  protected DatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    this.instrumentDatastore = instrumentDatastore;
  }

  public V createPanel() {
    M model = createModel();
    C controller = createController(model);
    controller.init();
    V view = createView(model, controller);
    view.init();
    return view;
  }

  public abstract String getInstrumentGroupShortCode();
  protected abstract M createModel();
  protected abstract C createController(M model);
  protected abstract V createView(M model, C controller);

}
