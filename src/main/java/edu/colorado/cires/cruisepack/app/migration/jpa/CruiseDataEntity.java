package edu.colorado.cires.cruisepack.app.migration.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "CRUISE_DATA")
public class CruiseDataEntity {

  @Id
  @Column(name = "ID")
  private Integer id;
  @Column(name = "CRUISE_ID")
  private String cruiseId;
  @Column(name = "SEGMENT_ID")
  private String segmentId;
  @Column(name = "PACKAGE_ID")
  private String packageId;
  @Column(name = "SHIP")
  private String ship;
  @Column(name = "SHIP_UUID")
  private String shipUuid;
  @Column(name = "SEA_AREA")
  private String seaArea;
  @Column(name = "DEPARTURE_PORT")
  private String departurePort;
  @Column(name = "DEPARTURE_TIME")
  private LocalDate departureTime;
  @Column(name = "ARRIVAL_PORT")
  private String arrivalPort;
  @Column(name = "ARRIVAL_TIME")
  private LocalDate arrivalTime;
  @Column(name = "CRUISE_TITLE")
  private String cruiseTitle;
  @Column(name = "PURPOSE_TEXT")
  private String purposeText;
  @Column(name = "ABSTRACT_TEXT")
  private String abstractText;
  @Column(name = "PROJECTS")
  private String projects;
  @Column(name = "METADATA_AUTHOR")
  private String metadataAuthor;
  @Column(name = "META_AUTHOR_UUID")
  private String metadataAuthorUuid;
  @Column(name = "SCIENTISTS")
  private String scientists;
  @Column(name = "FUNDERS")
  private String funders;
  @Column(name = "SPONSORS")
  private String sponsors;
  @Column(name = "MASTER_RELEASE_DATE")
  private LocalDate masterReleaseDate;
  @Column(name = "DESTINATION_PATH")
  private String destinationPath;
  @Column(name = "DATASETS")
  private String datasets;
  @Column(name = "USE")
  private String use;
  @Column(name = "DOCS_PATH")
  private String docsPath;
  @Column(name = "OMICS")
  private String omics;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public String getSeaArea() {
    return seaArea;
  }

  public void setSeaArea(String seaArea) {
    this.seaArea = seaArea;
  }

  public String getDeparturePort() {
    return departurePort;
  }

  public void setDeparturePort(String departurePort) {
    this.departurePort = departurePort;
  }

  public LocalDate getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(LocalDate departureTime) {
    this.departureTime = departureTime;
  }

  public String getArrivalPort() {
    return arrivalPort;
  }

  public void setArrivalPort(String arrivalPort) {
    this.arrivalPort = arrivalPort;
  }

  public LocalDate getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(LocalDate arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public String getCruiseTitle() {
    return cruiseTitle;
  }

  public void setCruiseTitle(String cruiseTitle) {
    this.cruiseTitle = cruiseTitle;
  }

  public String getPurposeText() {
    return purposeText;
  }

  public void setPurposeText(String purposeText) {
    this.purposeText = purposeText;
  }

  public String getAbstractText() {
    return abstractText;
  }

  public void setAbstractText(String abstractText) {
    this.abstractText = abstractText;
  }

  public String getProjects() {
    return projects;
  }

  public void setProjects(String projects) {
    this.projects = projects;
  }

  public String getMetadataAuthor() {
    return metadataAuthor;
  }

  public void setMetadataAuthor(String metadataAuthor) {
    this.metadataAuthor = metadataAuthor;
  }

  public String getMetadataAuthorUuid() {
    return metadataAuthorUuid;
  }

  public void setMetadataAuthorUuid(String metadataAuthorUuid) {
    this.metadataAuthorUuid = metadataAuthorUuid;
  }

  public String getScientists() {
    return scientists;
  }

  public void setScientists(String scientists) {
    this.scientists = scientists;
  }

  public String getFunders() {
    return funders;
  }

  public void setFunders(String funders) {
    this.funders = funders;
  }

  public String getSponsors() {
    return sponsors;
  }

  public void setSponsors(String sponsors) {
    this.sponsors = sponsors;
  }

  public LocalDate getMasterReleaseDate() {
    return masterReleaseDate;
  }

  public void setMasterReleaseDate(LocalDate masterReleaseDate) {
    this.masterReleaseDate = masterReleaseDate;
  }

  public String getDestinationPath() {
    return destinationPath;
  }

  public void setDestinationPath(String destinationPath) {
    this.destinationPath = destinationPath;
  }

  public String getDatasets() {
    return datasets;
  }

  public void setDatasets(String datasets) {
    this.datasets = datasets;
  }

  public String getUse() {
    return use;
  }

  public void setUse(String use) {
    this.use = use;
  }

  public String getDocsPath() {
    return docsPath;
  }

  public void setDocsPath(String docsPath) {
    this.docsPath = docsPath;
  }

  public String getOmics() {
    return omics;
  }

  public void setOmics(String omics) {
    this.omics = omics;
  }
}
