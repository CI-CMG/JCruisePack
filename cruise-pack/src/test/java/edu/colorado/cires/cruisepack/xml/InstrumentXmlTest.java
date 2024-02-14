package edu.colorado.cires.cruisepack.xml;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.cruisepack.xml.instrument.Instrument;
import edu.colorado.cires.cruisepack.xml.instrument.InstrumentData;
import edu.colorado.cires.cruisepack.xml.instrument.InstrumentGroup;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class InstrumentXmlTest {

  private static InstrumentData unmarshal() throws JAXBException, IOException {
    JAXBContext context = JAXBContext.newInstance(InstrumentData.class);
    return (InstrumentData) context.createUnmarshaller()
        .unmarshal(new FileReader("/Users/cslater/projects/JCruisePack/sample-work-dir/data/instruments.xml"));
  }

  @Disabled
  @Test
  public void testRead() throws Exception {
    InstrumentData instrumentData = unmarshal();
    assertEquals(13, instrumentData.getInstrumentGroups().getInstrumentGroups().size());
    InstrumentGroup mb = instrumentData.getInstrumentGroups().getInstrumentGroups().stream().filter((ig) -> ig.getShortType().equals("MB-BATHY")).findFirst().get();
    assertEquals("Multibeam Bathymetry", mb.getDataType());

    Instrument em122 = mb.getInstruments().getInstruments().stream().filter((i) -> i.getUuid().equals("9da1f3f0-9ec8-11e1-a8b0-0800200c9a66")).findFirst().get();
    assertEquals("Kongsberg EM122", em122.getName());
    assertEquals("EM122", em122.getShortName());
    assertEquals(Arrays.asList("all", "kmall"), em122.getFileExtensions());
    assertTrue(em122.isFlatten());
    assertTrue(em122.isUse());

    Instrument hydroSweep = mb.getInstruments().getInstruments().stream().filter((i) -> i.getUuid().equals("f0fc1bcf-5b10-45cc-ab92-d201931a0927")).findFirst().get();
    assertEquals("Atlas Hydrosweep DS", hydroSweep.getName());
    assertEquals("Hydrosweep-DS", hydroSweep.getShortName());
    assertEquals(Collections.emptyList(), hydroSweep.getFileExtensions());
    assertFalse(hydroSweep.isFlatten());
    assertTrue(hydroSweep.isUse());
  }

}