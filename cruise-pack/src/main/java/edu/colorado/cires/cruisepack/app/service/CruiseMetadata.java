package edu.colorado.cires.cruisepack.app.service;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// TODO make immutable, use builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CruiseMetadata {

  private String cruiseId;
  private String segmentId;
  private String packageId;
  private String masterReleaseDate;
  private String ship;
  private String shipUuid;
  private String departurePort;
  private String departureDate;
  private String arrivalPort;
  private String arrivalDate;
  private String seaArea;
  private String cruiseTitle;
  private String cruisePurpose;
  private String cruiseDescription;
  private List<PeopleOrg> sponsors = new ArrayList<>(0);
  private List<PeopleOrg> funders = new ArrayList<>(0);
  private List<PeopleOrg> scientists = new ArrayList<>(0);
  private List<String> projects = new ArrayList<>(0);
  private Omics omics;
  private MetadataAuthor metadataAuthor;
  private List<Instrument> instruments = new ArrayList<>(0);
  private Map<String, PackageInstrument> packageInstruments = new LinkedHashMap<>();

  public CruiseMetadata shallowCopy() {
    CruiseMetadata copy = new CruiseMetadata();
    copy.cruiseId = cruiseId;
    copy.segmentId = segmentId;
    copy.packageId = packageId;
    copy.masterReleaseDate = masterReleaseDate;
    copy.ship = ship;
    copy.shipUuid = shipUuid;
    copy.departurePort = departurePort;
    copy.departureDate = departureDate;
    copy.arrivalPort = arrivalPort;
    copy.arrivalDate = arrivalDate;
    copy.seaArea = seaArea;
    copy.cruiseTitle = cruiseTitle;
    copy.cruisePurpose = cruisePurpose;
    copy.cruiseDescription = cruiseDescription;
    copy.sponsors = new ArrayList<>(sponsors);
    copy.funders = new ArrayList<>(funders);
    copy.scientists = new ArrayList<>(scientists);
    copy.projects = new ArrayList<>(projects);
    copy.omics = omics;
    copy.metadataAuthor = metadataAuthor;
    copy.instruments = new ArrayList<>(instruments);
    copy.packageInstruments = new LinkedHashMap<>(packageInstruments);
    return copy;
  }

  public String getCruiseId() {
    return cruiseId;
  }

  public void setCruiseId(String cruiseId) {
    this.cruiseId = cruiseId;
  }

  public String getSegmentId() {
    return segmentId;
  }

  public void setSegmentId(String segmentId) {
    this.segmentId = segmentId;
  }

  public String getPackageId() {
    return packageId;
  }

  public void setPackageId(String packageId) {
    this.packageId = packageId;
  }

  public String getMasterReleaseDate() {
    return masterReleaseDate;
  }

  public void setMasterReleaseDate(String masterReleaseDate) {
    this.masterReleaseDate = masterReleaseDate;
  }

  public String getShip() {
    return ship;
  }

  public void setShip(String ship) {
    this.ship = ship;
  }

  public String getShipUuid() {
    return shipUuid;
  }

  public void setShipUuid(String shipUuid) {
    this.shipUuid = shipUuid;
  }

  public String getDeparturePort() {
    return departurePort;
  }

  public void setDeparturePort(String departurePort) {
    this.departurePort = departurePort;
  }

  public String getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(String departureDate) {
    this.departureDate = departureDate;
  }

  public String getArrivalPort() {
    return arrivalPort;
  }

  public void setArrivalPort(String arrivalPort) {
    this.arrivalPort = arrivalPort;
  }

  public String getArrivalDate() {
    return arrivalDate;
  }

  public void setArrivalDate(String arrivalDate) {
    this.arrivalDate = arrivalDate;
  }

  public String getSeaArea() {
    return seaArea;
  }

  public void setSeaArea(String seaArea) {
    this.seaArea = seaArea;
  }

  public String getCruiseTitle() {
    return cruiseTitle;
  }

  public void setCruiseTitle(String cruiseTitle) {
    this.cruiseTitle = cruiseTitle;
  }

  public String getCruisePurpose() {
    return cruisePurpose;
  }

  public void setCruisePurpose(String cruisePurpose) {
    this.cruisePurpose = cruisePurpose;
  }

  public String getCruiseDescription() {
    return cruiseDescription;
  }

  public void setCruiseDescription(String cruiseDescription) {
    this.cruiseDescription = cruiseDescription;
  }

  public MetadataAuthor getMetadataAuthor() {
    return metadataAuthor;
  }

  public void setMetadataAuthor(MetadataAuthor metadataAuthor) {
    this.metadataAuthor = metadataAuthor;
  }

  public List<PeopleOrg> getSponsors() {
    return sponsors;
  }

  public void setSponsors(List<PeopleOrg> sponsors) {
    if (sponsors == null) {
      sponsors = new ArrayList<>(0);
    }
    this.sponsors = sponsors;
  }

  public List<PeopleOrg> getFunders() {
    return funders;
  }

  public void setFunders(List<PeopleOrg> funders) {
    if (funders == null) {
      funders = new ArrayList<>(0);
    }
    this.funders = funders;
  }

  public List<PeopleOrg> getScientists() {
    return scientists;
  }

  public void setScientists(List<PeopleOrg> scientists) {
    if (scientists == null) {
      scientists = new ArrayList<>(0);
    }
    this.scientists = scientists;
  }

  public List<String> getProjects() {
    return projects;
  }

  public void setProjects(List<String> projects) {
    if (projects == null) {
      projects = new ArrayList<>(0);
    }
    this.projects = projects;
  }

  public Omics getOmics() {
    return omics;
  }

  public void setOmics(Omics omics) {
    this.omics = omics;
  }

  public List<Instrument> getInstruments() {
    return instruments;
  }

  public void setInstruments(List<Instrument> instruments) {
    if (instruments == null) {
      instruments = new ArrayList<>(0);
    }
    this.instruments = instruments;
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class MetadataAuthor {

    private String name;
    private String uuid;
    private String phone;
    private String email;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getUuid() {
      return uuid;
    }

    public void setUuid(String uuid) {
      this.uuid = uuid;
    }

    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Omics {

    @JsonProperty("NCBI_accession")
    private String ncbiAccession;
    private List<String> samplingTypes = new ArrayList<>(0);
    private List<String> analysesTypes = new ArrayList<>(0);
    private String trackingPath;
    private String omicsComment;
    private OmicsPoc omicsPoc;

    public String getNcbiAccession() {
      return ncbiAccession;
    }

    public void setNcbiAccession(String ncbiAccession) {
      this.ncbiAccession = ncbiAccession;
    }

    public List<String> getSamplingTypes() {
      return samplingTypes;
    }

    public void setSamplingTypes(List<String> samplingTypes) {
      if (samplingTypes == null) {
        samplingTypes = new ArrayList<>(0);
      }
      this.samplingTypes = samplingTypes;
    }

    public List<String> getAnalysesTypes() {
      return analysesTypes;
    }

    public void setAnalysesTypes(List<String> analysesTypes) {
      if (analysesTypes == null) {
        analysesTypes = new ArrayList<>(0);
      }
      this.analysesTypes = analysesTypes;
    }

    public String getTrackingPath() {
      return trackingPath;
    }

    public void setTrackingPath(String trackingPath) {
      this.trackingPath = trackingPath;
    }

    public String getOmicsComment() {
      return omicsComment;
    }

    public void setOmicsComment(String omicsComment) {
      this.omicsComment = omicsComment;
    }

    public OmicsPoc getOmicsPoc() {
      return omicsPoc;
    }

    public void setOmicsPoc(OmicsPoc omicsPoc) {
      this.omicsPoc = omicsPoc;
    }
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class OmicsPoc {

    private String name;
    private String uuid;
    private String phone;
    private String email;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getUuid() {
      return uuid;
    }

    public void setUuid(String uuid) {
      this.uuid = uuid;
    }

    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class PeopleOrg {

    private String name;
    private String uuid;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getUuid() {
      return uuid;
    }

    public void setUuid(String uuid) {
      this.uuid = uuid;
    }
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class AdditionalData {

    private String calibrationData;
    private String calibrationReport;

    public String getCalibrationData() {
      return calibrationData;
    }

    public void setCalibrationData(String calibrationData) {
      this.calibrationData = calibrationData;
    }

    public String getCalibrationReport() {
      return calibrationReport;
    }

    public void setCalibrationReport(String calibrationReport) {
      this.calibrationReport = calibrationReport;
    }
  }

  public Map<String, PackageInstrument> getPackageInstruments() {
    return packageInstruments;
  }

  public void setPackageInstruments(Map<String, PackageInstrument> packageInstruments) {
    if (packageInstruments == null) {
      packageInstruments = new LinkedHashMap<>(0);
    }
    this.packageInstruments = packageInstruments;
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class PackageInstrument {

    @JsonUnwrapped
    private Instrument instrument;
    private String typeName;
    private boolean flatten;
    private List<String> extensions = new ArrayList<>(0);
    private String dirName;
    private String bagName;

    public Instrument getInstrument() {
      return instrument;
    }

    public void setInstrument(Instrument instrument) {
      this.instrument = instrument;
    }

    public String getTypeName() {
      return typeName;
    }

    public void setTypeName(String typeName) {
      this.typeName = typeName;
    }

    public boolean isFlatten() {
      return flatten;
    }

    public void setFlatten(boolean flatten) {
      this.flatten = flatten;
    }

    public List<String> getExtensions() {
      return extensions;
    }

    public void setExtensions(List<String> extensions) {
      if (extensions == null) {
        extensions = new ArrayList<>(0);
      }
      this.extensions = extensions;
    }

    public String getDirName() {
      return dirName;
    }

    public void setDirName(String dirName) {
      this.dirName = dirName;
    }

    public String getBagName() {
      return bagName;
    }

    public void setBagName(String bagName) {
      this.bagName = bagName;
    }
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class Instrument {

    private String uuid;
    private String type;
    private String instrument;
    private String shortName;
    private String releaseDate;
    private String dataPath;
    private String status;
    private String dataComment;
    private List<AdditionalData> additionalData = new ArrayList<>(0);
    private final Map<String, Object> otherFields = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getOtherFields() {
      return otherFields;
    }

    @JsonAnySetter
    public void setOtherField(String key, Object value) {
      otherFields.put(key, value);
    }

    public String getUuid() {
      return uuid;
    }

    public void setUuid(String uuid) {
      this.uuid = uuid;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getInstrument() {
      return instrument;
    }

    public void setInstrument(String instrument) {
      this.instrument = instrument;
    }

    public String getShortName() {
      return shortName;
    }

    public void setShortName(String shortName) {
      this.shortName = shortName;
    }

    public String getReleaseDate() {
      return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
      this.releaseDate = releaseDate;
    }

    public String getDataPath() {
      return dataPath;
    }

    public void setDataPath(String dataPath) {
      this.dataPath = dataPath;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getDataComment() {
      return dataComment;
    }

    public void setDataComment(String dataComment) {
      this.dataComment = dataComment;
    }

    public List<AdditionalData> getAdditionalData() {
      return additionalData;
    }

    public void setAdditionalData(List<AdditionalData> additionalData) {
      if (additionalData == null) {
        additionalData = new ArrayList<>(0);
      }
      this.additionalData = additionalData;
    }
  }
}
