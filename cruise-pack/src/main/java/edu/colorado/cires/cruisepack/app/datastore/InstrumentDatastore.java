package edu.colorado.cires.cruisepack.app.datastore;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.instrument.InstrumentData;
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
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstrumentDatastore {

  public static final DropDownItem UNSELECTED_DATASET_TYPE = new DropDownItem("", "Select Data Type");


  private final ServiceProperties serviceProperties;
  private List<DropDownItem> datasetTypeDropDowns;

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
    InstrumentData instrumentData;
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
  }

  public List<DropDownItem> getDatasetTypeDropDowns() {
    return datasetTypeDropDowns;
  }
}
