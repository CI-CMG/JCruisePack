package edu.colorado.cires.cruisepack.app.datastore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.xml.singlebeamVerticalDatum.SinglebeamVerticalDatumData;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class SinglebeamVerticalDatumDatastoreTest extends XMLDatastoreTest<SinglebeamVerticalDatumData> {
  
  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(TEST_PATH.toString());
  }
  
  private final SinglebeamVerticalDatumDatastore datastore = new SinglebeamVerticalDatumDatastore(SERVICE_PROPERTIES);

  @Test
  void init() {
    datastore.init();

    Set<NameUUIDPair> expected = readFile(SinglebeamVerticalDatumData.class).getSinglebeamVerticalDatums()
        .getSinglebeamVerticalData().stream()
        .map(d -> new NameUUIDPair(d.getUuid(), d.getName()))
        .collect(Collectors.toSet());
    expected.add(new NameUUIDPair(
        SinglebeamVerticalDatumDatastore.UNSELECTED_VERTICAL_DATUM.getId(), SinglebeamVerticalDatumDatastore.UNSELECTED_VERTICAL_DATUM.getValue()
    ));
    
    Set<NameUUIDPair> actual = datastore.getVerticalDatumDropDowns().stream()
        .map(d -> new NameUUIDPair(d.getId(), d.getValue()))
        .collect(Collectors.toSet());
    
    assertEquals(expected, actual);
  }

  @Override
  protected String getXMLFilename() {
    return "singlebeamVerticalDatums.xml";
  }
}