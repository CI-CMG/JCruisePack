package edu.colorado.cires.cruisepack.app.datastore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.xml.waterColumnCalibrationState.WaterColumnCalibrationStateData;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class WaterColumnCalibrationStateDatastoreTest extends XMLDatastoreTest<WaterColumnCalibrationStateData> {
  
  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(TEST_PATH.toString());
  }
  
  private final WaterColumnCalibrationStateDatastore datastore = new WaterColumnCalibrationStateDatastore(SERVICE_PROPERTIES);

  @Test
  void init() {
    datastore.init();

    Set<NameUUIDPair> expected = readFile(WaterColumnCalibrationStateData.class).getWaterColumnCalibrationStates()
        .getWaterColumnCalibrationStates().stream()
        .map(d -> new NameUUIDPair(d.getUuid(), d.getName()))
        .collect(Collectors.toSet());
    expected.add(new NameUUIDPair(
        WaterColumnCalibrationStateDatastore.UNSELECTED_CALIBRATION_STATE.getId(), WaterColumnCalibrationStateDatastore.UNSELECTED_CALIBRATION_STATE.getValue()
    ));
    
    Set<NameUUIDPair> actual = datastore.getCalibrationStateDropDowns().stream()
        .map(d -> new NameUUIDPair(d.getId(), d.getValue()))
        .collect(Collectors.toSet());
    
    assertEquals(expected, actual);
  }

  @Override
  protected String getXMLFilename() {
    return "waterColumnCalibrationStates.xml";
  }
}