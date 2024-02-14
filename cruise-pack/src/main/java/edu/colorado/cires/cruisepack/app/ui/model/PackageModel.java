package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Path;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class PackageModel extends PropertyChangeModel {

  @NotBlank //TODO add more validation, length, underscores, etc?
  private String cruiseId = null;
  private String cruiseIdError = null;

  private String segment = null;
  private DropDownItem sea = SeaDatastore.UNSELECTED_SEA;
  private DropDownItem arrivalPort = PortDatastore.UNSELECTED_PORT;
  private DropDownItem departurePort = PortDatastore.UNSELECTED_PORT;
  private DropDownItem ship = ShipDatastore.UNSELECTED_SHIP;
  private LocalDate departureDate = null;
  private LocalDate arrivalDate = null;
  private LocalDate releaseDate = null;

  @NotNull //TODO add more validation, path exists, space?
  private Path packageDirectory = null;
  private String packageDirectoryError = null;

  public String getCruiseId() {
    return cruiseId;
  }

  public void setCruiseId(String cruiseId) {
    setIfChanged(Events.UPDATE_CRUISE_ID, cruiseId, () -> this.cruiseId, (nv) -> this.cruiseId = nv);
  }

  public DropDownItem getSea() {
    return sea;
  }

  public DropDownItem getArrivalPort() {
    return arrivalPort;
  }

  public void setArrivalPort(DropDownItem arrivalPort) {
    setIfChanged(Events.UPDATE_ARRIVAL_PORT, arrivalPort, () -> this.arrivalPort, (nv) -> this.arrivalPort = nv);
  }

  public DropDownItem getDeparturePort() {
    return departurePort;
  }

  public void setDeparturePort(DropDownItem departurePort) {
    setIfChanged(Events.UPDATE_DEPARTURE_PORT, departurePort, () -> this.departurePort, (nv) -> this.departurePort = nv);
  }

  public void setSea(DropDownItem sea) {
    setIfChanged(Events.UPDATE_SEA, sea, () -> this.sea, (nv) -> this.sea = nv);
  }

  public DropDownItem getShip() {
    return ship;
  }

  public void setShip(DropDownItem ship) {
    setIfChanged(Events.UPDATE_SHIP, ship, () -> this.ship, (nv) -> this.ship = nv);
  }

  public String getSegment() {
    return segment;
  }

  public void setSegment(String segment) {
    setIfChanged(Events.UPDATE_SEGMENT, segment, () -> this.segment, (nv) -> this.segment = nv);
  }

  public LocalDate getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDate departureDate) {
    setIfChanged(Events.UPDATE_DEPARTURE_DATE, departureDate, () -> this.departureDate, (nv) -> this.departureDate = nv);
  }

  public LocalDate getArrivalDate() {
    return arrivalDate;
  }

  public void setArrivalDate(LocalDate arrivalDate) {
    setIfChanged(Events.UPDATE_ARRIVAL_DATE, arrivalDate, () -> this.arrivalDate, (nv) -> this.arrivalDate = nv);
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(LocalDate releaseDate) {
    setIfChanged(Events.UPDATE_RELEASE_DATE, releaseDate, () -> this.releaseDate, (nv) -> this.releaseDate = nv);
  }

  public Path getPackageDirectory() {
    return packageDirectory;
  }

  public void setPackageDirectory(Path packageDirectory) {
    setIfChanged(Events.UPDATE_PACKAGE_DIRECTORY, packageDirectory, () -> this.packageDirectory, (nv) -> this.packageDirectory = nv);
  }

  public String getCruiseIdError() {
    return cruiseIdError;
  }

  public void setCruiseIdError(String cruiseIdError) {
    setIfChanged(Events.UPDATE_CRUISE_ID_ERROR, cruiseIdError, () -> this.cruiseIdError, (nv) -> this.cruiseIdError = nv);

  }

  public String getPackageDirectoryError() {
    return packageDirectoryError;
  }

  public void setPackageDirectoryError(String packageDirectoryError) {
    setIfChanged(Events.UPDATE_PACKAGE_DIRECTORY_ERROR, packageDirectoryError, () -> this.packageDirectoryError, (nv) -> this.packageDirectoryError = nv);

  }
}
