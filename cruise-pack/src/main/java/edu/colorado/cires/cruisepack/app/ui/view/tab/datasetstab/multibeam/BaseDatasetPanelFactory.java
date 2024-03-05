package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.multibeam;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.BaseDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseDatasetPanelFactory extends
    DatasetPanelFactory<BaseDatasetInstrumentModel, BaseDatasetInstrumentController<BaseDatasetInstrumentModel>, BaseDatasetPanel> {


  @Autowired
  public BaseDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  protected BaseDatasetInstrumentModel createModel(InstrumentGroupName groupName) {
    return new BaseDatasetInstrumentModel(groupName.getShortName()) {};
  }

  @Override
  protected BaseDatasetInstrumentModel createModel(InstrumentGroupName groupName, Instrument instrument) {
    BaseDatasetInstrumentModel model = createModel(groupName);
//    model.setDataPath(); TODO
    model.setComments(instrument.getDataComment());
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));
    model.setProcessingLevel(instrument.getStatus());
    
    if (instrument.getReleaseDate() != null) {
      model.setPublicReleaseDate(LocalDate.parse(instrument.getReleaseDate()));
    }
    return model;
  }

  @Override
  protected BaseDatasetInstrumentController<BaseDatasetInstrumentModel> createController(BaseDatasetInstrumentModel model) {
    return new BaseDatasetInstrumentController<>(model) {};
  }

  @Override
  protected BaseDatasetPanel createView(BaseDatasetInstrumentModel model, BaseDatasetInstrumentController<BaseDatasetInstrumentModel> controller) {
    return new BaseDatasetPanel(model, controller, instrumentDatastore);
  }
}
