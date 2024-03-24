package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.datastore.GravityCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.datastore.MagneticsCorrectionModelDatastore;
import edu.colorado.cires.cruisepack.app.datastore.NavigationDatumDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SinglebeamVerticalDatumDatastore;
import edu.colorado.cires.cruisepack.app.datastore.WaterColumnCalibrationStateDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class AdditionalFieldsModelFactory {
  
  public static WaterColumnAdditionalFieldsModel waterColumn(Map<String, Object> otherFields, List<DropDownItem> items) {
    WaterColumnAdditionalFieldsModel model = new WaterColumnAdditionalFieldsModel();
    
    setValueIfExists(
        "calibration_state",
        otherFields,
        String.class,
        (v) -> items.stream().filter(dd -> dd.getValue().equals(v)).findFirst().orElse(WaterColumnCalibrationStateDatastore.UNSELECTED_CALIBRATION_STATE),
        model::setCalibrationState
    );
    
    setValueIfExists(
        "calibration_date",
        otherFields,
        String.class,
        (v) -> v == null ? null : LocalDate.parse(v),
        model::setCalibrationDate
    );
    
    setValueIfExists(
        "calibration_report_path",
        otherFields,
        String.class,
        Paths::get,
        model::setCalibrationReportPath
    );
    
    setValueIfExists(
        "calibration_data_path",
        otherFields,
        String.class,
        Paths::get,
        model::setCalibrationDataPath
    );
    
    return model;
  }
  
  public static NavigationAdditionalFieldsModel navigation(Map<String, Object> otherFields, List<DropDownItem> items) {
    NavigationAdditionalFieldsModel model = new NavigationAdditionalFieldsModel();
    
    setValueIfExists(
        "nav_datum",
        otherFields,
        String.class,
        (v) -> items.stream().filter(dd -> dd.getValue().equals(v)).findFirst().orElse(NavigationDatumDatastore.UNSELECTED_NAVIGATION_DATUM),
        model::setNavDatum
    );
    
    return model;
  }
  
  public static SinglebeamAdditionalFieldsModel singlebeam(Map<String, Object> otherFields, List<DropDownItem> items) {
    SinglebeamAdditionalFieldsModel model = new SinglebeamAdditionalFieldsModel();
    
    setValueIfExists(
        "vertical_datum",
        otherFields,
        String.class,
        (v) -> items.stream().filter(dd -> dd.getValue().equals(v)).findFirst().orElse(SinglebeamVerticalDatumDatastore.UNSELECTED_VERTICAL_DATUM),
        model::setVerticalDatum
    );
    
    setValueIfExists(
        "obs_rate",
        otherFields,
        String.class,
        (v) -> v,
        model::setObsRate
    );
    
    setValueIfExists(
        "sound_velocity",
        otherFields,
        String.class,
        (v) -> v,
        model::setSoundVelocity
    );
    
    return model;
  }
  
  public static MagneticsAdditionalFieldsModel magnetics(Map<String, Object> otherFields, List<DropDownItem> items) {
    MagneticsAdditionalFieldsModel model = new MagneticsAdditionalFieldsModel();
    
    setValueIfExists(
        "correction_model",
        otherFields,
        String.class,
        (v) -> items.stream().filter(dd -> dd.getValue().equals(v)).findFirst().orElse(MagneticsCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL),
        model::setCorrectionModel
    );
    
    setValueIfExists(
        "sample_rate",
        otherFields,
        String.class,
        (v) -> v,
        model::setSampleRate
    );
    
    setValueIfExists(
        "tow_distance",
        otherFields,
        String.class,
        (v) -> v,
        model::setTowDistance
    );
    
    setValueIfExists(
        "sensor_depth",
        otherFields,
        String.class,
        (v) -> v,
        model::setSensorDepth
    );
    
    return model;
  }
  
  public static GravityAdditionalFieldsModel gravity(Map<String, Object> otherFields, List<DropDownItem> items) {
    GravityAdditionalFieldsModel model = new GravityAdditionalFieldsModel();
    setValueIfExists(
        "correction_model",
        otherFields,
        String.class,
        (v) -> items.stream().filter(dd -> dd.getValue().equals(v)).findFirst().orElse(GravityCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL),
        model::setCorrectionModel
    );
    
    setValueIfExists(
        "observation_rate",
        otherFields,
        String.class,
        (v) -> v,
        model::setObservationRate
    );
    
    setValueIfExists(
        "departure_tie",
        otherFields,
        String.class,
        (v) -> v,
        model::setDepartureTie
    );
    
    setValueIfExists(
        "arrival_tie",
        otherFields,
        String.class,
        (v) -> v,
        model::setArrivalTie
    );
    
    setValueIfExists(
        "drift_per_day",
        otherFields,
        String.class,
        (v) -> v,
        model::setDriftPerDay
    );
    
    return model;
  }

  private static <JsonType, ModelType> void setValueIfExists(
      String key,
      Map<String, Object> otherFields,
      Class<JsonType> jsonTypeClass,
      Function<JsonType, ModelType> transform,
      Consumer<ModelType> setter
  ) {
    if (otherFields == null) {
      return;
    }
    Object value = otherFields.get(key);
    if (value != null && value.getClass().isAssignableFrom(jsonTypeClass)) {
      JsonType v = (JsonType) value;
      setter.accept(transform.apply(v));
    }
  }

}
