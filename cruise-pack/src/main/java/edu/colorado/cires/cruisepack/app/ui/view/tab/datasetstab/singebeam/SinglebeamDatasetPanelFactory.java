package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.singebeam;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SinglebeamVerticalDatumDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.SinglebeamDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SinglebeamDatasetPanelFactory extends DatasetPanelFactory<SinglebeamDatasetInstrumentModel, SinglebeamDatasetInstrumentController, SinglebeamDatasetPanel> {

  private final SinglebeamVerticalDatumDatastore singlebeamVerticalDatumDatastore;

  @Autowired
  public SinglebeamDatasetPanelFactory(InstrumentDatastore instrumentDatastore, SinglebeamVerticalDatumDatastore singlebeamVerticalDatumDatastore) {
    super(instrumentDatastore);
    this.singlebeamVerticalDatumDatastore = singlebeamVerticalDatumDatastore;
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return SinglebeamDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  public String getInstrumentGroupName() {
    return "Singlebeam Bathymetry";
  }

  @Override
  protected SinglebeamDatasetInstrumentModel createModel() {
    return new SinglebeamDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected SinglebeamDatasetInstrumentModel createModel(Instrument instrument) {
    SinglebeamDatasetInstrumentModel model = createModel();
//    model.setDataPath(); TODO
    model.setComments(instrument.getDataComment());
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));
//    model.setObsRate(); TODO
    model.setProcessingLevel(instrument.getStatus());
//    model.setSoundVelocity(); TODO
//    model.setVerticalDatum(); TODO
    if (instrument.getReleaseDate() != null) {
      model.setPublicReleaseDate(LocalDate.parse(instrument.getReleaseDate()));
    }
    return model;
  }

  @Override
  protected SinglebeamDatasetInstrumentController createController(SinglebeamDatasetInstrumentModel model) {
    return new SinglebeamDatasetInstrumentController(model);
  }

  @Override
  protected SinglebeamDatasetPanel createView(SinglebeamDatasetInstrumentModel model, SinglebeamDatasetInstrumentController controller) {
    return new SinglebeamDatasetPanel(model, controller, instrumentDatastore, singlebeamVerticalDatumDatastore.getVerticalDatumDropDowns());
  }

}
