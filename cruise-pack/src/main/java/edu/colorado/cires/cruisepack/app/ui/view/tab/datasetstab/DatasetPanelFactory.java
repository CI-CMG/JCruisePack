package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.BaseDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class DatasetPanelFactory<M extends BaseDatasetInstrumentModel, C extends BaseDatasetInstrumentController<M>, V extends DatasetPanel<M, C>> {

  protected final InstrumentDatastore instrumentDatastore;

  protected DatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    this.instrumentDatastore = instrumentDatastore;
  }

  public V createPanel(String instrumentShortName) {
    M model = createModel(instrumentShortName);
    return createPanel(model);
  }
  
  public V createPanel(M model) {
    C controller = createController(model);
    controller.init();
    V view = createView(model, controller);
    view.init();
    return view;
  }
  
  protected <JsonType, ModelType> void setValueIfExists(
      String key,
      Map<String, Object> otherFields,
      Class<JsonType> jsonTypeClass,
      Function<JsonType, ModelType> transform,
      Consumer<ModelType> setter
  ) {
    Object value = otherFields.get(key);
    if (value != null && value.getClass().isAssignableFrom(jsonTypeClass)) {
      JsonType v = (JsonType) value;
      setter.accept(transform.apply(v));
    }
  } 

  protected abstract M createModel(String instrumentShortName);
  protected abstract M createModel(Instrument instrument);
  protected abstract C createController(M model);
  protected abstract V createView(M model, C controller);

}
