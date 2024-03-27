package edu.colorado.cires.cruisepack.app.datastore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.xml.port.PortData;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class PortDatastoreTest extends XMLDatastoreTest<PortData> {
  
  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(TEST_PATH.toString());
  }
  
  private final PortDatastore datastore = new PortDatastore(SERVICE_PROPERTIES);

  @Test
  void init() {
    datastore.init();

    Set<NameUUIDPair> expected = readFile(PortData.class).getPorts().getPorts().stream()
        .map(d -> new NameUUIDPair(d.getUuid(), d.getName()))
        .collect(Collectors.toSet());
    expected.add(new NameUUIDPair(
        PortDatastore.UNSELECTED_PORT.getId(), PortDatastore.UNSELECTED_PORT.getValue()
    ));
    
    Set<NameUUIDPair> actual = datastore.getPortDropDowns().stream()
        .map(d -> new NameUUIDPair(d.getId(), d.getValue()))
        .collect(Collectors.toSet());
    
    assertEquals(expected, actual);
  }

  @Test
  void getPortNameForUuid() {
    datastore.init();
    
    assertEquals("Aaiun, EH", datastore.getPortNameForUuid("9ac53abe-a2bd-42ad-b233-cd1ea2a9449c"));
    assertNull(datastore.getPortNameForUuid("TEST"));
  }

  @Test
  void getPortUuidForName() {
    datastore.init();

    assertEquals("9ac53abe-a2bd-42ad-b233-cd1ea2a9449c", datastore.getPortUuidForName("Aaiun, EH"));
    assertNull(datastore.getPortUuidForName("TEST"));
  }

  @Override
  protected String getXMLFilename() {
    return "ports.xml";
  }
}