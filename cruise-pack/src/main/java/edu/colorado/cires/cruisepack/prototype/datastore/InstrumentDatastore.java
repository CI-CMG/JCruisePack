package edu.colorado.cires.cruisepack.prototype.datastore;

//import edu.colorado.cires.cruisepack.xml.Instrument;
//import edu.colorado.cires.cruisepack.xml.Instruments;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class InstrumentDatastore {

//  private final Instruments instruments;
//
//  public InstrumentDatastore() {
//    try (Reader in = new InputStreamReader(getClass().getResourceAsStream("instruments.xml"), StandardCharsets.UTF_8)) {
//      instruments = (Instruments) JAXBContext.newInstance(Instruments.class).createUnmarshaller().unmarshal(in);
//    } catch (IOException | JAXBException e) {
//      throw new RuntimeException("Unable to read instruments.xml", e);
//    }
//  }
//
//  public Map<String, List<String>> getDropDowns() {
//    Map<String, List<String>> dropdowns = new HashMap<>();
//    instruments.getInstrumentGroups().forEach((instrumentGroup -> {
//      dropdowns.put(
//          instrumentGroup.getDataType(),
//          instrumentGroup.getInstruments().stream().map(Instrument::getName).collect(Collectors.toList()));
//    }));
//    return dropdowns;
//  }

}
