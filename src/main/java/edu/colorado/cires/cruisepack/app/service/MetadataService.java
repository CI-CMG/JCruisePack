package edu.colorado.cires.cruisepack.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.service.metadata.InstrumentData;
import edu.colorado.cires.cruisepack.app.service.metadata.InstrumentMetadata;
import edu.colorado.cires.cruisepack.app.service.metadata.MetadataAuthor;
import edu.colorado.cires.cruisepack.app.service.metadata.Omics;
import edu.colorado.cires.cruisepack.app.service.metadata.OmicsPoc;
import edu.colorado.cires.cruisepack.app.service.metadata.PackageInstrument;
import edu.colorado.cires.cruisepack.app.service.metadata.PeopleOrg;
import edu.colorado.cires.cruisepack.app.ui.model.ImportModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
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
  private final PersonDatastore personDatastore;
  private final OrganizationDatastore organizationDatastore;

  @Autowired
  public MetadataService(ObjectMapper objectMapper, ShipDatastore shipDatastore, PortDatastore portDatastore, SeaDatastore seaDatastore,
      InstrumentDatastore instrumentDatastore, PersonDatastore personDatastore, OrganizationDatastore organizationDatastore) {
    this.objectMapper = objectMapper;
    this.shipDatastore = shipDatastore;
    this.portDatastore = portDatastore;
    this.seaDatastore = seaDatastore;
    this.instrumentDatastore = instrumentDatastore;
    this.personDatastore = personDatastore;
    this.organizationDatastore = organizationDatastore;
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
            .withNCBIAccession(packJob.getOmicsBioProjectAccession())
            .withSamplingTypes(packJob.getOmicsSamplingTypes())
            .withAnalysesTypes(packJob.getOmicsExpectedAnalyses())
            .withOmicsComment(packJob.getOmicsAdditionalSamplingInformation())
            .withOmicsPoc(OmicsPoc.builder()
                .withUuid(packJob.getOmicsContactUuid())
                .withName(packJob.getOmicsContactName())
                .withEmail(packJob.getOmicsContactEmail())
                .withPhone(packJob.getOmicsContactPhone())
                .build())
            .build())
        .withInstruments(getInstrumentsJson(packJob))
        .build();
  }
  
  public CruiseData createData(PackJob packJob) {
    return CruiseData.builder()
        .withUse(true)
        .withDelete(false)
        .withDocumentsPath(packJob.getDocumentsPath() == null ? null : packJob.getDocumentsPath().toString())
        .withPackageDirectory(packJob.getPackageDirectory() == null ? null : packJob.getPackageDirectory().toString())
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
            .withNCBIAccession(packJob.getOmicsBioProjectAccession())
            .withSamplingTypes(packJob.getOmicsSamplingTypes())
            .withAnalysesTypes(packJob.getOmicsExpectedAnalyses())
            .withOmicsComment(packJob.getOmicsAdditionalSamplingInformation())
            .withOmicsPoc(OmicsPoc.builder()
                .withUuid(packJob.getOmicsContactUuid())
                .withName(packJob.getOmicsContactName())
                .withEmail(packJob.getOmicsContactEmail())
                .withPhone(packJob.getOmicsContactPhone())
                .build())
            .build())
        .withInstruments(getInstrumentDataJson(packJob))
        .build();
  }
  
  public CruiseData createData(ImportRow row, ImportModel importModel) {
    Path destPath = importModel.getDestinationPath();
    return CruiseData.builder()
        .withUse(true)
        .withDelete(false)
        .withPackageDirectory(destPath == null ? null : destPath.toString())
        .withMetadataAuthor(personDatastore.findByName(importModel.getMetadataAuthor().getValue()).map(
            person -> MetadataAuthor.builder()
                .withUuid(person.getUuid())
                .withName(person.getName())
                .withEmail(person.getEmail())
                .withPhone(person.getPhone())
                .build()
        ).orElse(null))
        .withShip(row.getShipName())
        .withCruiseId(row.getCruiseID())
        .withSegmentId(row.getLeg())
        .withScientists(
            personDatastore.findByName(row.getChiefScientist())
                .map(p -> Collections.singletonList(
                    PeopleOrg.builder()
                        .withUuid(p.getUuid())
                        .withName(p.getName())
                        .build()
                ))
                .orElse(null)
        )
        .withSponsors(
            organizationDatastore.findByName(row.getSponsorOrganization())
                .map(o -> Collections.singletonList(
                    PeopleOrg.builder()
                        .withUuid(o.getUuid())
                        .withName(o.getName())
                        .build()
                ))
                .orElse(null)
        )
        .withFunders(
            organizationDatastore.findByName(row.getFundingOrganization())
                .map(o -> Collections.singletonList(
                    PeopleOrg.builder()
                        .withUuid(o.getUuid())
                        .withName(o.getName())
                        .build()
                ))
                .orElse(null)
        )
        .withDeparturePort(row.getDeparturePort())
        .withDepartureDate(row.getStartDate())
        .withArrivalPort(row.getArrivalPort())
        .withArrivalDate(row.getEndDate())
        .withSeaArea(row.getSeaArea())
        .withProjects(Collections.singletonList(
            row.getProjectName()
        )).withCruiseTitle(row.getCruiseTitle())
        .withCruisePurpose(row.getCruisePurpose())
        .withInstruments(getInstrumentsJson(row))
        // .withComments() TODO
        .build();
  }
  
  private List<InstrumentData> getInstrumentsJson(ImportRow row) {
    List<InstrumentData> instruments = new ArrayList<>();
    
    instruments.addAll(getGroupInstruments(
        row.getADCPInstruments(),
        InstrumentGroupName.ADCP
    ));
    instruments.addAll(getGroupInstruments(
       row.getCTDInstruments(),
       InstrumentGroupName.CTD 
    ));
    instruments.addAll(getGroupInstruments(
        row.getMBESInstruments(),
        InstrumentGroupName.MULTIBEAM
    ));
    instruments.addAll(getGroupInstruments(
        row.getSBESInstruments(),
        InstrumentGroupName.SINGLE_BEAM
    ));
    instruments.addAll(getGroupInstruments(
        row.getWaterColumnInstruments(),
        InstrumentGroupName.WATER_COLUMN
    ));
    
    
    return instruments;
  }
  
  private List<InstrumentData> getGroupInstruments(List<String> instrumentNames, InstrumentGroupName groupName) {
    return instrumentNames.stream()
        .map(instrumentName ->
            instrumentDatastore.getInstrument(new InstrumentDetailPackageKey(
                groupName.getShortName(),
                instrumentName
        ))).filter(Optional::isPresent)
        .map(Optional::get)
        .map(instrument -> InstrumentData.builder()
            .withUuid(instrument.getUuid())
            .withType(groupName.getLongName())
            .withInstrument(instrument.getName())
            .withShortName(instrument.getShortName())
            .build())
        .toList();
  }

  private List<InstrumentMetadata> getInstrumentsJson(PackJob packJob) {
    List<InstrumentMetadata> instruments = new ArrayList<>();
    for (Entry<InstrumentDetailPackageKey, List<InstrumentDetail>> entry : packJob.getInstruments().entrySet()) {
      for (InstrumentDetail instrumentDetail : entry.getValue()) {
        instruments.add(instrumentDetailToJson(entry.getKey(), instrumentDetail).toInstrumentMetadata());
      }
    }
    return instruments;
  }
  
  private List<InstrumentData> getInstrumentDataJson(PackJob packJob) {
    List<InstrumentData> instruments = new ArrayList<>();
    for (Entry<InstrumentDetailPackageKey, List<InstrumentDetail>> entry : packJob.getInstruments().entrySet()) {
      for (InstrumentDetail instrumentDetail : entry.getValue()) {
        instruments.add(InstrumentData.builder(instrumentDetailToJson(entry.getKey(), instrumentDetail))
                .withDataPath(instrumentDetail.getDataPath() == null ? null : Paths.get(instrumentDetail.getDataPath()))
                .withAncillaryDataPath(instrumentDetail.getAncillaryDataPath() == null ? null : Paths.get(instrumentDetail.getAncillaryDataPath()))
            .build());
      }
    }
    return instruments;
  }

  private InstrumentData instrumentDetailToJson(InstrumentDetailPackageKey key, InstrumentDetail instrumentDetail) {

    InstrumentData.Builder builder = InstrumentData.builder()
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
