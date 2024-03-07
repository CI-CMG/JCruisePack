package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.BaseDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.time.LocalDate;

public abstract class DatasetPanelFactory<T extends AdditionalFieldsModel, C extends BaseDatasetInstrumentController<T>> {

  protected final InstrumentDatastore instrumentDatastore;

  protected DatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    this.instrumentDatastore = instrumentDatastore;
  }

  public DatasetPanel<T, C> createPanel(InstrumentGroupName groupName) {
    BaseDatasetInstrumentModel<T> model = createModel(groupName);
    return createPanel(model);
  }
  
  public DatasetPanel<T, C> createPanel(BaseDatasetInstrumentModel<T> model) {
    C controller = createController(model);
    controller.init();
    DatasetPanel<T, C> view = createView(model, controller);
    view.init();
    return view;
  }

  protected BaseDatasetInstrumentModel<T> createModel(InstrumentGroupName groupName) {
    BaseDatasetInstrumentModel<T> model = new BaseDatasetInstrumentModel<>(groupName.getShortName()) {
    };
    model.setAdditionalFieldsModel(
        createAdditionalFieldsModel()
    );
    return model;
  }
  
  protected BaseDatasetInstrumentModel<T> createModel(InstrumentGroupName groupName, Instrument instrument) {
    BaseDatasetInstrumentModel<T> model = createModel(groupName);

    //    model.setDataPath(); TODO
    model.setComments(instrument.getDataComment());
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));
//    model.setAncillaryPath(); TODO
    model.setAncillaryDetails(instrument.getAncillaryDataDetails());
    model.setProcessingLevel(instrument.getStatus());
    if (instrument.getReleaseDate() != null) {
      model.setPublicReleaseDate(LocalDate.parse(instrument.getReleaseDate()));
    }
    model.setAdditionalFieldsModel(
        createAdditionalFieldsModel(instrument)
    );
    
    return model;
  }

  protected DatasetPanel<T, C> createView(BaseDatasetInstrumentModel<T> model, C controller) {
    return new DatasetPanel<>(model, controller, instrumentDatastore) {
      @Override
      protected AdditionalFieldsPanel<T, C> createAndInitializeContentPanel() {
        AdditionalFieldsPanel<T, C> panel = createAdditionalFieldsView(model, controller);
        if (panel != null) {
          panel.init();
        }
        return panel;
      }
    };
  }
  
  protected abstract T createAdditionalFieldsModel();
  protected abstract T createAdditionalFieldsModel(Instrument instrument);
  protected abstract AdditionalFieldsPanel<T, C> createAdditionalFieldsView(BaseDatasetInstrumentModel<T> model, C controller);
  protected abstract C createController(BaseDatasetInstrumentModel<T> model);

}
