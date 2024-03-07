package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonTypeInfo(use = Id.DEDUCTION)
@JsonSubTypes({ @Type(CruiseMetadata.class), @Type(CruiseData.class) })
public abstract class Cruise {

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

  protected Cruise(String cruiseId, String segmentId, String packageId, String masterReleaseDate, String ship, String shipUuid, String departurePort,
      String departureDate, String arrivalPort, String arrivalDate, String seaArea, String cruiseTitle, String cruisePurpose,
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
    if (!(o instanceof Cruise cruise)) {
      return false;
    }
    return Objects.equals(cruiseId, cruise.cruiseId) && Objects.equals(segmentId, cruise.segmentId) && Objects.equals(
        packageId, cruise.packageId) && Objects.equals(masterReleaseDate, cruise.masterReleaseDate) && Objects.equals(ship,
        cruise.ship) && Objects.equals(shipUuid, cruise.shipUuid) && Objects.equals(departurePort, cruise.departurePort)
        && Objects.equals(departureDate, cruise.departureDate) && Objects.equals(arrivalPort, cruise.arrivalPort)
        && Objects.equals(arrivalDate, cruise.arrivalDate) && Objects.equals(seaArea, cruise.seaArea) && Objects.equals(
        cruiseTitle, cruise.cruiseTitle) && Objects.equals(cruisePurpose, cruise.cruisePurpose) && Objects.equals(cruiseDescription,
        cruise.cruiseDescription) && Objects.equals(sponsors, cruise.sponsors) && Objects.equals(funders, cruise.funders)
        && Objects.equals(scientists, cruise.scientists) && Objects.equals(projects, cruise.projects) && Objects.equals(
        omics, cruise.omics) && Objects.equals(metadataAuthor, cruise.metadataAuthor) && Objects.equals(instruments,
        cruise.instruments) && Objects.equals(packageInstruments, cruise.packageInstruments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cruiseId, segmentId, packageId, masterReleaseDate, ship, shipUuid, departurePort, departureDate, arrivalPort, arrivalDate,
        seaArea, cruiseTitle, cruisePurpose, cruiseDescription, sponsors, funders, scientists, projects, omics, metadataAuthor, instruments,
        packageInstruments);
  }

  @Override
  public String toString() {
    return "Cruise{" +
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
}
