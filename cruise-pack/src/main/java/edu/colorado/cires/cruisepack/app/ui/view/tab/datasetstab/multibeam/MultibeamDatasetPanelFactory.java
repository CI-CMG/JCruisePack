package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.multibeam;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.MultibeamDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.MultibeamDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MultibeamDatasetPanelFactory extends
    DatasetPanelFactory<MultibeamDatasetInstrumentModel, MultibeamDatasetInstrumentController, MultibeamDatasetPanel> {


  @Autowired
  public MultibeamDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return MultibeamDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  public String getInstrumentGroupName() {
    return "Multibeam Bathymetry";
  }

  @Override
  protected MultibeamDatasetInstrumentModel createModel() {
    return new MultibeamDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected MultibeamDatasetInstrumentModel createModel(Instrument instrument) {
    MultibeamDatasetInstrumentModel model = createModel();
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
  protected MultibeamDatasetInstrumentController createController(MultibeamDatasetInstrumentModel model) {
    return new MultibeamDatasetInstrumentController(model);
  }

  @Override
  protected MultibeamDatasetPanel createView(MultibeamDatasetInstrumentModel model, MultibeamDatasetInstrumentController controller) {
    return new MultibeamDatasetPanel(model, controller, instrumentDatastore);
  }
}
