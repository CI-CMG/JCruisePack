package edu.colorado.cires.cruisepack.xml;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;

public class InstrumentXmlTest {

//  private static Instruments unmarshal() throws JAXBException, IOException {
//    JAXBContext context = JAXBContext.newInstance(Instruments.class);
//    return (Instruments) context.createUnmarshaller()
//        .unmarshal(new FileReader("src/main/resources/edu/colorado/cires/cruisepack/datastore/instruments.xml"));
//  }
//
//  @Test
//  public void testRead() throws Exception {
//    Instruments instruments = unmarshal();
//    assertEquals(13, instruments.getInstrumentGroups().size());
//    InstrumentGroup mb = instruments.getInstrumentGroups().stream().filter((ig) -> ig.shortType.equals("MB-BATHY")).findFirst().get();
//    assertEquals("Multibeam Bathymetry", mb.getDataType());
//
//    Instrument em122 = mb.getInstruments().stream().filter((i) -> i.getUuid().equals("9da1f3f0-9ec8-11e1-a8b0-0800200c9a66")).findFirst().get();
//    assertEquals("Kongsberg EM122", em122.getName());
//    assertEquals("EM122", em122.getShortName());
//    assertEquals(Arrays.asList("all", "kmall"), em122.getFileExtensions());
//    assertTrue(em122.isFlatten());
//    assertTrue(em122.isUse());
//
//    Instrument hydroSweep = mb.getInstruments().stream().filter((i) -> i.getUuid().equals("f0fc1bcf-5b10-45cc-ab92-d201931a0927")).findFirst().get();
//    assertEquals("Atlas Hydrosweep DS", hydroSweep.getName());
//    assertEquals("Hydrosweep-DS", hydroSweep.getShortName());
//    assertEquals(Collections.emptyList(), hydroSweep.getFileExtensions());
//    assertFalse(hydroSweep.isFlatten());
//    assertTrue(hydroSweep.isUse());
//  }

}