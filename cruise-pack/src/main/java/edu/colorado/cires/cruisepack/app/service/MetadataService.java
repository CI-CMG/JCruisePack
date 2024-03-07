package edu.colorado.cires.cruisepack.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.service.metadata.InstrumentMetadata;
import edu.colorado.cires.cruisepack.app.service.metadata.MetadataAuthor;
import edu.colorado.cires.cruisepack.app.service.metadata.Omics;
import edu.colorado.cires.cruisepack.app.service.metadata.OmicsPoc;
import edu.colorado.cires.cruisepack.app.service.metadata.PackageInstrument;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MetadataService {

  private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private final ObjectMapper objectMapper;
  private final ShipDatastore shipDatastore;
  private final PortDatastore portDatastore;
  private final SeaDatastore seaDatastore;
  private final InstrumentDatastore instrumentDatastore;

  @Autowired
  public MetadataService(ObjectMapper objectMapper, ShipDatastore shipDatastore, PortDatastore portDatastore, SeaDatastore seaDatastore,
      InstrumentDatastore instrumentDatastore) {
    this.objectMapper = objectMapper;
    this.shipDatastore = shipDatastore;
    this.portDatastore = portDatastore;
    this.seaDatastore = seaDatastore;
    this.instrumentDatastore = instrumentDatastore;
  }

  public CruiseMetadata createMetadata(PackJob packJob) {
    return CruiseMetadata.builder()
        .withCruiseId(packJob.getCruiseId())
        .withSegmentId(packJob.getSegment())
        .withPackageId(packJob.getPackageId())
        .withMasterReleaseDate(packJob.getReleaseDate() == null ? null : packJob.getReleaseDate().format(DTF))
        .withShip(packJob.getShipUuid() == null ? null : shipDatastore.getShipNameForUuid(packJob.getShipUuid()))
        .withShipUuid(packJob.getShipUuid())
        .withProjects(packJob.getProjects())
        .withDeparturePort(packJob.getDeparturePortUuid() == null ? null : portDatastore.getPortNameForUuid(packJob.getDeparturePortUuid()))
        .withDepartureDate(packJob.getDepartureDate() == null ? null : packJob.getDepartureDate().format(DTF))
        .withArrivalPort(packJob.getArrivalPortUuid() == null ? null : portDatastore.getPortNameForUuid(packJob.getArrivalPortUuid()))
        .withArrivalDate(packJob.getArrivalDate() == null ? null : packJob.getArrivalDate().format(DTF))
        .withSeaArea(packJob.getSeaUuid() == null ? null : seaDatastore.getSeaNameForUuid(packJob.getSeaUuid()))
        .withCruiseTitle(packJob.getCruiseTitle())
        .withCruisePurpose(packJob.getCruisePurpose())
        .withCruiseDescription(packJob.getCruiseDescription())
       .withSponsors(packJob.getSources())
       .withFunders(packJob.getFunders())
       .withMetadataAuthor(packJob.getMetadataAuthor() == null ? null : MetadataAuthor.builder()
       .withUuid(packJob.getMetadataAuthor().getUuid())
       .withName(packJob.getMetadataAuthor().getName())
       .withEmail(packJob.getMetadataAuthor().getEmail())
       .withPhone(packJob.getMetadataAuthor().getPhone())
       .build())
       //        .withProjects()
       .withScientists(packJob.getScientists())
       .withOmics(Omics.builder()
          .withSamplingTypes(packJob.getOmicsSamplingTypes())
          .withAnalysesTypes(packJob.getOmicsExpectedAnalyses())
          .withOmicsPoc(OmicsPoc.builder()
           .withUuid(packJob.getOmicsContactUuid())
            .withName(packJob.getOmicsContactName())
            .withEmail(packJob.getOmicsContactEmail())
            .withPhone(packJob.getOmicsContactPhone())
          .build())
          .withNcbiAccession(packJob.getOmicsBioProjectAccession())
          .withOmicsComment(packJob.getOmicsAdditionalSamplingInformation())
        .build())
        .withInstruments(getInstrumentsJson(packJob))
        .build();
  }

  private List<InstrumentMetadata> getInstrumentsJson(PackJob packJob) {
    List<InstrumentMetadata> instruments = new ArrayList<>();
    for (Entry<InstrumentDetailPackageKey, List<InstrumentDetail>> entry : packJob.getInstruments().entrySet()) {
      for (InstrumentDetail instrumentDetail : entry.getValue()) {
        instruments.add(instrumentDetailToJson(entry.getKey(), instrumentDetail));
      }
    }
    return instruments;
  }

  private InstrumentMetadata instrumentDetailToJson(InstrumentDetailPackageKey key, InstrumentDetail instrumentDetail) {
    
    InstrumentMetadata.Builder builder = InstrumentMetadata.builder()
        .withUuid(instrumentDetail.getUuid())
        .withType(instrumentDatastore.getNameForShortCode(key.getInstrumentGroupShortType()))
        .withInstrument(instrumentDetail.getInstrument())
        .withShortName(instrumentDetail.getShortName())
        .withReleaseDate(instrumentDetail.getReleaseDate() == null ? null : instrumentDetail.getReleaseDate().format(DTF))
        .withStatus(instrumentDetail.getStatus() == null ? null : instrumentDetail.getStatus().getStrValue())
        .withDataComment(instrumentDetail.getDataComment())
        .withDirName(instrumentDetail.getDirName())
        .withBagName(instrumentDetail.getBagName())
        .withAncillaryDataDetails(instrumentDetail.getAncillaryDataDetails());
    
    for (Entry<String, Object> entry : instrumentDetail.getAdditionalFields().entrySet()) {
      builder = builder.withOtherField(entry.getKey(), entry.getValue());
    }

    return builder.build();
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
        .filter(instrument -> instrument.getBagName().equals(dataset.getBagName()) && instrument.getDirName().equals(dataset.getDirName()))
        .findFirst().orElseThrow(() -> new IllegalStateException("Metadata for " + dataset.getDataPath() + " was not found"));
  }
}
