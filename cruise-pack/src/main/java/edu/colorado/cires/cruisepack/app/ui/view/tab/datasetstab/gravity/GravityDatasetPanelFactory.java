package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.gravity;

import edu.colorado.cires.cruisepack.app.datastore.GravityCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.GravityDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GravityDatasetPanelFactory extends
    DatasetPanelFactory<GravityDatasetInstrumentModel, GravityDatasetInstrumentController, GravityDatasetPanel> {

  private final GravityCorrectionModelDatastore gravityCorrectionModelDatastore;

  @Autowired
  public GravityDatasetPanelFactory(InstrumentDatastore instrumentDatastore, GravityCorrectionModelDatastore gravityCorrectionModelDatastore) {
    super(instrumentDatastore);
    this.gravityCorrectionModelDatastore = gravityCorrectionModelDatastore;
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return GravityDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  public String getInstrumentGroupName() {
    return "Gravity";
  }

  @Override
  protected GravityDatasetInstrumentModel createModel() {
    return new GravityDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected GravityDatasetInstrumentModel createModel(Instrument instrument) {
    GravityDatasetInstrumentModel model = createModel();
//    model.setCorrectionModel(); TODO
    model.setComments(instrument.getDataComment());
//    model.setArrivalTie(); TODO
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));
//    model.setDepartureTie(); TODO
//    model.setDriftPerDay(); TODO
//    model.setObservationRate(); TODO
    model.setProcessingLevel(instrument.getStatus());
//    model.setDataPath(); TODO
    if (instrument.getReleaseDate() != null) {
      model.setPublicReleaseDate(LocalDate.parse(instrument.getReleaseDate()));
    }
    return model;
  }

  @Override
  protected GravityDatasetInstrumentController createController(GravityDatasetInstrumentModel model) {
    return new GravityDatasetInstrumentController(model);
  }

  @Override
  protected GravityDatasetPanel createView(GravityDatasetInstrumentModel model, GravityDatasetInstrumentController controller) {
    return new GravityDatasetPanel(model, controller, instrumentDatastore, gravityCorrectionModelDatastore.getCorrectionModelDropDowns());
  }

}
