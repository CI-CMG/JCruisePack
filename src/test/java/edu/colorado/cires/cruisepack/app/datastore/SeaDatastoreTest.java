package edu.colorado.cires.cruisepack.app.datastore;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.xml.sea.SeaData;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class SeaDatastoreTest extends XMLDatastoreTest<SeaData> {
  
  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(TEST_PATH.toString());
  }
  
  private final SeaDatastore datastore = new SeaDatastore(SERVICE_PROPERTIES);

  @Test
  void init() {
    datastore.init();

    Set<NameUUIDPair> expected = readFile(SeaData.class).getSeas().getSeas().stream()
        .map(d -> new NameUUIDPair(d.getUuid(), d.getName()))
        .collect(Collectors.toSet());
    expected.add(new NameUUIDPair(
        SeaDatastore.UNSELECTED_SEA.getId(), SeaDatastore.UNSELECTED_SEA.getValue()
    ));
    
    Set<NameUUIDPair> actual = datastore.getSeaDropDowns().stream()
        .map(d -> new NameUUIDPair(d.getId(), d.getValue()))
        .collect(Collectors.toSet());
    
    assertEquals(expected, actual);
  }

  @Test
  void getSeaNameForUuid() {
    datastore.init();
    
    assertEquals("Adriatic Sea", datastore.getSeaNameForUuid("1b4147b8-c4f6-4f30-b002-79462262f94f"));
    assertNull(datastore.getSeaNameForUuid("TEST"));
  }

  @Test
  void getSeaUuidForName() {
    datastore.init();
    
    assertEquals("1b4147b8-c4f6-4f30-b002-79462262f94f", datastore.getSeaUuidForName("Adriatic Sea"));
    assertNull(datastore.getSeaUuidForName("TEST"));
  }

  @Override
  protected String getXMLFilename() {
    return "seas.xml";
  }
}