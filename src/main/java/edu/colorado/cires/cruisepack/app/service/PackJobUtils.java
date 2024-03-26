package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.service.metadata.ExpectedAnalyses;
import edu.colorado.cires.cruisepack.app.service.metadata.InstrumentData;
import edu.colorado.cires.cruisepack.app.service.metadata.Omics;
import edu.colorado.cires.cruisepack.app.service.metadata.OmicsData;
import edu.colorado.cires.cruisepack.app.service.metadata.OmicsPoc;
import edu.colorado.cires.cruisepack.app.service.metadata.PeopleOrg;
import edu.colorado.cires.cruisepack.app.service.metadata.SamplingTypes;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.model.DatasetsModel;
import edu.colorado.cires.cruisepack.app.ui.model.DropDownItemModel;
import edu.colorado.cires.cruisepack.app.ui.model.ExpectedAnalysesModel;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.model.SamplingTypesModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import edu.colorado.cires.cruisepack.xml.instrument.FileExtensionList;
import edu.colorado.cires.cruisepack.xml.instrument.Instrument;
import edu.colorado.cires.cruisepack.xml.person.Person;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

public final class PackJobUtils {

  private static String resolveDropDownItemUuid(DropDownItem ddi) {
    if (ddi == null || ddi.getId().isEmpty()) {
      return null;
    }
    return ddi.getId();
  }

  public static String resolvePackageId(PackageModel packageModel) {
    return resolvePackageId(packageModel.getCruiseId(), packageModel.getSegment());
  }
  
  public static String resolvePackageId(String cruiseId, String segment) {
    if (cruiseId != null) {
      String packageId = cruiseId;
      if (segment != null) {
        packageId = packageId + "_" + segment;
      }
      return packageId;
    }
    return null;
  }

  private static Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> getDirNames(DatasetsModel datasetsModel, String packageId, InstrumentDatastore instrumentDatastore) {
    Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> namers = new LinkedHashMap<>();
    for (BaseDatasetInstrumentModel<?> instrumentModel : datasetsModel.getDatasets()) {
      instrumentModel.getPackageKey().ifPresent(key -> instrumentDatastore.getInstrument(key).flatMap(instrumentModel::getInstrumentNameHolder).ifPresent(nameHolder -> {
        List<InstrumentNameHolder> holders = namers.computeIfAbsent(key, k -> new ArrayList<>());
        holders.add(nameHolder);
      }));
    }
    DatasetNameResolver.setDirNamesOnInstruments(packageId, namers);
    return namers;
  }

  private static Map<InstrumentDetailPackageKey, List<InstrumentDetail>> getInstruments(DatasetsModel datasetsModel, InstrumentDatastore instrumentDatastore, String packageId) {
    Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> namers = getDirNames(datasetsModel, packageId, instrumentDatastore);
    Map<InstrumentDetailPackageKey, List<InstrumentDetail>> map = new LinkedHashMap<>();
    for (Entry<InstrumentDetailPackageKey, List<InstrumentNameHolder>> entry : namers.entrySet()) {
      InstrumentDetailPackageKey pkg = entry.getKey();
      List<InstrumentDetail> instrumentDetails = new ArrayList<>(entry.getValue().size());
      for (InstrumentNameHolder nameHolder : entry.getValue()) {
        Instrument instrument = instrumentDatastore.getInstrument(pkg)
            .orElseThrow(() -> new IllegalStateException("Unable to find instrument " + pkg));
        Set<String> exts = new LinkedHashSet<>();
        FileExtensionList fileExtensionList = instrument.getFileExtensions();
        if (fileExtensionList != null) {
          List<String> extList = fileExtensionList.getFileExtensions();
          if (extList != null) {
            exts.addAll(extList);
          }
        }

        InstrumentDetail.Builder builder = InstrumentDetail.builder()
            .setUuid(nameHolder.getUuid())
            .setStatus(nameHolder.getStatus())
            .setInstrument(nameHolder.getInstrument())
            .setShortName(nameHolder.getShortName())
            .setExtensions(exts)
            .setDataPath(nameHolder.getDataPath() == null ? null : nameHolder.getDataPath().toString())
            .setFlatten(instrument.isFlatten())
            .setDirName(nameHolder.getDirName())
            .setBagName(nameHolder.getBagName())
            .setAdditionalFiles(nameHolder.getAdditionalFiles())
            .setReleaseDate(nameHolder.getReleaseDate())
            .setDataComment(nameHolder.getDataComment())
            .setAncillaryDataPath(nameHolder.getAncillaryDataPath() == null ? null : nameHolder.getAncillaryDataPath().toString())
            .setAncillaryDataDetails(nameHolder.getAncillaryDataDetails());

        if (nameHolder.getAdditionalFields() != null) {
          for (Entry<String, Object> e : nameHolder.getAdditionalFields().entrySet()) {
            builder = builder.setAdditionalField(e);
          }
        }

        instrumentDetails.add(
            builder.build()
        );

      }
      map.put(pkg, Collections.unmodifiableList(instrumentDetails));
    }
    return map;
  }

  public static PackJob create(CruiseData cruiseData, SeaDatastore seaDatastore, PortDatastore portDatastore, PersonDatastore personDatastore, InstrumentDatastore instrumentDatastore) {
    PackJob.Builder builder = PackJob.builder()
        .setCruiseId(cruiseData.getCruiseId())
        .setSegment(cruiseData.getSegmentId())
        .setSeaUuid(seaDatastore.getSeaUuidForName(cruiseData.getSeaArea()))
        .setArrivalPortUuid(portDatastore.getPortUuidForName(cruiseData.getArrivalPort()))
        .setDeparturePortUuid(portDatastore.getPortUuidForName(cruiseData.getDeparturePort()))
        .setShipUuid(cruiseData.getShipUuid())
        .setDepartureDate(cruiseData.getDepartureDate() == null ? null : LocalDate.parse(cruiseData.getDepartureDate()))
        .setArrivalDate(cruiseData.getArrivalDate() == null ? null : LocalDate.parse(cruiseData.getArrivalDate()))
        .setProjects(cruiseData.getProjects())
        .setReleaseDate(cruiseData.getMasterReleaseDate() == null ? null : LocalDate.parse(cruiseData.getMasterReleaseDate()))
        .setPackageDirectory(cruiseData.getPackageDirectory() == null ? null : Paths.get(cruiseData.getPackageDirectory()))
        .setScientists(cruiseData.getScientists())
        .setFunders(cruiseData.getFunders())
        .setSources(cruiseData.getSponsors())
        .setMetadataAuthor(cruiseData.getMetadataAuthor() == null ? null : personDatastore.findByName(cruiseData.getMetadataAuthor().getName()).orElse(null))
        .setCruiseTitle(cruiseData.getCruiseTitle())
        .setCruisePurpose(cruiseData.getCruisePurpose())
        .setCruiseDescription(cruiseData.getCruiseDescription())
        .setDocumentsPath(cruiseData.getDocumentsPath() == null ? null : Paths.get(cruiseData.getDocumentsPath()))
        .setPackageId(cruiseData.getPackageId())
        .setInstruments(resolveInstruments(cruiseData.getInstruments(), instrumentDatastore));

    builder = setOmicsFields(cruiseData, builder);

    return builder.build();
  }

  private static Map<InstrumentDetailPackageKey, List<InstrumentDetail>> resolveInstruments(List<edu.colorado.cires.cruisepack.app.service.metadata.Instrument> instruments, InstrumentDatastore instrumentDatastore) {
    Map<InstrumentDetailPackageKey, List<InstrumentDetail>> map = new HashMap<>(0);
    instruments.forEach(instrument -> {
      InstrumentDetailPackageKey key = new InstrumentDetailPackageKey(
          InstrumentGroupName.fromLongName(instrument.getType()).getShortName(),
          instrument.getInstrument()
      );

      List<InstrumentDetail> instrumentDetails = map.get(key);
      if (instrumentDetails == null) {
        instrumentDetails = new ArrayList<>(0);
      }

      Optional<Instrument> xmlInstrument = instrumentDatastore.getInstrument(key);
      if (xmlInstrument.isPresent()) {
        instrumentDetails.add(instrumentDetailFromInstrument(
            instrument,
            xmlInstrument.get()
        ));
        map.put(key, instrumentDetails);
      }

    });

    return map;
  }

  private static InstrumentDetail instrumentDetailFromInstrument(edu.colorado.cires.cruisepack.app.service.metadata.Instrument instrument, Instrument xmlInstrument) {
    InstrumentDetail.Builder builder = InstrumentDetail.builder()
        .setStatus(InstrumentStatus.forValue(instrument.getStatus()))
        .setUuid(instrument.getUuid())
        .setReleaseDate(instrument.getReleaseDate() == null ? null : LocalDate.parse(instrument.getReleaseDate()))
        .setInstrument(instrument.getInstrument())
        .setShortName(instrument.getShortName())
        .setExtensions(
            xmlInstrument.getFileExtensions() == null ? null : new HashSet<>(xmlInstrument.getFileExtensions().getFileExtensions())
        )
        .setFlatten(xmlInstrument.isFlatten())
        .setDirName(instrument.getDirName())
        .setBagName(instrument.getBagName())
        .setDataComment(instrument.getDataComment())
        .setAncillaryDataDetails(instrument.getAncillaryDataDetails())
        .setAdditionalFiles(Collections.emptyList());

    for (Entry<String, Object> entry : instrument.getOtherFields().entrySet()) {
      builder = builder.setAdditionalField(entry);
    }

    if (instrument instanceof InstrumentData) {
      Path dataPath = ((InstrumentData) instrument).getDataPath();
      Path ancillaryPath = ((InstrumentData) instrument).getAncillaryDataPath();
      builder = builder.setDataPath(dataPath == null ? null : dataPath.toString())
          .setAncillaryDataPath(ancillaryPath == null ? null : ancillaryPath.toString());
    }

    return builder.build();
  }

  private static PackJob.Builder setOmicsFields(CruiseData cruiseData, PackJob.Builder builder) {
    Omics omics = cruiseData.getOmics();
    if (omics != null) {
      if (omics instanceof OmicsData) {
        builder = builder.setOmicsSamplingConducted(((OmicsData) omics).samplingConducted())
            .setOmicsSampleTrackingSheetPath(((OmicsData) omics).trackingPath());
      }

      OmicsPoc omicsPoc = omics.omicsPoc();
      if (omicsPoc != null) {
        builder = builder.setOmicsContactUuid(omicsPoc.getUuid())
            .setOmicsContactName(omicsPoc.getName())
            .setOmicsContactEmail(omicsPoc.getEmail())
            .setOmicsContactPhone(omicsPoc.getPhone());
      }

      builder = builder.setOmicsBioProjectAccession(omics.ncbiAccession())
          .setOmicsSamplingTypes(omics.samplingTypes())
          .setOmicsExpectedAnalyses(omics.analysesTypes())
          .setOmicsAdditionalSamplingInformation(omics.omicsComment());
    }
    return builder;
  }

  public static PackJob create(PackageModel packageModel, PeopleModel peopleModel, OmicsModel omicsModel, CruiseInformationModel cruiseInformationModel, DatasetsModel datasetsModel, InstrumentDatastore instrumentDatastore, PersonDatastore personDatastore) {
    String packageId = resolvePackageId(packageModel);
    Person omicsContact = personDatastore.getByUUID(omicsModel.getContact().getId()).orElse(null);

    List<String> omicsSamplingTypes = new ArrayList<>(0);
    SamplingTypesModel samplingTypesModel = omicsModel.getSamplingTypes();
    if (samplingTypesModel != null) {
      if (samplingTypesModel.isWater()) {
        omicsSamplingTypes.add(SamplingTypes.WATER.getName());
      }

      if (samplingTypesModel.isSoilSediment()) {
        omicsSamplingTypes.add(SamplingTypes.SOIL_SEDIMENT.getName());
      }

      if (samplingTypesModel.isOrganicTissue()) {
        omicsSamplingTypes.add(SamplingTypes.ORGANIC_TISSUE.getName());
      }
    }

    List<String> omicsExpectedAnalyses = new ArrayList<>(0);
    ExpectedAnalysesModel expectedAnalysesModel = omicsModel.getExpectedAnalyses();
    if (expectedAnalysesModel != null) {
      if (expectedAnalysesModel.isBarcoding()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.BARCODING.getName());
      }

      if (expectedAnalysesModel.isGenomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.GENOMICS.getName());
      }

      if (expectedAnalysesModel.isTranscriptomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.TRANSCRIPOMICS.getName());
      }

      if (expectedAnalysesModel.isProteomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.PROTEOMICS.getName());
      }

      if (expectedAnalysesModel.isMetabolomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.METABOLOMICS.getName());
      }

      if (expectedAnalysesModel.isEpigenetics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.EPIGENETICS.getName());
      }

      if (expectedAnalysesModel.isOther()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.OTHER.getName());
      }

      if (expectedAnalysesModel.isMetabarcoding()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.METABARCODING.getName());
      }

      if (expectedAnalysesModel.isMetagenomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.METAGENOMICS.getName());
      }

      if (expectedAnalysesModel.isMetatranscriptomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.METATRANSCRIPTOMICS.getName());
      }

      if (expectedAnalysesModel.isMetaproteomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.METAPROTEOMICS.getName());
      }

      if (expectedAnalysesModel.isMetametabolomics()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.METAMETABOLOMICS.getName());
      }

      if (expectedAnalysesModel.isMicrobiome()) {
        omicsExpectedAnalyses.add(ExpectedAnalyses.MICROBIOME.getName());
      }
    }

    return PackJob.builder()
        .setCruiseId(packageModel.getCruiseId())
        .setSegment(packageModel.getSegment())
        .setSeaUuid(resolveDropDownItemUuid(packageModel.getSea()))
        .setArrivalPortUuid(resolveDropDownItemUuid(packageModel.getArrivalPort()))
        .setDeparturePortUuid(resolveDropDownItemUuid(packageModel.getDeparturePort()))
        .setShipUuid(resolveDropDownItemUuid(packageModel.getShip()))
        .setDepartureDate(packageModel.getDepartureDate())
        .setArrivalDate(packageModel.getArrivalDate())
        .setProjects(packageModel.getProjects().stream().map(DropDownItemModel::getItem).map(DropDownItem::getValue).toList())
        .setReleaseDate(packageModel.getReleaseDate())
        .setPackageDirectory(packageModel.getPackageDirectory())
        .setCruiseTitle(cruiseInformationModel.getCruiseTitle())
        .setCruisePurpose(cruiseInformationModel.getCruisePurpose())
        .setCruiseDescription(cruiseInformationModel.getCruiseDescription())
        .setDocumentsPath(datasetsModel.getDocumentsPath())
        .setOmicsSamplingConducted(omicsModel.isSamplingConducted())
        .setOmicsContactUuid(omicsContact != null ? omicsContact.getUuid() : null)
        .setOmicsContactName(omicsContact != null ? omicsContact.getName() : null)
        .setOmicsContactEmail(omicsContact != null ? omicsContact.getEmail() : null)
        .setOmicsContactPhone(omicsContact != null ? omicsContact.getPhone() : null)
        .setOmicsSampleTrackingSheetPath(omicsModel.getSampleTrackingSheet())
        .setOmicsBioProjectAccession(omicsModel.getBioProjectAccession())
        .setOmicsSamplingTypes(omicsSamplingTypes)
        .setOmicsExpectedAnalyses(omicsExpectedAnalyses)
        .setOmicsAdditionalSamplingInformation(omicsModel.getAdditionalSamplingInformation())
        .setPackageId(packageId)
        .setInstruments(getInstruments(datasetsModel, instrumentDatastore, packageId))
        .setScientists(peopleModel.getScientists().stream().map(
            (dd) -> PeopleOrg.builder()
                .withUuid(dd.getItem().getId())
                .withName(dd.getItem().getValue())
                .build()).toList())
        .setFunders(peopleModel.getFundingOrganizations().stream().map(
            (dd) -> PeopleOrg.builder()
                .withUuid(dd.getItem().getId())
                .withName(dd.getItem().getValue())
                .build()).toList())
        .setSources(peopleModel.getSourceOrganizations().stream().map(
            (dd) -> PeopleOrg.builder()
                .withUuid(dd.getItem().getId())
                .withName(dd.getItem().getValue())
                .build()).toList())
        .setMetadataAuthor(peopleModel.getMetadataAuthor() == null ? null : personDatastore.getByUUID(peopleModel.getMetadataAuthor().getId()).orElse(null))
        .build();
  }

}
