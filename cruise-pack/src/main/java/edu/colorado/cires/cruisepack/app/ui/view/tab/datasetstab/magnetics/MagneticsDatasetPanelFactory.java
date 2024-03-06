package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.magnetics;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.MagneticsCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.MagneticsDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModelFactory;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MagneticsDatasetPanelFactory extends
    DatasetPanelFactory<BaseDatasetInstrumentModel<MagneticsAdditionalFieldsModel>, MagneticsDatasetInstrumentController, MagneticsDatasetPanel> {

  private final MagneticsCorrectionModelDatastore magneticsCorrectionModelDatastore;

  @Autowired
  public MagneticsDatasetPanelFactory(InstrumentDatastore instrumentDatastore, MagneticsCorrectionModelDatastore magneticsCorrectionModelDatastore) {
    super(instrumentDatastore);
    this.magneticsCorrectionModelDatastore = magneticsCorrectionModelDatastore;
  }

  @Override
  protected BaseDatasetInstrumentModel<MagneticsAdditionalFieldsModel> createModel(InstrumentGroupName groupName) {
    BaseDatasetInstrumentModel<MagneticsAdditionalFieldsModel> model = new BaseDatasetInstrumentModel<>(groupName.getShortName()) {};
    model.setAdditionalFieldsModel(new MagneticsAdditionalFieldsModel());
    return model;
  }

  @Override
  protected BaseDatasetInstrumentModel<MagneticsAdditionalFieldsModel> createModel(InstrumentGroupName groupName, Instrument instrument) {
    BaseDatasetInstrumentModel<MagneticsAdditionalFieldsModel> model = createModel(groupName);
    model.setComments(instrument.getDataComment());
    model.setInstrument(new DropDownItem(instrument.getUuid(), instrument.getShortName()));
    model.setProcessingLevel(instrument.getStatus());
    model.setAdditionalFieldsModel(
        AdditionalFieldsModelFactory.magnetics(
            instrument.getOtherFields(),
            magneticsCorrectionModelDatastore.getCorrectionModelDropDowns(),
            MagneticsCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL
        )
    );
//    model.setDataPath(); TODO
    if (instrument.getReleaseDate() != null) {
      model.setPublicReleaseDate(LocalDate.parse(instrument.getReleaseDate()));
    }
    return model;
  }

  @Override
  protected MagneticsDatasetInstrumentController createController(BaseDatasetInstrumentModel<MagneticsAdditionalFieldsModel> model) {
    return new MagneticsDatasetInstrumentController(model);
  }

  @Override
  protected MagneticsDatasetPanel createView(BaseDatasetInstrumentModel<MagneticsAdditionalFieldsModel> model, MagneticsDatasetInstrumentController controller) {
    return new MagneticsDatasetPanel(model, controller, instrumentDatastore, magneticsCorrectionModelDatastore.getCorrectionModelDropDowns());
  }
}
