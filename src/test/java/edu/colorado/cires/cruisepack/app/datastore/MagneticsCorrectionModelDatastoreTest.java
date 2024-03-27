package edu.colorado.cires.cruisepack.app.datastore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.xml.magneticsCorrectionModel.MagneticsCorrectionModel;
import edu.colorado.cires.cruisepack.xml.magneticsCorrectionModel.MagneticsCorrectionModelData;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class MagneticsCorrectionModelDatastoreTest extends XMLDatastoreTest<MagneticsCorrectionModelData> {

  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(TEST_PATH.toString());
  }
  private final MagneticsCorrectionModelDatastore datastore = new MagneticsCorrectionModelDatastore(SERVICE_PROPERTIES);

  @Test
  void init() {
    datastore.init();

    List<MagneticsCorrectionModel> data = readFile(MagneticsCorrectionModelData.class).getMagneticsCorrectionModels()
        .getMagneticsCorrectionModels();

    Set<NameUUIDPair> expected = data.stream()
        .map(d -> new NameUUIDPair(d.getUuid(), d.getName()))
        .collect(Collectors.toSet());
    expected.add(new NameUUIDPair(
        MagneticsCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL.getId(), MagneticsCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL.getValue()
    ));

    Set<NameUUIDPair> actual = datastore.getCorrectionModelDropDowns().stream()
        .map(d -> new NameUUIDPair(d.getId(), d.getValue()))
        .collect(Collectors.toSet());

    assertEquals(expected, actual);
  }

  @Override
  protected String getXMLFilename() {
    return "magneticsCorrectionModels.xml";
  }
}