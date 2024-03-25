package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.cruisepack.app.datastore.GravityCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModelTest;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.util.Map;
import org.junit.jupiter.api.Test;

class GravityAdditionalFieldsModelTest extends PropertyChangeModelTest<GravityAdditionalFieldsModel> {

  @Override
  protected GravityAdditionalFieldsModel createModel() {
    return new GravityAdditionalFieldsModel();
  }

  @Test
  void clearErrors() {
    model.setCorrectionModelError("correction model error");
    model.setObservationRateError("observation rate error");
    model.setDepartureTieError("departure tie error");
    model.setArrivalTieError("arrival tie error");
    model.setDriftPerDayError("drift per day error");
    
    clearEvents();
    model.clearErrors();
    
    assertChangeEvent(GravityAdditionalFieldsModel.UPDATE_CORRECTION_MODEL_ERROR, "correction model error", null);
    assertNull(model.getCorrectionModelError());
    assertChangeEvent(GravityAdditionalFieldsModel.UPDATE_OBSERVATION_RATE_ERROR, "observation rate error", null);
    assertNull(model.getObservationRateError());
    assertChangeEvent(GravityAdditionalFieldsModel.UPDATE_DEPARTURE_TIE_ERROR, "departure tie error", null);
    assertNull(model.getDepartureTieError());
    assertChangeEvent(GravityAdditionalFieldsModel.UPDATE_ARRIVAL_TIE_ERROR, "arrival tie error", null);
    assertNull(model.getArrivalTieError());
    assertChangeEvent(GravityAdditionalFieldsModel.UPDATE_DRIFT_PER_DAY_ERROR, "drift per day error", null);
    assertNull(model.getDriftPerDayError());
  }

  @Test
  void setError() {
    model.setError("correctionModel", "correction model error");
    model.setError("observationRate", "observation rate error");
    model.setError("departureTie", "departure tie error");
    model.setError("arrivalTie", "arrival tie error");
    model.setError("driftPerDay", "drift per day error");
    
    assertChangeEvent(GravityAdditionalFieldsModel.UPDATE_CORRECTION_MODEL_ERROR, null, "correction model error");
    assertEquals("correction model error", model.getCorrectionModelError());
    assertChangeEvent(GravityAdditionalFieldsModel.UPDATE_OBSERVATION_RATE_ERROR, null, "observation rate error");
    assertEquals("observation rate error", model.getObservationRateError());
    assertChangeEvent(GravityAdditionalFieldsModel.UPDATE_DEPARTURE_TIE_ERROR, null, "departure tie error");
    assertEquals("departure tie error", model.getDepartureTieError());
    assertChangeEvent(GravityAdditionalFieldsModel.UPDATE_ARRIVAL_TIE_ERROR, null, "arrival tie error");
    assertEquals("arrival tie error", model.getArrivalTieError());
    assertChangeEvent(GravityAdditionalFieldsModel.UPDATE_DRIFT_PER_DAY_ERROR, null, "drift per day error");
    assertEquals("drift per day error", model.getDriftPerDayError());
  }

  @Test
  void transform() {
    model.setCorrectionModel(new DropDownItem("1", "value 1"));
    model.setObservationRate("observation rate");
    model.setDepartureTie("departure tie");
    model.setArrivalTie("arrival tie");
    model.setDriftPerDay("drift per day");
    
    assertEquals(
        Map.of(
            "correction_model", "value 1",
            "observation_rate", "observation rate",
            "arrival_tie", "arrival tie",
            "departure_tie", "departure tie",
            "drift_per_day", "drift per day"
        ),
        model.transform()
    );
  }

  @Test
  void setCorrectionModel() {
    assertPropertyChange(
        GravityAdditionalFieldsModel.UPDATE_CORRECTION_MODEL,
        model::getCorrectionModel,
        model::setCorrectionModel,
        new DropDownItem("1", "value 1"),
        new DropDownItem("2", "value 2"),
        GravityCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL
    );
  }

  @Test
  void setObservationRate() {
    assertPropertyChange(
        GravityAdditionalFieldsModel.UPDATE_OBSERVATION_RATE,
        model::getObservationRate,
        model::setObservationRate,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setDepartureTie() {
    assertPropertyChange(
        GravityAdditionalFieldsModel.UPDATE_DEPARTURE_TIE,
        model::getDepartureTie,
        model::setDepartureTie,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setArrivalTie() {
    assertPropertyChange(
        GravityAdditionalFieldsModel.UPDATE_ARRIVAL_TIE,
        model::getArrivalTie,
        model::setArrivalTie,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setDriftPerDay() {
    assertPropertyChange(
        GravityAdditionalFieldsModel.UPDATE_DRIFT_PER_DAY,
        model::getDriftPerDay,
        model::setDriftPerDay,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setCorrectionModelError() {
    assertPropertyChange(
        GravityAdditionalFieldsModel.UPDATE_CORRECTION_MODEL_ERROR,
        model::getCorrectionModelError,
        model::setCorrectionModelError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setObservationRateError() {
    assertPropertyChange(
        GravityAdditionalFieldsModel.UPDATE_OBSERVATION_RATE_ERROR,
        model::getObservationRateError,
        model::setObservationRateError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setDepartureTieError() {
    assertPropertyChange(
        GravityAdditionalFieldsModel.UPDATE_DEPARTURE_TIE_ERROR,
        model::getDepartureTieError,
        model::setDepartureTieError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setArrivalTieError() {
    assertPropertyChange(
        GravityAdditionalFieldsModel.UPDATE_ARRIVAL_TIE_ERROR, 
        model::getArrivalTieError,
        model::setArrivalTieError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void setDriftPerDayError() {
    assertPropertyChange(
        GravityAdditionalFieldsModel.UPDATE_DRIFT_PER_DAY_ERROR, 
        model::getDriftPerDayError,
        model::setDriftPerDayError,
        "value1",
        "value2",
        null
    );
  }
}