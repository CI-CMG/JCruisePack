package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.gravity;

import edu.colorado.cires.cruisepack.app.datastore.GravityCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.GravityDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import java.time.LocalDate;
import java.util.Map;
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
  protected GravityDatasetInstrumentModel createModel(InstrumentGroupName groupName) {
    return new GravityDatasetInstrumentModel(GravityDatasetPanel.INSTRUMENT_SHORT_CODE);
  }

  @Override
  protected GravityDatasetInstrumentModel createModel(InstrumentGroupName groupName, Instrument instrument) {
    GravityDatasetInstrumentModel model = createModel(groupName);
    model.setComments(instrument.getDataComment());
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));

    Map<String, Object> otherFields = instrument.getOtherFields();
    setValueIfExists(
        "correction_model",
        otherFields,
        String.class,
        (v) -> gravityCorrectionModelDatastore.getCorrectionModelDropDowns().stream()
            .filter(dd -> dd.getValue().equals(v))
            .findFirst()
            .orElse(GravityCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL),
        model::setCorrectionModel
    );
    setValueIfExists(
        "arrival_tie",
        otherFields,
        String.class,
        (v) -> v,
        model::setArrivalTie
    );
    setValueIfExists(
        "departure_tie",
        otherFields,
        String.class,
        (v) -> v,
        model::setDepartureTie
    );
    setValueIfExists(
        "drift_per_day",
        otherFields,
        String.class,
        (v) -> v,
        model::setDriftPerDay
    );
    setValueIfExists(
        "observation_rate",
        otherFields,
        String.class,
        (v) -> v,
        model::setObservationRate
    );
    
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
