package edu.colorado.cires.cruisepack.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.service.metadata.PackageInstrument;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MetadataService {

  private final ObjectMapper objectMapper;

  @Autowired
  public MetadataService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public CruiseMetadata createMetadata(PackJob packJob) {
    return CruiseMetadata.builder()
        // TODO
//        .withCruiseId()
//        .withSegmentId()
//        .withPackageId()
//        .withMasterReleaseDate()
//        .withShip()
//        .withShipUuid()
//        .withDeparturePort()
//        .withDepartureDate()
//        .withArrivalPort()
//        .withArrivalDate()
//        .withSeaArea()
//        .withCruiseTitle()
//        .withCruisePurpose()
//        .withCruiseDescription()
//        .withSponsors()
//        .withFunders()
//        .withScientists()
//        .withProjects()
//        .withOmics()
//        .withMetadataAuthor()
//        .withInstruments()
        .build();
  }

  public CruiseMetadata createDatasetMetadata(CruiseMetadata cruiseMetadata, List<InstrumentDetail> instruments) {
    Map<String, PackageInstrument> packageInstruments = new LinkedHashMap<>();
    for (InstrumentDetail dataset : instruments) {
      Instrument instrument = resolveInstrument(cruiseMetadata, dataset);
      PackageInstrument packageInstrument = PackageInstrument.builder()
          .withInstrument(instrument)
          .withTypeName(dataset.getShortName())
          .withFlatten(dataset.isFlatten())
          .withExtensions(new ArrayList<>(dataset.getExtensions()))
          .withDirName(dataset.getDirName())
          .withBagName(dataset.getBagName())
          .build();
      packageInstruments.put(dataset.getDirName(), packageInstrument);
    }

    return CruiseMetadata.builder(cruiseMetadata)
        .withPackageInstruments(packageInstruments)
        .build();
  }

  public void writeMetadata(CruiseMetadata cruiseMetadata, Path file) {
    try (OutputStream outputStream = Files.newOutputStream(file)) {
      objectMapper.writeValue(outputStream, cruiseMetadata);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to write metadata " + file, e);
    }
  }

  private static Instrument resolveInstrument(CruiseMetadata cruiseMetadata, InstrumentDetail dataset) {
    return cruiseMetadata.getInstruments().stream()
        .filter(instrument -> Paths.get(instrument.getDataPath()).equals(dataset.getDataPath()))
        .findFirst().orElseThrow(() -> new IllegalStateException("Metadata for " + dataset.getDataPath() + " was not found"));
  }
}
