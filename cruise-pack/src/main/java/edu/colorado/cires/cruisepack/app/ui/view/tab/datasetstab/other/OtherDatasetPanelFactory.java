package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.other;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.OtherDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.OtherDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OtherDatasetPanelFactory extends DatasetPanelFactory<OtherDatasetInstrumentModel, OtherDatasetInstrumentController, OtherDatasetPanel> {


  @Autowired
  public OtherDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return OtherDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  public String getInstrumentGroupName() {
    return "Other";
  }

  @Override
  protected OtherDatasetInstrumentModel createModel() {
    return new OtherDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected OtherDatasetInstrumentModel createModel(Instrument instrument) {
    OtherDatasetInstrumentModel model = createModel();
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
  protected OtherDatasetInstrumentController createController(OtherDatasetInstrumentModel model) {
    return new OtherDatasetInstrumentController(model);
  }

  @Override
  protected OtherDatasetPanel createView(OtherDatasetInstrumentModel model, OtherDatasetInstrumentController controller) {
    return new OtherDatasetPanel(model, controller, instrumentDatastore);
  }

}
