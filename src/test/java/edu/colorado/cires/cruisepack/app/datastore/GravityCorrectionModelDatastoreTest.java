package edu.colorado.cires.cruisepack.app.datastore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.xml.gravityCorrectionModel.GravityCorrectionModel;
import edu.colorado.cires.cruisepack.xml.gravityCorrectionModel.GravityCorrectionModelData;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class GravityCorrectionModelDatastoreTest extends XMLDatastoreTest<GravityCorrectionModelData> {
  
  
  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(TEST_PATH.toString());
  }
  private final GravityCorrectionModelDatastore datastore = new GravityCorrectionModelDatastore(SERVICE_PROPERTIES);
  
  @Override
  protected String getXMLFilename() {
    return "gravityCorrectionModels.xml";
  }

  @Test
  void init() {
    datastore.init();
    
    List<GravityCorrectionModel> data = readFile(GravityCorrectionModelData.class).getGravityCorrectionModels()
        .getGravityCorrectionModels();
    
    Set<NameUUIDPair> expected = data.stream()
        .map(d -> new NameUUIDPair(d.getUuid(), d.getName()))
        .collect(Collectors.toSet());
    expected.add(new NameUUIDPair(
        GravityCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL.getId(), GravityCorrectionModelDatastore.UNSELECTED_CORRECTION_MODEL.getValue()
    ));
    
    Set<NameUUIDPair> actual = datastore.getCorrectionModelDropDowns().stream()
        .map(d -> new NameUUIDPair(d.getId(), d.getValue()))
        .collect(Collectors.toSet());
    
    assertEquals(expected, actual);
  }
}