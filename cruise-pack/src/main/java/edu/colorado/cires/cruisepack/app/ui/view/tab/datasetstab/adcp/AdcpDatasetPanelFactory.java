package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.adcp;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.AdcpDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.AdcpDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdcpDatasetPanelFactory extends DatasetPanelFactory<AdcpDatasetInstrumentModel, AdcpDatasetInstrumentController, AdcpDatasetPanel> {


  @Autowired
  public AdcpDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return AdcpDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  public String getInstrumentGroupName() {
    return AdcpDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected AdcpDatasetInstrumentModel createModel() {
    return new AdcpDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected AdcpDatasetInstrumentModel createModel(Instrument instrument) {
    AdcpDatasetInstrumentModel model = createModel();
    model.setComments(instrument.getDataComment());
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));
//    model.setDataPath(); TODO
    model.setProcessingLevel(instrument.getStatus());
    if (instrument.getReleaseDate() != null) {
      model.setPublicReleaseDate(LocalDate.parse(instrument.getReleaseDate()));
    }
    return model;
  }

  @Override
  protected AdcpDatasetInstrumentController createController(AdcpDatasetInstrumentModel model) {
    return new AdcpDatasetInstrumentController(model);
  }

  @Override
  protected AdcpDatasetPanel createView(AdcpDatasetInstrumentModel model, AdcpDatasetInstrumentController controller) {
    return new AdcpDatasetPanel(model, controller, instrumentDatastore);
  }

}
