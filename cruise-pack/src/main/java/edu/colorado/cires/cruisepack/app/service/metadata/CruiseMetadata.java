package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder = CruiseMetadata.Builder.class)
public class CruiseMetadata {

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(CruiseMetadata src) {
    return new Builder(src);
  }

  private final String cruiseId;
  private final String segmentId;
  private final String packageId;
  private final String masterReleaseDate;
  private final String ship;
  private final String shipUuid;
  private final String departurePort;
  private final String departureDate;
  private final String arrivalPort;
  private final String arrivalDate;
  private final String seaArea;
  private final String cruiseTitle;
  private final String cruisePurpose;
  private final String cruiseDescription;
  private final List<PeopleOrg> sponsors;
  private final List<PeopleOrg> funders;
  private final List<PeopleOrg> scientists;
  private final List<String> projects;
  private final Omics omics;
  private final MetadataAuthor metadataAuthor;
  private final List<Instrument> instruments;
  private final Map<String, PackageInstrument> packageInstruments;

  protected CruiseMetadata(String cruiseId, String segmentId, String packageId, String masterReleaseDate, String ship, String shipUuid,
      String departurePort, String departureDate, String arrivalPort, String arrivalDate, String seaArea, String cruiseTitle, String cruisePurpose,
      String cruiseDescription, List<PeopleOrg> sponsors, List<PeopleOrg> funders, List<PeopleOrg> scientists, List<String> projects, Omics omics,
      MetadataAuthor metadataAuthor, List<Instrument> instruments, Map<String, PackageInstrument> packageInstruments) {
    this.cruiseId = cruiseId;
    this.segmentId = segmentId;
    this.packageId = packageId;
    this.masterReleaseDate = masterReleaseDate;
    this.ship = ship;
    this.shipUuid = shipUuid;
    this.departurePort = departurePort;
    this.departureDate = departureDate;
    this.arrivalPort = arrivalPort;
    this.arrivalDate = arrivalDate;
    this.seaArea = seaArea;
    this.cruiseTitle = cruiseTitle;
    this.cruisePurpose = cruisePurpose;
    this.cruiseDescription = cruiseDescription;
    this.sponsors = sponsors;
    this.funders = funders;
    this.scientists = scientists;
    this.projects = projects;
    this.omics = omics;
    this.metadataAuthor = metadataAuthor;
    this.instruments = instruments;
    this.packageInstruments = packageInstruments;
  }

  public String getCruiseId() {
    return cruiseId;
  }

  public String getSegmentId() {
    return segmentId;
  }

  public String getPackageId() {
    return packageId;
  }

  public String getMasterReleaseDate() {
    return masterReleaseDate;
  }

  public String getShip() {
    return ship;
  }

  public String getShipUuid() {
    return shipUuid;
  }

  public String getDeparturePort() {
    return departurePort;
  }

  public String getDepartureDate() {
    return departureDate;
  }

  public String getArrivalPort() {
    return arrivalPort;
  }

  public String getArrivalDate() {
    return arrivalDate;
  }

  public String getSeaArea() {
    return seaArea;
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

  public List<PeopleOrg> getSponsors() {
    return sponsors;
  }

  public List<PeopleOrg> getFunders() {
    return funders;
  }

  public List<PeopleOrg> getScientists() {
    return scientists;
  }

  public List<String> getProjects() {
    return projects;
  }

  public Omics getOmics() {
    return omics;
  }

  public MetadataAuthor getMetadataAuthor() {
    return metadataAuthor;
  }

  public List<Instrument> getInstruments() {
    return instruments;
  }

  public Map<String, PackageInstrument> getPackageInstruments() {
    return packageInstruments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CruiseMetadata that = (CruiseMetadata) o;
    return Objects.equals(cruiseId, that.cruiseId) && Objects.equals(segmentId, that.segmentId) && Objects.equals(
        packageId, that.packageId) && Objects.equals(masterReleaseDate, that.masterReleaseDate) && Objects.equals(ship, that.ship)
        && Objects.equals(shipUuid, that.shipUuid) && Objects.equals(departurePort, that.departurePort) && Objects.equals(
        departureDate, that.departureDate) && Objects.equals(arrivalPort, that.arrivalPort) && Objects.equals(arrivalDate,
        that.arrivalDate) && Objects.equals(seaArea, that.seaArea) && Objects.equals(cruiseTitle, that.cruiseTitle)
        && Objects.equals(cruisePurpose, that.cruisePurpose) && Objects.equals(cruiseDescription, that.cruiseDescription)
        && Objects.equals(sponsors, that.sponsors) && Objects.equals(funders, that.funders) && Objects.equals(scientists,
        that.scientists) && Objects.equals(projects, that.projects) && Objects.equals(omics, that.omics)
        && Objects.equals(metadataAuthor, that.metadataAuthor) && Objects.equals(instruments, that.instruments)
        && Objects.equals(packageInstruments, that.packageInstruments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cruiseId, segmentId, packageId, masterReleaseDate, ship, shipUuid, departurePort, departureDate, arrivalPort, arrivalDate,
        seaArea, cruiseTitle, cruisePurpose, cruiseDescription, sponsors, funders, scientists, projects, omics, metadataAuthor, instruments,
        packageInstruments);
  }

  @Override
  public String toString() {
    return "CruiseMetadata{" +
        "cruiseId='" + cruiseId + '\'' +
        ", segmentId='" + segmentId + '\'' +
        ", packageId='" + packageId + '\'' +
        ", masterReleaseDate='" + masterReleaseDate + '\'' +
        ", ship='" + ship + '\'' +
        ", shipUuid='" + shipUuid + '\'' +
        ", departurePort='" + departurePort + '\'' +
        ", departureDate='" + departureDate + '\'' +
        ", arrivalPort='" + arrivalPort + '\'' +
        ", arrivalDate='" + arrivalDate + '\'' +
        ", seaArea='" + seaArea + '\'' +
        ", cruiseTitle='" + cruiseTitle + '\'' +
        ", cruisePurpose='" + cruisePurpose + '\'' +
        ", cruiseDescription='" + cruiseDescription + '\'' +
        ", sponsors=" + sponsors +
        ", funders=" + funders +
        ", scientists=" + scientists +
        ", projects=" + projects +
        ", omics=" + omics +
        ", metadataAuthor=" + metadataAuthor +
        ", instruments=" + instruments +
        ", packageInstruments=" + packageInstruments +
        '}';
  }

  public static class Builder {

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
    private List<PeopleOrg> sponsors = Collections.emptyList();
    private List<PeopleOrg> funders = Collections.emptyList();
    private List<PeopleOrg> scientists = Collections.emptyList();
    private List<String> projects = Collections.emptyList();
    private Omics omics;
    private MetadataAuthor metadataAuthor;
    private List<Instrument> instruments = Collections.emptyList();
    private Map<String, PackageInstrument> packageInstruments;

    private Builder() {

    }

    private Builder(CruiseMetadata src) {
      cruiseId = src.cruiseId;
      segmentId = src.segmentId;
      packageId = src.packageId;
      masterReleaseDate = src.masterReleaseDate;
      ship = src.ship;
      shipUuid = src.shipUuid;
      departurePort = src.departurePort;
      departureDate = src.departureDate;
      arrivalPort = src.arrivalPort;
      arrivalDate = src.arrivalDate;
      seaArea = src.seaArea;
      cruiseTitle = src.cruiseTitle;
      cruisePurpose = src.cruisePurpose;
      cruiseDescription = src.cruiseDescription;
      sponsors = src.sponsors;
      funders = src.funders;
      scientists = src.scientists;
      projects = src.projects;
      omics = src.omics;
      metadataAuthor = src.metadataAuthor;
      instruments = src.instruments;
      packageInstruments = src.packageInstruments;
    }

    public Builder withCruiseId(String cruiseId) {
      this.cruiseId = cruiseId;
      return this;
    }

    public Builder withSegmentId(String segmentId) {
      this.segmentId = segmentId;
      return this;
    }

    public Builder withPackageId(String packageId) {
      this.packageId = packageId;
      return this;
    }

    public Builder withMasterReleaseDate(String masterReleaseDate) {
      this.masterReleaseDate = masterReleaseDate;
      return this;
    }

    public Builder withShip(String ship) {
      this.ship = ship;
      return this;
    }

    public Builder withShipUuid(String shipUuid) {
      this.shipUuid = shipUuid;
      return this;
    }

    public Builder withDeparturePort(String departurePort) {
      this.departurePort = departurePort;
      return this;
    }

    public Builder withDepartureDate(String departureDate) {
      this.departureDate = departureDate;
      return this;
    }

    public Builder withArrivalPort(String arrivalPort) {
      this.arrivalPort = arrivalPort;
      return this;
    }

    public Builder withArrivalDate(String arrivalDate) {
      this.arrivalDate = arrivalDate;
      return this;
    }

    public Builder withSeaArea(String seaArea) {
      this.seaArea = seaArea;
      return this;
    }

    public Builder withCruiseTitle(String cruiseTitle) {
      this.cruiseTitle = cruiseTitle;
      return this;
    }

    public Builder withCruisePurpose(String cruisePurpose) {
      this.cruisePurpose = cruisePurpose;
      return this;
    }

    public Builder withCruiseDescription(String cruiseDescription) {
      this.cruiseDescription = cruiseDescription;
      return this;
    }

    public Builder withSponsors(List<PeopleOrg> sponsors) {
      if (sponsors == null) {
        sponsors = new ArrayList<>(0);
      }
      this.sponsors = Collections.unmodifiableList(new ArrayList<>(sponsors));
      return this;
    }

    public Builder withFunders(List<PeopleOrg> funders) {
      if (funders == null) {
        funders = new ArrayList<>(0);
      }
      this.funders = Collections.unmodifiableList(new ArrayList<>(funders));
      return this;
    }

    public Builder withScientists(List<PeopleOrg> scientists) {
      if (scientists == null) {
        scientists = new ArrayList<>(0);
      }
      this.scientists = Collections.unmodifiableList(new ArrayList<>(scientists));
      return this;
    }

    public Builder withProjects(List<String> projects) {
      if (projects == null) {
        projects = new ArrayList<>(0);
      }
      this.projects = Collections.unmodifiableList(new ArrayList<>(projects));
      return this;
    }

    public Builder withOmics(Omics omics) {
      this.omics = omics;
      return this;
    }

    public Builder withMetadataAuthor(MetadataAuthor metadataAuthor) {
      this.metadataAuthor = metadataAuthor;
      return this;
    }

    public Builder withInstruments(List<Instrument> instruments) {
      if (instruments == null) {
        instruments = new ArrayList<>(0);
      }
      this.instruments = Collections.unmodifiableList(new ArrayList<>(instruments));
      return this;
    }

    public Builder withPackageInstruments(Map<String, PackageInstrument> packageInstruments) {
      if (packageInstruments == null) {
        // Allow this to be null
        this.packageInstruments = packageInstruments;
      } else {
        this.packageInstruments = Collections.unmodifiableMap(new LinkedHashMap<>(packageInstruments));
      }
      return this;
    }

    public CruiseMetadata build() {
      return new CruiseMetadata(cruiseId, segmentId, packageId, masterReleaseDate, ship, shipUuid,
          departurePort, departureDate, arrivalPort, arrivalDate, seaArea, cruiseTitle, cruisePurpose,
          cruiseDescription, sponsors, funders, scientists, projects, omics,
          metadataAuthor, instruments, packageInstruments);
    }
  }

}
