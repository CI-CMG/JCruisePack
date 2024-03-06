package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.singebeam;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SinglebeamVerticalDatumDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.SinglebeamDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModelFactory;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SinglebeamDatasetPanelFactory extends DatasetPanelFactory<BaseDatasetInstrumentModel<SinglebeamAdditionalFieldsModel>, SinglebeamDatasetInstrumentController, SinglebeamDatasetPanel> {

  private final SinglebeamVerticalDatumDatastore singlebeamVerticalDatumDatastore;

  @Autowired
  public SinglebeamDatasetPanelFactory(InstrumentDatastore instrumentDatastore, SinglebeamVerticalDatumDatastore singlebeamVerticalDatumDatastore) {
    super(instrumentDatastore);
    this.singlebeamVerticalDatumDatastore = singlebeamVerticalDatumDatastore;
  }

  @Override
  protected BaseDatasetInstrumentModel<SinglebeamAdditionalFieldsModel> createModel(InstrumentGroupName groupName) {
    BaseDatasetInstrumentModel<SinglebeamAdditionalFieldsModel> model = new BaseDatasetInstrumentModel<>(groupName.getShortName()) {};
    model.setAdditionalFieldsModel(new SinglebeamAdditionalFieldsModel());
    return model;
  }

  @Override
  protected BaseDatasetInstrumentModel<SinglebeamAdditionalFieldsModel> createModel(InstrumentGroupName groupName, Instrument instrument) {
    BaseDatasetInstrumentModel<SinglebeamAdditionalFieldsModel> model = createModel(groupName);
//    model.setDataPath(); TODO
    model.setComments(instrument.getDataComment());
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));
    model.setAdditionalFieldsModel(
        AdditionalFieldsModelFactory.singlebeam(
            instrument.getOtherFields(),
            singlebeamVerticalDatumDatastore.getVerticalDatumDropDowns(),
            SinglebeamVerticalDatumDatastore.UNSELECTED_VERTICAL_DATUM
        )
    );
    model.setProcessingLevel(instrument.getStatus());
    if (instrument.getReleaseDate() != null) {
      model.setPublicReleaseDate(LocalDate.parse(instrument.getReleaseDate()));
    }
    return model;
  }

  @Override
  protected SinglebeamDatasetInstrumentController createController(BaseDatasetInstrumentModel<SinglebeamAdditionalFieldsModel> model) {
    return new SinglebeamDatasetInstrumentController(model);
  }

  @Override
  protected SinglebeamDatasetPanel createView(BaseDatasetInstrumentModel<SinglebeamAdditionalFieldsModel> model, SinglebeamDatasetInstrumentController controller) {
    return new SinglebeamDatasetPanel(model, controller, instrumentDatastore, singlebeamVerticalDatumDatastore.getVerticalDatumDropDowns());
  }

}
