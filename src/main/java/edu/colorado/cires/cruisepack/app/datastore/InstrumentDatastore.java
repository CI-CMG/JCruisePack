package edu.colorado.cires.cruisepack.app.datastore;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.service.InstrumentDetailPackageKey;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.instrument.Instrument;
import edu.colorado.cires.cruisepack.xml.instrument.InstrumentData;
import edu.colorado.cires.cruisepack.xml.instrument.InstrumentGroup;
import edu.colorado.cires.cruisepack.xml.instrument.InstrumentGroupList;
import edu.colorado.cires.cruisepack.xml.instrument.InstrumentList;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstrumentDatastore {

  public static final DropDownItem UNSELECTED_DATASET_TYPE = new DropDownItem("", "Select Data Type");
  public static final DropDownItem UNSELECTED_INSTRUMENT = new DropDownItem("", "Select Instrument");


  private final ServiceProperties serviceProperties;
  private List<DropDownItem> datasetTypeDropDowns;
  private Map<String, List<DropDownItem>> instrumentDropDowns;
  private InstrumentData instrumentData;

  @Autowired
  public InstrumentDatastore(ServiceProperties serviceProperties) {
    this.serviceProperties = serviceProperties;
  }

  @PostConstruct
  public void init() {
    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path dataDir = workDir.resolve("data");
    Path instrumentFile = dataDir.resolve("instruments.xml");
    if (!Files.isRegularFile(instrumentFile)) {
      throw new IllegalStateException("Unable to read " + instrumentFile);
    }
    try (Reader reader = Files.newBufferedReader(instrumentFile, StandardCharsets.UTF_8)) {
      instrumentData = (InstrumentData) JAXBContext.newInstance(InstrumentData.class).createUnmarshaller().unmarshal(reader);
    } catch (IOException | JAXBException e) {
      throw new IllegalStateException("Unable to parse " + instrumentFile, e);
    }
    datasetTypeDropDowns = new ArrayList<>(instrumentData.getInstrumentGroups().getInstrumentGroups().size() + 1);
    datasetTypeDropDowns.add(UNSELECTED_DATASET_TYPE);
    instrumentData.getInstrumentGroups().getInstrumentGroups().stream()
        .sorted((s1, s2) -> s1.getDataType().compareToIgnoreCase(s2.getDataType()))
        .map(instrumentGroup -> new DropDownItem(instrumentGroup.getShortType(), instrumentGroup.getDataType()))
        .forEach(datasetTypeDropDowns::add);

    instrumentDropDowns = new HashMap<>(0);
    instrumentData.getInstrumentGroups().getInstrumentGroups().forEach((ig) -> {
      List<DropDownItem> instruments = new ArrayList<>(ig.getInstruments().getInstruments().stream()
        .map(i -> new DropDownItem(i.getUuid(), i.getShortName()))
        .sorted((i1, i2) -> i1.getValue().compareToIgnoreCase(i2.getValue()))
        .toList());
      instruments.add(0, UNSELECTED_INSTRUMENT);
      instrumentDropDowns.put(
        ig.getShortType(),
        instruments
      );
    });
  }
  
  public Optional<Instrument> getInstrumentByTypeAndInstrumentName(String type, String instrumentName) {
    return instrumentData.getInstrumentGroups().getInstrumentGroups().stream()
        .filter(instrumentGroup -> instrumentGroup.getShortType().equals(type))
        .findFirst()
        .flatMap(instrumentGroup -> 
              instrumentGroup.getInstruments().getInstruments().stream()
                .filter(instrument -> instrument.getName().equals(instrumentName))
                .findFirst()
        );

  }

  public Optional<Instrument> getInstrument(InstrumentDetailPackageKey key) {
    InstrumentGroupList instrumentGroupList = instrumentData.getInstrumentGroups();
    if (instrumentGroupList != null) {
      List<InstrumentGroup> instrumentGroups = instrumentGroupList.getInstrumentGroups();
      if (instrumentGroups != null) {
        Optional<InstrumentGroup> maybeGroup = instrumentGroups.stream()
            .filter(ig -> ig.getShortType().equals(key.getInstrumentGroupShortType()))
            .findFirst();
        if(maybeGroup.isPresent()) {
          InstrumentGroup ig = maybeGroup.get();
          InstrumentList instrumentList = ig.getInstruments();
          if (instrumentList != null) {
            List<Instrument> instruments = instrumentList.getInstruments();
            if (instruments != null) {
              return instruments.stream()
                  .filter(instrument -> instrument.getShortName().equals(key.getInstrumentShortCode()))
                  .findFirst();
            }
          }
        }
      }
    }
    return Optional.empty();
  }

  public List<DropDownItem> getDatasetTypeDropDowns() {
    return datasetTypeDropDowns;
  }

  public List<DropDownItem> getInstrumentDropDownsForDatasetType(String datasetType) {
    return instrumentDropDowns.get(datasetType);
  }

  public String getInstrumentUuidForDatasetTypeAndInstrumentName(String datasetType, String instrumentName) {
    List<DropDownItem> typeDds = getInstrumentDropDownsForDatasetType(datasetType);
    if (typeDds != null) {
      return typeDds.stream().filter(dd -> dd.getValue().equals(instrumentName)).findFirst().map(DropDownItem::getId).orElse(null);
    }
    return null;
  }

  public String getNameForShortCode(String shortCode) {
    return datasetTypeDropDowns.stream().filter(dd -> shortCode.equals(dd.getId())).map(DropDownItem::getValue).findFirst().orElse(null);
  }

}
