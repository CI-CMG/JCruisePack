package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.subbottom;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.SubBottomDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.SubBottomDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import java.time.LocalDate;
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
  public String getInstrumentGroupName() {
    return "Sub Bottom";
  }

  @Override
  protected SubBottomDatasetInstrumentModel createModel() {
    return new SubBottomDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected SubBottomDatasetInstrumentModel createModel(Instrument instrument) {
    SubBottomDatasetInstrumentModel model = createModel();
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
  protected SubBottomDatasetInstrumentController createController(SubBottomDatasetInstrumentModel model) {
    return new SubBottomDatasetInstrumentController(model);
  }

  @Override
  protected SubBottomDatasetPanel createView(SubBottomDatasetInstrumentModel model, SubBottomDatasetInstrumentController controller) {
    return new SubBottomDatasetPanel(model, controller, instrumentDatastore);
  }
}
