package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.xbt;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.XbtDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.XbtDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class XbtDatasetPanelFactory extends DatasetPanelFactory<XbtDatasetInstrumentModel, XbtDatasetInstrumentController, XbtDatasetPanel> {


  @Autowired
  public XbtDatasetPanelFactory(InstrumentDatastore instrumentDatastore) {
    super(instrumentDatastore);
  }

  @Override
  public String getInstrumentGroupShortCode() {
    return XbtDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  public String getInstrumentGroupName() {
    return XbtDatasetPanel.INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected XbtDatasetInstrumentModel createModel() {
    return new XbtDatasetInstrumentModel(getInstrumentGroupShortCode());
  }

  @Override
  protected XbtDatasetInstrumentModel createModel(Instrument instrument) {
    XbtDatasetInstrumentModel model = createModel();
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
  protected XbtDatasetInstrumentController createController(XbtDatasetInstrumentModel model) {
    return new XbtDatasetInstrumentController(model);
  }

  @Override
  protected XbtDatasetPanel createView(XbtDatasetInstrumentModel model, XbtDatasetInstrumentController controller) {
    return new XbtDatasetPanel(model, controller, instrumentDatastore);
  }

}
