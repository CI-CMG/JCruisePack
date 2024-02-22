package edu.colorado.cires.cruisepack.app.service;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.Set;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.model.DatasetsModel;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.instrument.FileExtensionList;
import edu.colorado.cires.cruisepack.xml.instrument.Instrument;

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

  private static Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> getDirNames(DatasetsModel datasetsModel, String packageId) {
    Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> namers = new LinkedHashMap<>();
    for (BaseDatasetInstrumentModel instrumentModel : datasetsModel.getDatasets()) {
      instrumentModel.getPackageKey().ifPresent(key -> {
        instrumentModel.getInstrumentNameHolder().ifPresent(nameHolder -> {
          List<InstrumentNameHolder> holders = namers.get(key);
          if (holders == null) {
            holders = new ArrayList<>();
            namers.put(key, holders);
          }
          holders.add(nameHolder);
        });
      });
    }
    DatasetNameResolver.setDirNamesOnInstruments(packageId, namers);
    return namers;
  }

  private static Map<InstrumentDetailPackageKey, List<InstrumentDetail>> getInstruments(DatasetsModel datasetsModel, InstrumentDatastore instrumentDatastore, String packageId) {
    Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> namers = getDirNames(datasetsModel, packageId);
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

        instrumentDetails.add(
            InstrumentDetail.builder()
                .setStatus(nameHolder.getStatus())
                .setInstrument(nameHolder.getInstrument())
                .setShortName(nameHolder.getShortName())
                .setExtensions(exts)
                .setDataPath(nameHolder.getDataPath())
                .setFlatten(instrument.isFlatten())
                .setDirName(nameHolder.getDirName())
                .setBagName(nameHolder.getBagName())
                .setAdditionalFiles(nameHolder.getAdditionalFiles())
                .build());

      }
      map.put(pkg, Collections.unmodifiableList(instrumentDetails));
    }
    return map;
  }

  public static PackJob create(PackageModel packageModel, OmicsModel omicsModel, CruiseInformationModel cruiseInformationModel, DatasetsModel datasetsModel, InstrumentDatastore instrumentDatastore) {
    String packageId = resolvePackageId(packageModel);
    return PackJob.builder()
        .setCruiseId(packageModel.getCruiseId())
        .setSegment(packageModel.getSegment())
        .setSeaUuid(resolveDropDownItemUuid(packageModel.getSea()))
        .setArrivalPortUuid(resolveDropDownItemUuid(packageModel.getArrivalPort()))
        .setDeparturePortUuid(resolveDropDownItemUuid(packageModel.getDeparturePort()))
        .setShipUuid(resolveDropDownItemUuid(packageModel.getShip()))
        .setDepartureDate(packageModel.getDepartureDate())
        .setArrivalDate(packageModel.getArrivalDate())
        .setReleaseDate(packageModel.getReleaseDate())
        .setPackageDirectory(packageModel.getPackageDirectory())
        .setCruiseTitle(cruiseInformationModel.getCruiseTitle())
        .setCruisePurpose(cruiseInformationModel.getCruisePurpose())
        .setCruiseDescription(cruiseInformationModel.getCruiseDescription())
        .setDocumentsPath(cruiseInformationModel.getDocumentsPath())
        .setOmicsSamplingConducted(omicsModel.isSamplingConducted())
        .setOmicsContactUuid(resolveDropDownItemUuid(omicsModel.getContact()))
        .setOmicsSampleTrackingSheetPath(omicsModel.getSampleTrackingSheet())
        .setOmicsBioProjectAccession(omicsModel.getBioProjectAccession())
        //TODO
//        .setOmicsSamplingTypes(omicsModel.getSamplingTypes())
//        .setOmicsExpectedAnalyses(omicsModel.getExpectedAnalyses())
        .setOmicsAdditionalSamplingInformation(omicsModel.getAdditionalSamplingInformation())
        .setPackageId(packageId)
        .setInstruments(getInstruments(datasetsModel, instrumentDatastore, packageId))
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
  private final LocalDate releaseDate;
  private final Path packageDirectory;

  private final String cruiseTitle;
  private final String cruisePurpose;
  private final String cruiseDescription;
  private final Path documentsPath;

  private final boolean omicsSamplingConducted;
  private final String omicsContactUuid;
  private final Path omicsSampleTrackingSheetPath;
  private final String omicsBioProjectAccession;
  private final List<String> omicsSamplingTypes;
  private final List<String> omicsExpectedAnalyses;
  private final String omicsAdditionalSamplingInformation;

  private final String packageId;

  private final Map<InstrumentDetailPackageKey, List<InstrumentDetail>> instruments;

  private PackJob(String cruiseId, String segment, String seaUuid, String arrivalPortUuid, String departurePortUuid, String shipUuid,
      LocalDate departureDate, LocalDate arrivalDate, LocalDate releaseDate, Path packageDirectory, String cruiseTitle, String cruisePurpose,
      String cruiseDescription, Path documentsPath, boolean omicsSamplingConducted, String omicsContactUuid, Path omicsSampleTrackingSheetPath,
      String omicsBioProjectAccession, List<String> omicsSamplingTypes, List<String> omicsExpectedAnalyses, String omicsAdditionalSamplingInformation,
      String packageId, Map<InstrumentDetailPackageKey, List<InstrumentDetail>> instruments) {
    this.cruiseId = Objects.requireNonNull(cruiseId, "cruiseId must not be null");
    this.segment = segment;
    this.seaUuid = seaUuid;
    this.arrivalPortUuid = arrivalPortUuid;
    this.departurePortUuid = departurePortUuid;
    this.shipUuid = shipUuid;
    this.departureDate = departureDate;
    this.arrivalDate = arrivalDate;
    this.releaseDate = releaseDate;
    this.packageDirectory = packageDirectory;
    this.cruiseTitle = cruiseTitle;
    this.cruisePurpose = cruisePurpose;
    this.cruiseDescription = cruiseDescription;
    this.documentsPath = documentsPath;
    this.omicsSamplingConducted = omicsSamplingConducted;
    this.omicsContactUuid = omicsContactUuid;
    this.omicsSampleTrackingSheetPath = omicsSampleTrackingSheetPath;
    this.omicsBioProjectAccession = omicsBioProjectAccession;
    this.omicsSamplingTypes = omicsSamplingTypes;
    this.omicsExpectedAnalyses = omicsExpectedAnalyses;
    this.omicsAdditionalSamplingInformation = omicsAdditionalSamplingInformation;
    this.packageId = packageId;
    this.instruments = instruments;
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

  public static class Builder {

    private String cruiseId;
    private String segment;
    private String seaUuid;
    private String arrivalPortUuid;
    private String departurePortUuid;
    private String shipUuid;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private LocalDate releaseDate;
    private Path packageDirectory;
    private String cruiseTitle;
    private String cruisePurpose;
    private String cruiseDescription;
    private Path documentsPath;
    private boolean omicsSamplingConducted;
    private String omicsContactUuid;
    private Path omicsSampleTrackingSheetPath;
    private String omicsBioProjectAccession;
    private List<String> omicsSamplingTypes = Collections.emptyList();
    private List<String> omicsExpectedAnalyses = Collections.emptyList();
    private String omicsAdditionalSamplingInformation;
    private String packageId;
    private Map<InstrumentDetailPackageKey, List<InstrumentDetail>> instruments = Collections.emptyMap();

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
      releaseDate = src.releaseDate;
      packageDirectory = src.packageDirectory;
      cruiseTitle = src.cruiseTitle;
      cruisePurpose = src.cruisePurpose;
      cruiseDescription = src.cruiseDescription;
      documentsPath = src.documentsPath;
      omicsSamplingConducted = src.omicsSamplingConducted;
      omicsContactUuid = src.omicsContactUuid;
      omicsSampleTrackingSheetPath = src.omicsSampleTrackingSheetPath;
      omicsBioProjectAccession = src.omicsBioProjectAccession;
      omicsSamplingTypes = src.omicsSamplingTypes;
      omicsExpectedAnalyses = src.omicsExpectedAnalyses;
      omicsAdditionalSamplingInformation = src.omicsAdditionalSamplingInformation;
      packageId = src.packageId;
      instruments = src.instruments;
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

    public PackJob build() {
      return new PackJob(
          cruiseId, segment, seaUuid, arrivalPortUuid, departurePortUuid, shipUuid,
          departureDate, arrivalDate, releaseDate, packageDirectory, cruiseTitle, cruisePurpose, cruiseDescription, documentsPath,
          omicsSamplingConducted, omicsContactUuid, omicsSampleTrackingSheetPath, omicsBioProjectAccession, omicsSamplingTypes, omicsExpectedAnalyses,
          omicsAdditionalSamplingInformation, packageId, instruments);
    }
  }
}
