package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.magnetics;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.MagneticsCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.MagneticsDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import java.time.LocalDate;
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
  public String getInstrumentGroupShortCode() {
    return MagneticsDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  public String getInstrumentGroupName() {
    return "Magnetics";
  }

  @Override
  protected MagneticsDatasetInstrumentModel createModel() {
    return new MagneticsDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected MagneticsDatasetInstrumentModel createModel(Instrument instrument) {
    MagneticsDatasetInstrumentModel model = createModel();
//    model.setCorrectionModel(); TODO
    model.setComments(instrument.getDataComment());
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));
//    model.setSampleRate(); TODO
    model.setProcessingLevel(instrument.getStatus());
//    model.setSensorDepth(); TODO
//    model.setTowDistance(); TODO
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
