package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.singebeam;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SinglebeamVerticalDatumDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.SinglebeamDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModelFactory;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.AdditionalFieldsPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class SinglebeamDatasetPanelFactory extends DatasetPanelFactory<SinglebeamAdditionalFieldsModel, SinglebeamDatasetInstrumentController> {

  private final SinglebeamVerticalDatumDatastore singlebeamVerticalDatumDatastore;

  @Autowired
  public SinglebeamDatasetPanelFactory(InstrumentDatastore instrumentDatastore, SinglebeamVerticalDatumDatastore singlebeamVerticalDatumDatastore) {
    super(instrumentDatastore);
    this.singlebeamVerticalDatumDatastore = singlebeamVerticalDatumDatastore;
  }


  @Override
  protected SinglebeamAdditionalFieldsModel createAdditionalFieldsModel() {
    return new SinglebeamAdditionalFieldsModel();
  }

  @Override
  protected SinglebeamAdditionalFieldsModel createAdditionalFieldsModel(Instrument instrument) {
    return AdditionalFieldsModelFactory.singlebeam(
        instrument.getOtherFields(),
        singlebeamVerticalDatumDatastore.getVerticalDatumDropDowns()
    );
  }

  @Override
  protected AdditionalFieldsPanel<SinglebeamAdditionalFieldsModel, SinglebeamDatasetInstrumentController> createAdditionalFieldsView(
      BaseDatasetInstrumentModel<SinglebeamAdditionalFieldsModel> model, SinglebeamDatasetInstrumentController controller) {
    return new SinglebeamDatasetPanel(model.getAdditionalFieldsModel(), controller, singlebeamVerticalDatumDatastore.getVerticalDatumDropDowns());
  }

  @Override
  protected SinglebeamDatasetInstrumentController createController(BaseDatasetInstrumentModel<SinglebeamAdditionalFieldsModel> model) {
    return new SinglebeamDatasetInstrumentController(model);
  }
}
