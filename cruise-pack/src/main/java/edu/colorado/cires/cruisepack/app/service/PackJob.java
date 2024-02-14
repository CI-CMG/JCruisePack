package edu.colorado.cires.cruisepack.app.service;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackJob {

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(PackJob src) {
    return new Builder(src);
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

  private PackJob(String cruiseId, String segment, String seaUuid, String arrivalPortUuid, String departurePortUuid, String shipUuid,
      LocalDate departureDate, LocalDate arrivalDate, LocalDate releaseDate, Path packageDirectory, String cruiseTitle, String cruisePurpose,
      String cruiseDescription, Path documentsPath, boolean omicsSamplingConducted, String omicsContactUuid, Path omicsSampleTrackingSheetPath,
      String omicsBioProjectAccession, List<String> omicsSamplingTypes, List<String> omicsExpectedAnalyses, String omicsAdditionalSamplingInformation,
      String packageId) {
    this.cruiseId = cruiseId;
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
    private List<String> omicsSamplingTypes;
    private List<String> omicsExpectedAnalyses;
    private String omicsAdditionalSamplingInformation;
    private String packageId;

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
        omicsSamplingTypes = new ArrayList<>(0);
      }
      this.omicsSamplingTypes = Collections.unmodifiableList(omicsSamplingTypes);
      return this;
    }

    public Builder setOmicsExpectedAnalyses(List<String> omicsExpectedAnalyses) {
      if (omicsExpectedAnalyses == null) {
        omicsExpectedAnalyses = new ArrayList<>(0);
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

    public PackJob build() {
      return new PackJob(
          cruiseId, segment, seaUuid, arrivalPortUuid, departurePortUuid, shipUuid,
          departureDate, arrivalDate, releaseDate, packageDirectory, cruiseTitle, cruisePurpose, cruiseDescription, documentsPath,
          omicsSamplingConducted, omicsContactUuid, omicsSampleTrackingSheetPath, omicsBioProjectAccession, omicsSamplingTypes, omicsExpectedAnalyses,
          omicsAdditionalSamplingInformation, packageId);
    }
  }

//  private String packageId;
//  private String masterBagName;
//  private Path bagPath;
//  private Map<String, List<InstrumentDetail>> instruments;
//  private Path docsDir;
//  private Path omicsFile;
//  private Path cruisePackDataDir;
//  private CruiseMetadata cruiseMetadata;


//  public Instant getStartTime() {
//    return startTime;
//  }
//
//  public void setStartTime(Instant startTime) {
//    this.startTime = startTime;
//  }
//
//  public String getPackageId() {
//    return packageId;
//  }
//
//  public void setPackageId(String packageId) {
//    this.packageId = packageId;
//  }
//
//  public String getMasterBagName() {
//    return masterBagName;
//  }
//
//  public void setMasterBagName(String masterBagName) {
//    this.masterBagName = masterBagName;
//  }
//
//  public Path getBagPath() {
//    return bagPath;
//  }
//
//  public void setBagPath(Path bagPath) {
//    this.bagPath = bagPath;
//  }
//
//  public Map<String, List<InstrumentDetail>> getInstruments() {
//    return instruments;
//  }
//
//  public void setInstruments(Map<String, List<InstrumentDetail>> instruments) {
//    this.instruments = instruments;
//  }
//
//  public Path getDocsDir() {
//    return docsDir;
//  }
//
//  public void setDocsDir(Path docsDir) {
//    this.docsDir = docsDir;
//  }
//
//  public Path getOmicsFile() {
//    return omicsFile;
//  }
//
//  public void setOmicsFile(Path omicsFile) {
//    this.omicsFile = omicsFile;
//  }
//
//  public Path getCruisePackDataDir() {
//    return cruisePackDataDir;
//  }
//
//  public void setCruisePackDataDir(Path cruisePackDataDir) {
//    this.cruisePackDataDir = cruisePackDataDir;
//  }
//
//  public CruiseMetadata getCruiseMetadata() {
//    return cruiseMetadata;
//  }
//
//  public void setCruiseMetadata(CruiseMetadata cruiseMetadata) {
//    this.cruiseMetadata = cruiseMetadata;
//  }


}
