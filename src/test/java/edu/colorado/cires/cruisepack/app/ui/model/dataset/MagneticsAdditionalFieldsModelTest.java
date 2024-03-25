package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.cruisepack.app.datastore.MagneticsCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModelTest;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MagneticsAdditionalFieldsModelTest extends PropertyChangeModelTest<MagneticsAdditionalFieldsModel> {

  @Override
  protected MagneticsAdditionalFieldsModel createModel() {
    return new MagneticsAdditionalFieldsModel();
  }

  @Test
  void clearErrors() {
    String correctionModelError = "correction model error";
    String sampleRateError = "sample rate error";
    String towDistanceError = "tow distance error";
    String sensorDepthError = "sensor depth error";
    
    model.setCorrectionModelError(correctionModelError);
    model.setSampleRateError(sampleRateError);
    model.setTowDistanceError(towDistanceError);
    model.setSensorDepthError(sensorDepthError);
    
    clearMap();
    
    model.clearErrors();
    
    assertChangeEvent(MagneticsAdditionalFieldsModel.UPDATE_CORRECTION_MODEL_ERROR, correctionModelError, null);
    assertNull(model.getCorrectionModelError());
    assertChangeEvent(MagneticsAdditionalFieldsModel.UPDATE_SAMPLE_RATE_ERROR, sampleRateError, null);
    assertNull(model.getSampleRateError());
    assertChangeEvent(MagneticsAdditionalFieldsModel.UPDATE_TOW_DISTANCE_ERROR, towDistanceError, null);
    assertNull(model.getTowDistanceError());
    assertChangeEvent(MagneticsAdditionalFieldsModel.UPDATE_SENSOR_DEPTH_ERROR, sensorDepthError, null);
    assertNull(model.getSensorDepthError());
  }

  @Test
  void setError() {
    String correctionModelError = "correction model error";
    String sampleRateError = "sample rate error";
    String towDistanceError = "tow distance error";
    String sensorDepthError = "sensor depth error";
    
    model.setError("correctionModel", correctionModelError);
    model.setError("sampleRate", sampleRateError);
    model.setError("towDistance", towDistanceError);
    model.setError("sensorDepth", sensorDepthError);
    
    assertChangeEvent(MagneticsAdditionalFieldsModel.UPDATE_CORRECTION_MODEL_ERROR, null, correctionModelError);
    assertEquals(correctionModelError, model.getCorrectionModelError());
    assertChangeEvent(MagneticsAdditionalFieldsModel.UPDATE_SAMPLE_RATE_ERROR, null, sampleRateError);
    assertEquals(sampleRateError, model.getSampleRateError());
    assertChangeEvent(MagneticsAdditionalFieldsModel.UPDATE_TOW_DISTANCE_ERROR, null, towDistanceError);
    assertEquals(towDistanceError, model.getTowDistanceError());
    assertChangeEvent(MagneticsAdditionalFieldsModel.UPDATE_SENSOR_DEPTH_ERROR, null, sensorDepthError);
    assertEquals(sensorDepthError, model.getSensorDepthError());
  }

  @Test
  void transform() {
    model.setCorrectionModel(new DropDownItem("1", "value 1"));
    model.setSampleRate("sample rate");
    model.setTowDistance("tow distance");
    model.setSensorDepth("sensor depth");
    
    assertEquals(
        Map.of(
            "correction_model", "value 1",
            "sample_rate", "sample rate",
            "tow_distance", "tow distance",
            "sensor_depth", "sensor depth"
        ),
        model.transform()
    );
  }

  @Test
  void setCorrectionModel() {
    assertPropertyChange(
        MagneticsAdditionalFieldsModel.UPDATE_CORRECTION_MODEL,
        model::getCorrectionModel,
        model::setCorrectionModel,
        new DropDownItem("1", "value 1"),
        new DropDownItem("2", "value 2"),
        MagneticsCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL
    );
  }

  @Test
  void setSampleRate() {
    assertPropertyChange(
        MagneticsAdditionalFieldsModel.UPDATE_SAMPLE_RATE,
        model::getSampleRate,
        model::setSampleRate,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setTowDistance() {
    assertPropertyChange(
        MagneticsAdditionalFieldsModel.UPDATE_TOW_DISTANCE,
        model::getTowDistance,
        model::setTowDistance,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setSensorDepth() {
    assertPropertyChange(
        MagneticsAdditionalFieldsModel.UPDATE_SENSOR_DEPTH,
        model::getSensorDepth,
        model::setSensorDepth,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setCorrectionModelError() {
    assertPropertyChange(
        MagneticsAdditionalFieldsModel.UPDATE_CORRECTION_MODEL_ERROR,
        model::getCorrectionModelError,
        model::setCorrectionModelError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setSampleRateError() {
    assertPropertyChange(
        MagneticsAdditionalFieldsModel.UPDATE_SAMPLE_RATE_ERROR,
        model::getSampleRateError,
        model::setSampleRateError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setTowDistanceError() {
    assertPropertyChange(
        MagneticsAdditionalFieldsModel.UPDATE_TOW_DISTANCE_ERROR,
        model::getTowDistanceError,
        model::setTowDistanceError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setSensorDepthError() {
    assertPropertyChange(
        MagneticsAdditionalFieldsModel.UPDATE_SENSOR_DEPTH,
        model::getSensorDepth,
        model::setSensorDepth,
        "value1",
        "value2",
        null
    );
  }
}