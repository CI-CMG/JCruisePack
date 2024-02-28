package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.sss;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.SideScanDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.SideScanDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SideScanDatasetPanelFactory extends
    DatasetPanelFactory<SideScanDatasetInstrumentModel, SideScanDatasetInstrumentController, SideScanDatasetPanel> {


  @Autowired
  public SideScanDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return SideScanDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  public String getInstrumentGroupName() {
    return "Side Scan Sonar";
  }

  @Override
  protected SideScanDatasetInstrumentModel createModel() {
    return new SideScanDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected SideScanDatasetInstrumentModel createModel(Instrument instrument) {
    SideScanDatasetInstrumentModel model = createModel();
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
  protected SideScanDatasetInstrumentController createController(SideScanDatasetInstrumentModel model) {
    return new SideScanDatasetInstrumentController(model);
  }

  @Override
  protected SideScanDatasetPanel createView(SideScanDatasetInstrumentModel model, SideScanDatasetInstrumentController controller) {
    return new SideScanDatasetPanel(model, controller, instrumentDatastore);
  }

}
