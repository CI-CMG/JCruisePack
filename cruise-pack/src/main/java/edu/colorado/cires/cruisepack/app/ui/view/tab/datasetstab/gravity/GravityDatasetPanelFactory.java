package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.gravity;

import edu.colorado.cires.cruisepack.app.datastore.GravityCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.GravityDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModelFactory;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GravityDatasetPanelFactory extends
    DatasetPanelFactory<BaseDatasetInstrumentModel<GravityAdditionalFieldsModel>, GravityDatasetInstrumentController, GravityDatasetPanel> {

  private final GravityCorrectionModelDatastore gravityCorrectionModelDatastore;

  @Autowired
  public GravityDatasetPanelFactory(InstrumentDatastore instrumentDatastore, GravityCorrectionModelDatastore gravityCorrectionModelDatastore) {
    super(instrumentDatastore);
    this.gravityCorrectionModelDatastore = gravityCorrectionModelDatastore;
  }

  @Override
  protected BaseDatasetInstrumentModel<GravityAdditionalFieldsModel> createModel(InstrumentGroupName groupName) {
    BaseDatasetInstrumentModel<GravityAdditionalFieldsModel> model = new BaseDatasetInstrumentModel<>(groupName.getShortName()) {};
    model.setAdditionalFieldsModel(new GravityAdditionalFieldsModel());
    return model;
  }

  @Override
  protected BaseDatasetInstrumentModel<GravityAdditionalFieldsModel> createModel(InstrumentGroupName groupName, Instrument instrument) {
    BaseDatasetInstrumentModel<GravityAdditionalFieldsModel> model = createModel(groupName);
    model.setComments(instrument.getDataComment());
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));
    model.setAdditionalFieldsModel(
        AdditionalFieldsModelFactory.gravity(instrument.getOtherFields(), gravityCorrectionModelDatastore.getCorrectionModelDropDowns(), GravityCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL)
    );
    model.setProcessingLevel(instrument.getStatus());
//    model.setDataPath(); TODO
    if (instrument.getReleaseDate() != null) {
      model.setPublicReleaseDate(LocalDate.parse(instrument.getReleaseDate()));
    }
    return model;
  }

  @Override
  protected GravityDatasetInstrumentController createController(BaseDatasetInstrumentModel<GravityAdditionalFieldsModel> model) {
    return new GravityDatasetInstrumentController(model);
  }

  @Override
  protected GravityDatasetPanel createView(BaseDatasetInstrumentModel<GravityAdditionalFieldsModel> model, GravityDatasetInstrumentController controller) {
    return new GravityDatasetPanel(model, controller, instrumentDatastore, gravityCorrectionModelDatastore.getCorrectionModelDropDowns());
  }

}
