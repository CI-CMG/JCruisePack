package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.magnetics;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.MagneticsCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.MagneticsDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MagneticsDatasetPanelFactory extends
    DatasetPanelFactory<MagneticsDatasetInstrumentModel, MagneticsDatasetInstrumentController, MagneticsDatasetPanel> {

  private final MagneticsCorrectionModelDatastore magneticsCorrectionModelDatastore;

  @Autowired
  public MagneticsDatasetPanelFactory(InstrumentDatastore instrumentDatastore, MagneticsCorrectionModelDatastore magneticsCorrectionModelDatastore) {
    super(instrumentDatastore);
    this.magneticsCorrectionModelDatastore = magneticsCorrectionModelDatastore;
  }

  @Override
  protected MagneticsDatasetInstrumentModel createModel(InstrumentGroupName groupName) {
    return new MagneticsDatasetInstrumentModel(MagneticsDatasetPanel.INSTRUMENT_SHORT_CODE);
  }

  @Override
  protected MagneticsDatasetInstrumentModel createModel(InstrumentGroupName groupName, Instrument instrument) {
    MagneticsDatasetInstrumentModel model = createModel(groupName);
    model.setComments(instrument.getDataComment());
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));
    model.setProcessingLevel(instrument.getStatus());

    Map<String, Object> otherFields = instrument.getOtherFields();
    setValueIfExists(
        "sample_rate",
        otherFields,
        String.class,
        (v) -> v,
        model::setSampleRate
    );
    setValueIfExists(
        "correction_model",
        otherFields,
        String.class,
        (v) -> magneticsCorrectionModelDatastore.getCorrectionModelDropDowns().stream()
            .filter(dd -> dd.getValue().equals(v))
            .findFirst()
            .orElse(MagneticsCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL),
        model::setCorrectionModel
    );
    setValueIfExists(
        "sensor_depth",
        otherFields,
        String.class,
        (v) -> v,
        model::setSensorDepth
    );
    setValueIfExists(
        "tow_distance",
        otherFields,
        String.class,
        (v) -> v,
        model::setTowDistance
    );
//    model.setDataPath(); TODO
    if (instrument.getReleaseDate() != null) {
      model.setPublicReleaseDate(LocalDate.parse(instrument.getReleaseDate()));
    }
    return model;
  }

  @Override
  protected MagneticsDatasetInstrumentController createController(MagneticsDatasetInstrumentModel model) {
    return new MagneticsDatasetInstrumentController(model);
  }

  @Override
  protected MagneticsDatasetPanel createView(MagneticsDatasetInstrumentModel model, MagneticsDatasetInstrumentController controller) {
    return new MagneticsDatasetPanel(model, controller, instrumentDatastore, magneticsCorrectionModelDatastore.getCorrectionModelDropDowns());
  }
}
