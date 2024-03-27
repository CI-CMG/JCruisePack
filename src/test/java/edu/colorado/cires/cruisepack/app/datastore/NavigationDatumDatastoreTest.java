package edu.colorado.cires.cruisepack.app.datastore;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.xml.navigationDatum.NavigationDatum;
import edu.colorado.cires.cruisepack.xml.navigationDatum.NavigationDatumData;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class NavigationDatumDatastoreTest extends XMLDatastoreTest<NavigationDatumData> {

  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(TEST_PATH.toString());
  }
  
  private final NavigationDatumDatastore datastore = new NavigationDatumDatastore(SERVICE_PROPERTIES);

  @Test
  void init() {
    datastore.init();

    List<NavigationDatum> data = readFile(NavigationDatumData.class).getNavigationDatums()
        .getNavigationData();
    
    Set<NameUUIDPair> expected = data.stream()
        .map(d -> new NameUUIDPair(d.getUuid(), d.getName()))
        .collect(Collectors.toSet());
    expected.add(new NameUUIDPair(
        NavigationDatumDatastore.UNSELECTED_NAVIGATION_DATUM.getId(), NavigationDatumDatastore.UNSELECTED_NAVIGATION_DATUM.getValue()
    ));
    
    Set<NameUUIDPair> actual = datastore.getNavigationDatumDropDowns().stream()
        .map(d -> new NameUUIDPair(d.getId(), d.getValue()))
        .collect(Collectors.toSet());
    
    assertEquals(expected, actual);
  }

  @Override
  protected String getXMLFilename() {
    return "navigationDatums.xml";
  }
}