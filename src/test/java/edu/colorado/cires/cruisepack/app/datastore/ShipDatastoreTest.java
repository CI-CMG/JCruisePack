package edu.colorado.cires.cruisepack.app.datastore;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.xml.ship.ShipData;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class ShipDatastoreTest extends XMLDatastoreTest<ShipData> {
  
  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(TEST_PATH.toString());
  }
  
  private final ShipDatastore datastore = new ShipDatastore(SERVICE_PROPERTIES);

  @Test
  void init() {
    datastore.init();
    
    Set<NameUUIDPair> expected = readFile(ShipData.class).getShips().getShips().stream()
        .map(d -> new NameUUIDPair(d.getUuid(), d.getName()))
        .collect(Collectors.toSet());
    expected.add(new NameUUIDPair(
        ShipDatastore.UNSELECTED_SHIP.getId(), ShipDatastore.UNSELECTED_SHIP.getValue()
    ));
    
    Set<NameUUIDPair> actual = datastore.getShipDropDowns().stream()
        .map(d -> new NameUUIDPair(d.getId(), d.getValue()))
        .collect(Collectors.toSet());
    
    assertEquals(expected, actual);
  }

  @Test
  void getShipNameForUuid() {
    datastore.init();
    
    assertEquals("Alaska Knight", datastore.getShipNameForUuid("0d069136-2a5b-4bdd-889f-ef132972465f"));
    assertNull(datastore.getShipNameForUuid("TEST"));
  }

  @Test
  void getShipUuidForName() {
    datastore.init();

    assertEquals("0d069136-2a5b-4bdd-889f-ef132972465f", datastore.getShipUuidForName("Alaska Knight"));
    assertNull(datastore.getShipUuidForName("TEST"));
  }

  @Override
  protected String getXMLFilename() {
    return "ships.xml";
  }
}