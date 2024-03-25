package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.cruisepack.app.datastore.SinglebeamVerticalDatumDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModelTest;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SinglebeamAdditionalFieldsModelTest extends PropertyChangeModelTest<SinglebeamAdditionalFieldsModel> {

  @Override
  protected SinglebeamAdditionalFieldsModel createModel() {
    return new SinglebeamAdditionalFieldsModel();
  }

  @Test
  void clearErrors() {
    String verticalDatumError = "vertical datum error";
    String obsRateError = "obs rate error";
    String soundVelocityError = "sound velocity error";
    
    model.setVerticalDatumError(verticalDatumError);
    model.setObsRateError(obsRateError);
    model.setSoundVelocityError(soundVelocityError);
    
    clearMap();
    
    model.clearErrors();
    
    assertChangeEvent(SinglebeamAdditionalFieldsModel.UPDATE_VERTICAL_DATUM_ERROR, verticalDatumError, null);
    assertNull(model.getVerticalDatumError());
    assertChangeEvent(SinglebeamAdditionalFieldsModel.UPDATE_OBS_RATE_ERROR, obsRateError, null);
    assertNull(model.getObsRateError());
    assertChangeEvent(SinglebeamAdditionalFieldsModel.UPDATE_SOUND_VELOCITY_ERROR, soundVelocityError, null);
    assertNull(model.getSoundVelocityError());
  }

  @Test
  void setError() {
    String verticalDatumError = "vertical datum error";
    String obsRateError = "obs rate error";
    String soundVelocityError = "sound velocity error";
    
    model.setError("verticalDatum", verticalDatumError);
    model.setError("obsRate", obsRateError);
    model.setError("soundVelocity", soundVelocityError);

    assertChangeEvent(SinglebeamAdditionalFieldsModel.UPDATE_VERTICAL_DATUM_ERROR, null, verticalDatumError);
    assertEquals(verticalDatumError, model.getVerticalDatumError());
    assertChangeEvent(SinglebeamAdditionalFieldsModel.UPDATE_OBS_RATE_ERROR, null, obsRateError);
    assertEquals(obsRateError, model.getObsRateError());
    assertChangeEvent(SinglebeamAdditionalFieldsModel.UPDATE_SOUND_VELOCITY_ERROR, null, soundVelocityError);
    assertEquals(soundVelocityError, model.getSoundVelocityError());
  }

  @Test
  void transform() {
    model.setVerticalDatum(new DropDownItem("1", "value 1"));
    model.setObsRate("obs rate");
    model.setSoundVelocity("sound velocity");
    
    assertEquals(
        Map.of(
            "vertical_datum", "value 1",
            "obs_rate", "obs rate",
            "sound_velocity", "sound velocity"
        ),
        model.transform()
    );
  }

  @Test
  void setVerticalDatum() {
    assertPropertyChange(
        SinglebeamAdditionalFieldsModel.UPDATE_VERTICAL_DATUM,
        model::getVerticalDatum,
        model::setVerticalDatum,
        new DropDownItem("1", "value 1"),
        new DropDownItem("2", "value 2"),
        SinglebeamVerticalDatumDatastore.UNSELECTED_VERTICAL_DATUM
    );
  }

  @Test
  void setObsRate() {
    assertPropertyChange(
        SinglebeamAdditionalFieldsModel.UPDATE_OBS_RATE,
        model::getObsRate,
        model::setObsRate,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setSoundVelocity() {
    assertPropertyChange(
        SinglebeamAdditionalFieldsModel.UPDATE_SOUND_VELOCITY,
        model::getSoundVelocity,
        model::setSoundVelocity,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setVerticalDatumError() {
    assertPropertyChange(
        SinglebeamAdditionalFieldsModel.UPDATE_VERTICAL_DATUM_ERROR,
        model::getVerticalDatumError,
        model::setVerticalDatumError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setObsRateError() {
    assertPropertyChange(
        SinglebeamAdditionalFieldsModel.UPDATE_OBS_RATE_ERROR,
        model::getObsRateError,
        model::setObsRateError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setSoundVelocityError() {
    assertPropertyChange(
        SinglebeamAdditionalFieldsModel.UPDATE_SOUND_VELOCITY_ERROR,
        model::getSoundVelocityError,
        model::setSoundVelocityError,
        "value1",
        "value2",
        null
    );
  }
}