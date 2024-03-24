package edu.colorado.cires.cruisepack.app.ui.model;

import static org.junit.jupiter.api.Assertions.*;

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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class AdditionalFieldsModelFactoryTest {
  
  private static final List<DropDownItem> OPTIONS = List.of(
      new DropDownItem("1", "value1"), new DropDownItem("2", "value 2")
  );

  @Test
  void waterColumn() {
    LocalDate date = LocalDate.now();
    
    WaterColumnAdditionalFieldsModel model = AdditionalFieldsModelFactory.waterColumn(
        Map.of(
            "calibration_state", OPTIONS.get(1).getValue(),
            "calibration_date", date.toString(),
            "calibration_report_path", "calibration report path",
            "calibration_data_path", "calibration data path"
        ),
        OPTIONS
    );
    
    assertEquals(OPTIONS.get(1), model.getCalibrationState());
    assertEquals(date, model.getCalibrationDate());
    assertEquals(Paths.get("calibration report path"), model.getCalibrationReportPath());
    assertEquals(Paths.get("calibration data path"), model.getCalibrationDataPath());
    
    model = AdditionalFieldsModelFactory.waterColumn(
        Collections.emptyMap(),
        OPTIONS
    );
    
    assertEquals(WaterColumnCalibrationStateDatastore.UNSELECTED_CALIBRATION_STATE, model.getCalibrationState());
    assertNull(model.getCalibrationDate());
    assertNull(model.getCalibrationReportPath());
    assertNull(model.getCalibrationDataPath());
  }

  @Test
  void navigation() {
    NavigationAdditionalFieldsModel model = AdditionalFieldsModelFactory.navigation(
      Map.of(
          "nav_datum", OPTIONS.get(0).getValue()
      ),
      OPTIONS
    );
    
    assertEquals(OPTIONS.get(0), model.getNavDatum());
    
    model = AdditionalFieldsModelFactory.navigation(
        Collections.emptyMap(),
        OPTIONS
    );
    assertEquals(NavigationDatumDatastore.UNSELECTED_NAVIGATION_DATUM, model.getNavDatum());
  }

  @Test
  void singlebeam() {
    SinglebeamAdditionalFieldsModel model = AdditionalFieldsModelFactory.singlebeam(
        Map.of(
            "vertical_datum", OPTIONS.get(1).getValue(),
            "obs_rate", "20",
            "sound_velocity", "10"
        ),
        OPTIONS
    );
    
    assertEquals(OPTIONS.get(1), model.getVerticalDatum());
    assertEquals("20", model.getObsRate());
    assertEquals("10", model.getSoundVelocity());
    
    model = AdditionalFieldsModelFactory.singlebeam(
        Collections.emptyMap(),
        OPTIONS
    );
    
    assertEquals(SinglebeamVerticalDatumDatastore.UNSELECTED_VERTICAL_DATUM, model.getVerticalDatum());
    assertNull(model.getObsRate());
    assertNull(model.getSoundVelocity());
  }

  @Test
  void magnetics() {
    MagneticsAdditionalFieldsModel model = AdditionalFieldsModelFactory.magnetics(
        Map.of(
            "correction_model", OPTIONS.get(0).getValue(),
            "sample_rate", "10",
            "tow_distance", "20",
            "sensor_depth", "30"
        ),
        OPTIONS
    );
    
    assertEquals(OPTIONS.get(0), model.getCorrectionModel());
    assertEquals("10", model.getSampleRate());
    assertEquals("20", model.getTowDistance());
    assertEquals("30", model.getSensorDepth());
    
    model = AdditionalFieldsModelFactory.magnetics(
        Collections.emptyMap(),
        OPTIONS
    );
    assertEquals(MagneticsCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL, model.getCorrectionModel());
    assertNull(model.getSampleRate());
    assertNull(model.getTowDistance());
    assertNull(model.getSensorDepth());
  }

  @Test
  void gravity() {
    GravityAdditionalFieldsModel model = AdditionalFieldsModelFactory.gravity(
        Map.of(
            "correction_model", OPTIONS.get(1).getValue(),
            "observation_rate", "10",
            "departure_tie", "20",
            "arrival_tie", "30",
            "drift_per_day", "40"
        ),
        OPTIONS
    );
    
    assertEquals(OPTIONS.get(1), model.getCorrectionModel());
    assertEquals("10", model.getObservationRate());
    assertEquals("20", model.getDepartureTie());
    assertEquals("30", model.getArrivalTie());
    assertEquals("40", model.getDriftPerDay());
    
    model = AdditionalFieldsModelFactory.gravity(
        Collections.emptyMap(),
        OPTIONS
    );
    
    assertEquals(GravityCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL, model.getCorrectionModel());
    assertNull(model.getObservationRate());
    assertNull(model.getDepartureTie());
    assertNull(model.getArrivalTie());
    assertNull(model.getDriftPerDay());
  }
}