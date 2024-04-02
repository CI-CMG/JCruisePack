package edu.colorado.cires.cruisepack.app.datastore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.service.InstrumentDetailPackageKey;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import edu.colorado.cires.cruisepack.xml.instrument.Instrument;
import edu.colorado.cires.cruisepack.xml.instrument.InstrumentData;
import edu.colorado.cires.cruisepack.xml.instrument.InstrumentGroup;
import edu.colorado.cires.cruisepack.xml.instrument.InstrumentList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class InstrumentDatastoreTest extends XMLDatastoreTest<InstrumentData> {
  
  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(TEST_PATH.toString());
  }
  
  private final InstrumentDatastore datastore = new InstrumentDatastore(SERVICE_PROPERTIES);

  @Test
  void init() {
    datastore.init();
    
    Set<NameUUIDPair> expected = readFile(InstrumentData.class).getInstrumentGroups().getInstrumentGroups().stream()
        .map(i -> new NameUUIDPair(i.getShortType(), i.getDataType()))
        .collect(Collectors.toSet());
    expected.add(new NameUUIDPair(
        InstrumentDatastore.UNSELECTED_DATASET_TYPE.getId(), InstrumentDatastore.UNSELECTED_DATASET_TYPE.getValue()
    ));
    
    Set<NameUUIDPair> actual = datastore.getDatasetTypeDropDowns().stream()
        .map(d -> new NameUUIDPair(d.getId(), d.getValue()))
        .collect(Collectors.toSet());
    
    assertEquals(expected, actual);
  }

  @Test
  void getInstrument() {
    datastore.init();
    
    Optional<Instrument> maybeInstrument = datastore.getInstrument(new InstrumentDetailPackageKey(
        InstrumentGroupName.WATER_COLUMN.getShortName(), 
        "Hydrosweep-DS"
    ));
    assertTrue(maybeInstrument.isPresent());
    assertEquals("Hydrosweep-DS", maybeInstrument.get().getShortName());
    assertEquals("Atlas Hydrosweep DS", maybeInstrument.get().getName());
    
    maybeInstrument = datastore.getInstrument(new InstrumentDetailPackageKey(
        "TEST",
        "TEST"
    ));
    assertTrue(maybeInstrument.isEmpty());
  }

  @Test
  void getInstrumentDropDownsForDatasetType() {
    datastore.init();

    Set<NameUUIDPair> expected = readFile(InstrumentData.class).getInstrumentGroups().getInstrumentGroups().stream()
        .filter(i -> i.getDataType().equals(InstrumentGroupName.WATER_COLUMN.getLongName()))
        .map(InstrumentGroup::getInstruments)
        .map(InstrumentList::getInstruments)
        .flatMap(List::stream)
        .map(i -> new NameUUIDPair(i.getUuid(), i.getShortName()))
        .collect(Collectors.toSet());
    expected.add(new NameUUIDPair(
        InstrumentDatastore.UNSELECTED_INSTRUMENT.getId(), InstrumentDatastore.UNSELECTED_INSTRUMENT.getValue()
    ));
    
    Set<NameUUIDPair> actual = datastore.getInstrumentDropDownsForDatasetType(InstrumentGroupName.WATER_COLUMN.getShortName()).stream()
        .map(d -> new NameUUIDPair(d.getId(), d.getValue()))
        .collect(Collectors.toSet());
    
    assertEquals(expected, actual);
  }

  @Test
  void getInstrumentUuidForDatasetTypeAndInstrumentName() {
    datastore.init();
    
    String expected = "f0fc1bcf-5b10-45cc-ab92-d201931a0927";
    
    String actual = datastore.getInstrumentUuidForDatasetTypeAndInstrumentName(
        InstrumentGroupName.WATER_COLUMN.getShortName(),
        "Hydrosweep-DS"
    );
    
    assertNotNull(actual);
    assertEquals(expected, actual);
    
    assertNull(datastore.getInstrumentUuidForDatasetTypeAndInstrumentName(
        "TEST",
        "TEST"
    ));
  }

  @Test
  void getNameForShortCode() {
    datastore.init();
    
    assertEquals(
        InstrumentGroupName.WATER_COLUMN.getLongName(),
        datastore.getNameForShortCode(InstrumentGroupName.WATER_COLUMN.getShortName())
    );
  }

  @Override
  protected String getXMLFilename() {
    return "instruments.xml";
  }
}