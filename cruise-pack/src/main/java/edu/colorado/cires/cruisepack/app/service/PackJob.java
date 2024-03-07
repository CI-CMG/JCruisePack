package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.ExpectedAnalyses;
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
import edu.colorado.cires.cruisepack.app.ui.model.validation.EnoughDiskSpace;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.instrument.FileExtensionList;
import edu.colorado.cires.cruisepack.xml.instrument.Instrument;
import edu.colorado.cires.cruisepack.xml.person.Person;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

@EnoughDiskSpace
public class PackJob {

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(PackJob src) {
    return new Builder(src);
  }

  private static String resolveDropDownItemUuid(DropDownItem ddi) {
    if (ddi == null || ddi.getId().isEmpty()) {
      return null;
    }
    return ddi.getId();
  }

  public static String resolvePackageId(PackageModel packageModel) {
    String packageId = packageModel.getCruiseId();
    if (packageModel.getSegment() != null) {
      packageId = packageId + "_" + packageModel.getSegment();
    }
    return packageId;
  }

  private static Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> getDirNames(DatasetsModel datasetsModel, String packageId, InstrumentDatastore instrumentDatastore) {
    Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> namers = new LinkedHashMap<>();
    for (BaseDatasetInstrumentModel<?> instrumentModel : datasetsModel.getDatasets()) {
      instrumentModel.getPackageKey().ifPresent(key -> {
        instrumentDatastore.getInstrument(key).flatMap(instrumentModel::getInstrumentNameHolder).ifPresent(nameHolder -> {
          List<InstrumentNameHolder> holders = namers.computeIfAbsent(key, k -> new ArrayList<>());
          holders.add(nameHolder);
        });
      });
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
            .setDataPath(nameHolder.getDataPath())
            .setFlatten(instrument.isFlatten())
            .setDirName(nameHolder.getDirName())
            .setBagName(nameHolder.getBagName())
            .setAdditionalFiles(nameHolder.getAdditionalFiles())
            .setReleaseDate(nameHolder.getReleaseDate())
            .setDataComment(nameHolder.getDataComment())
            .setAncillaryDataPath(nameHolder.getAncillaryDataPath())
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
        .setDocumentsPath(cruiseInformationModel.getDocumentsPath())
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

  private final String cruiseId;
  private final String segment;
  private final String seaUuid;
  private final String arrivalPortUuid;
  private final String departurePortUuid;
  private final String shipUuid;
  private final LocalDate departureDate;
  private final LocalDate arrivalDate;
  private final List<String> projects;
  private final LocalDate releaseDate;
  private final Path packageDirectory;
  
  private final List<PeopleOrg> scientists;
  private final List<PeopleOrg> funders;
  private final List<PeopleOrg> sources;
  private final Person metadataAuthor;

  private final String cruiseTitle;
  private final String cruisePurpose;
  private final String cruiseDescription;
  private final Path documentsPath;

  private final boolean omicsSamplingConducted;
  private final String omicsContactUuid;
  private final String omicsContactName;
  private final String omicsContactEmail;
  private final String omicsContactPhone;
  private final Path omicsSampleTrackingSheetPath;
  private final String omicsBioProjectAccession;
  private final List<String> omicsSamplingTypes;
  private final List<String> omicsExpectedAnalyses;
  private final String omicsAdditionalSamplingInformation;

  private final String packageId;

  private final Map<InstrumentDetailPackageKey, List<InstrumentDetail>> instruments;

  private PackJob(String cruiseId, String segment, String seaUuid, String arrivalPortUuid, String departurePortUuid, String shipUuid,
      LocalDate departureDate, LocalDate arrivalDate, List<String> projects, LocalDate releaseDate, Path packageDirectory, String cruiseTitle, String cruisePurpose,
      String cruiseDescription, Path documentsPath, boolean omicsSamplingConducted, String omicsContactUuid, String omicsContactName, String omicsContactEmail, String omicsContactPhone,
       Path omicsSampleTrackingSheetPath, String omicsBioProjectAccession, List<String> omicsSamplingTypes, List<String> omicsExpectedAnalyses, String omicsAdditionalSamplingInformation,
      String packageId, Map<InstrumentDetailPackageKey, List<InstrumentDetail>> instruments, List<PeopleOrg> scientists, List<PeopleOrg> funders, List<PeopleOrg> sources, Person metadataAuthor) {
    this.cruiseId = Objects.requireNonNull(cruiseId, "cruiseId must not be null");
    this.segment = segment;
    this.seaUuid = seaUuid;
    this.arrivalPortUuid = arrivalPortUuid;
    this.departurePortUuid = departurePortUuid;
    this.shipUuid = shipUuid;
    this.departureDate = departureDate;
    this.arrivalDate = arrivalDate;
    this.projects = projects;
    this.releaseDate = releaseDate;
    this.packageDirectory = packageDirectory;
    this.cruiseTitle = cruiseTitle;
    this.cruisePurpose = cruisePurpose;
    this.cruiseDescription = cruiseDescription;
    this.documentsPath = documentsPath;
    this.omicsSamplingConducted = omicsSamplingConducted;
    this.omicsContactUuid = omicsContactUuid;
    this.omicsContactName = omicsContactName;
    this.omicsContactEmail = omicsContactEmail;
    this.omicsContactPhone = omicsContactPhone;
    this.omicsSampleTrackingSheetPath = omicsSampleTrackingSheetPath;
    this.omicsBioProjectAccession = omicsBioProjectAccession;
    this.omicsSamplingTypes = omicsSamplingTypes;
    this.omicsExpectedAnalyses = omicsExpectedAnalyses;
    this.omicsAdditionalSamplingInformation = omicsAdditionalSamplingInformation;
    this.packageId = packageId;
    this.instruments = instruments;
    this.scientists = scientists;
    this.funders = funders;
    this.sources = sources;
    this.metadataAuthor = metadataAuthor;
  }

  public String getCruiseId() {
    return cruiseId;
  }

  public String getSegment() {
    return segment;
  }

  public String getSeaUuid() {
    return seaUuid;
  }

  public String getArrivalPortUuid() {
    return arrivalPortUuid;
  }

  public String getDeparturePortUuid() {
    return departurePortUuid;
  }

  public String getShipUuid() {
    return shipUuid;
  }

  public LocalDate getDepartureDate() {
    return departureDate;
  }

  public LocalDate getArrivalDate() {
    return arrivalDate;
  }

  public List<String> getProjects() {
    return projects;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  public Path getPackageDirectory() {
    return packageDirectory;
  }

  public String getCruiseTitle() {
    return cruiseTitle;
  }

  public String getCruisePurpose() {
    return cruisePurpose;
  }

  public String getCruiseDescription() {
    return cruiseDescription;
  }

  public Path getDocumentsPath() {
    return documentsPath;
  }

  public boolean isOmicsSamplingConducted() {
    return omicsSamplingConducted;
  }

  public String getOmicsContactUuid() {
    return omicsContactUuid;
  }

  public String getOmicsContactName() {
    return omicsContactName;
  }

  public String getOmicsContactEmail() {
    return omicsContactEmail;
  }

  public String getOmicsContactPhone() {
    return omicsContactPhone;
  }

  public Path getOmicsSampleTrackingSheetPath() {
    return omicsSampleTrackingSheetPath;
  }

  public String getOmicsBioProjectAccession() {
    return omicsBioProjectAccession;
  }

  public List<String> getOmicsSamplingTypes() {
    return omicsSamplingTypes;
  }

  public List<String> getOmicsExpectedAnalyses() {
    return omicsExpectedAnalyses;
  }

  public String getOmicsAdditionalSamplingInformation() {
    return omicsAdditionalSamplingInformation;
  }

  public String getPackageId() {
    return packageId;
  }

  public Map<InstrumentDetailPackageKey, List<InstrumentDetail>> getInstruments() {
    return instruments;
  }

  public List<PeopleOrg> getScientists() {
    return scientists;
  }

  public List<PeopleOrg> getFunders() {
    return funders;
  }

  public List<PeopleOrg> getSources() {
    return sources;
  }

  public Person getMetadataAuthor() {
    return metadataAuthor;
  }

  public static class Builder {

    private String cruiseId;
    private String segment;
    private String seaUuid;
    private String arrivalPortUuid;
    private String departurePortUuid;
    private String shipUuid;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private List<String> projects = Collections.emptyList();
    private LocalDate releaseDate;
    private Path packageDirectory;
    private String cruiseTitle;
    private String cruisePurpose;
    private String cruiseDescription;
    private Path documentsPath;
    private boolean omicsSamplingConducted;
    private String omicsContactUuid;
    private String omicsContactName;
    private String omicsContactEmail;
    private String omicsContactPhone;
    private Path omicsSampleTrackingSheetPath;
    private String omicsBioProjectAccession;
    private List<String> omicsSamplingTypes = Collections.emptyList();
    private List<String> omicsExpectedAnalyses = Collections.emptyList();
    private String omicsAdditionalSamplingInformation;
    private String packageId;
    private Map<InstrumentDetailPackageKey, List<InstrumentDetail>> instruments = Collections.emptyMap();
    private List<PeopleOrg> scientists = Collections.emptyList();
    private List<PeopleOrg> funders = Collections.emptyList();
    private List<PeopleOrg> sources = Collections.emptyList();
    private Person metadataAuthor;

    private Builder() {

    }

    private Builder(PackJob src) {
      cruiseId = src.cruiseId;
      segment = src.segment;
      seaUuid = src.seaUuid;
      arrivalPortUuid = src.arrivalPortUuid;
      departurePortUuid = src.departurePortUuid;
      shipUuid = src.shipUuid;
      departureDate = src.departureDate;
      arrivalDate = src.arrivalDate;
      projects = src.projects;
      releaseDate = src.releaseDate;
      packageDirectory = src.packageDirectory;
      cruiseTitle = src.cruiseTitle;
      cruisePurpose = src.cruisePurpose;
      cruiseDescription = src.cruiseDescription;
      documentsPath = src.documentsPath;
      omicsSamplingConducted = src.omicsSamplingConducted;
      omicsContactUuid = src.omicsContactUuid;
      omicsContactName = src.omicsContactName;
      omicsContactEmail = src.omicsContactEmail;
      omicsContactPhone = src.omicsContactPhone;
      omicsSampleTrackingSheetPath = src.omicsSampleTrackingSheetPath;
      omicsBioProjectAccession = src.omicsBioProjectAccession;
      omicsSamplingTypes = src.omicsSamplingTypes;
      omicsExpectedAnalyses = src.omicsExpectedAnalyses;
      omicsAdditionalSamplingInformation = src.omicsAdditionalSamplingInformation;
      packageId = src.packageId;
      instruments = src.instruments;
      scientists = src.scientists;
      funders = src.funders;
      sources = src.sources;
      metadataAuthor = src.metadataAuthor;
    }

    public Builder setCruiseId(String cruiseId) {
      this.cruiseId = cruiseId;
      return this;
    }

    public Builder setSegment(String segment) {
      this.segment = segment;
      return this;
    }

    public Builder setSeaUuid(String seaUuid) {
      this.seaUuid = seaUuid;
      return this;
    }

    public Builder setArrivalPortUuid(String arrivalPortUuid) {
      this.arrivalPortUuid = arrivalPortUuid;
      return this;
    }

    public Builder setDeparturePortUuid(String departurePortUuid) {
      this.departurePortUuid = departurePortUuid;
      return this;
    }

    public Builder setShipUuid(String shipUuid) {
      this.shipUuid = shipUuid;
      return this;
    }

    public Builder setDepartureDate(LocalDate departureDate) {
      this.departureDate = departureDate;
      return this;
    }

    public Builder setArrivalDate(LocalDate arrivalDate) {
      this.arrivalDate = arrivalDate;
      return this;
    }

    public Builder setProjects(List<String> projects) {
      this.projects = projects;
      return this;
    }

    public Builder setReleaseDate(LocalDate releaseDate) {
      this.releaseDate = releaseDate;
      return this;
    }

    public Builder setPackageDirectory(Path packageDirectory) {
      this.packageDirectory = packageDirectory;
      return this;
    }

    public Builder setCruiseTitle(String cruiseTitle) {
      this.cruiseTitle = cruiseTitle;
      return this;
    }

    public Builder setCruisePurpose(String cruisePurpose) {
      this.cruisePurpose = cruisePurpose;
      return this;
    }

    public Builder setCruiseDescription(String cruiseDescription) {
      this.cruiseDescription = cruiseDescription;
      return this;
    }

    public Builder setDocumentsPath(Path documentsPath) {
      this.documentsPath = documentsPath;
      return this;
    }

    public Builder setOmicsSamplingConducted(boolean omicsSamplingConducted) {
      this.omicsSamplingConducted = omicsSamplingConducted;
      return this;
    }

    public Builder setOmicsContactUuid(String omicsContactUuid) {
      this.omicsContactUuid = omicsContactUuid;
      return this;
    }

    public Builder setOmicsContactName(String omicsContactName) {
      this.omicsContactName = omicsContactName;
      return this;
    }

    public Builder setOmicsContactEmail(String omicsContactEmail) {
      this.omicsContactEmail = omicsContactEmail;
      return this;
    }

    public Builder setOmicsContactPhone(String omicsContactPhone) {
      this.omicsContactPhone = omicsContactPhone;
      return this;
    }

    public Builder setOmicsSampleTrackingSheetPath(Path omicsSampleTrackingSheetPath) {
      this.omicsSampleTrackingSheetPath = omicsSampleTrackingSheetPath;
      return this;
    }

    public Builder setOmicsBioProjectAccession(String omicsBioProjectAccession) {
      this.omicsBioProjectAccession = omicsBioProjectAccession;
      return this;
    }

    public Builder setOmicsSamplingTypes(List<String> omicsSamplingTypes) {
      if (omicsSamplingTypes == null) {
        omicsSamplingTypes = Collections.emptyList();
      }
      this.omicsSamplingTypes = Collections.unmodifiableList(omicsSamplingTypes);
      return this;
    }

    public Builder setOmicsExpectedAnalyses(List<String> omicsExpectedAnalyses) {
      if (omicsExpectedAnalyses == null) {
        omicsExpectedAnalyses = Collections.emptyList();
      }
      this.omicsExpectedAnalyses = Collections.unmodifiableList(omicsExpectedAnalyses);
      return this;
    }

    public Builder setOmicsAdditionalSamplingInformation(String omicsAdditionalSamplingInformation) {
      this.omicsAdditionalSamplingInformation = omicsAdditionalSamplingInformation;
      return this;
    }

    public Builder setPackageId(String packageId) {
      this.packageId = packageId;
      return this;
    }

    public Builder setInstruments(Map<InstrumentDetailPackageKey, List<InstrumentDetail>> instruments) {
      if (instruments == null) {
        instruments = Collections.emptyMap();
      }
      Map<InstrumentDetailPackageKey, List<InstrumentDetail>> map = new LinkedHashMap<>();
      for (Entry<InstrumentDetailPackageKey, List<InstrumentDetail>> entry : instruments.entrySet()) {
        if (!entry.getValue().isEmpty()) {
          map.put(entry.getKey(), Collections.unmodifiableList(new ArrayList<>(entry.getValue())));
        }
      }
      this.instruments = Collections.unmodifiableMap(map);
      return this;
    }

    public Builder setScientists(List<PeopleOrg> scientists) {
      this.scientists = scientists;
      return this;
    }
    
    public Builder setFunders(List<PeopleOrg> funders) {
      this.funders = funders;
      return this;
    }

    public Builder setSources(List<PeopleOrg> sources) {
      this.sources = sources;
      return this;
    }

    public Builder setMetadataAuthor(Person metadataAuthor) {
      this.metadataAuthor = metadataAuthor;
      return this;
    }

    public PackJob build() {
      return new PackJob(
          cruiseId, segment, seaUuid, arrivalPortUuid, departurePortUuid, shipUuid,
          departureDate, arrivalDate, projects, releaseDate, packageDirectory, cruiseTitle, cruisePurpose, cruiseDescription, documentsPath,
          omicsSamplingConducted, omicsContactUuid, omicsContactName, omicsContactEmail, omicsContactPhone, omicsSampleTrackingSheetPath, omicsBioProjectAccession, omicsSamplingTypes, omicsExpectedAnalyses,
          omicsAdditionalSamplingInformation, packageId, instruments, scientists, funders, sources, metadataAuthor);
    }
  }
}
