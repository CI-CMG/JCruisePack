package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class PackageModel extends PropertyChangeModel {

  private String cruiseId = "";
  private String segment = "";
  private DropDownItem sea = SeaDatastore.UNSELECTED_SEA;
  private DropDownItem arrivalPort = PortDatastore.UNSELECTED_PORT;
  private DropDownItem departurePort = PortDatastore.UNSELECTED_PORT;
  private DropDownItem ship = ShipDatastore.UNSELECTED_SHIP;
  private LocalDate departureDate = null;
  private LocalDate arrivalDate = null;
  private LocalDate releaseDate = null;
  private Path packageDirectory = null;

  public String getCruiseId() {
    return cruiseId;
  }

  public void setCruiseId(String cruiseId) {
    String oldCruiseId = this.cruiseId;
    if (!Objects.equals(cruiseId, oldCruiseId)) {
      this.cruiseId = cruiseId;
      fireChangeEvent(Events.UPDATE_CRUISE_ID, oldCruiseId, cruiseId);
    }
  }

  public DropDownItem getSea() {
    return sea;
  }

  public DropDownItem getArrivalPort() {
    return arrivalPort;
  }

  public void setArrivalPort(DropDownItem arrivalPort) {
    DropDownItem oldArrivalPort = this.arrivalPort;
    if (!Objects.equals(arrivalPort, oldArrivalPort)) {
      this.arrivalPort = arrivalPort;
      fireChangeEvent(Events.UPDATE_ARRIVAL_PORT, oldArrivalPort, arrivalPort);
    }
  }

  public DropDownItem getDeparturePort() {
    return departurePort;
  }

  public void setDeparturePort(DropDownItem departurePort) {
    DropDownItem oldDeparturePort = this.departurePort;
    if (!Objects.equals(departurePort, oldDeparturePort)) {
      this.departurePort = departurePort;
      fireChangeEvent(Events.UPDATE_DEPARTURE_PORT, oldDeparturePort, departurePort);
    }
  }

  public void setSea(DropDownItem sea) {
    DropDownItem oldSea = this.sea;
    if (!Objects.equals(sea, oldSea)) {
      this.sea = sea;
      fireChangeEvent(Events.UPDATE_SEA, oldSea, sea);
    }
  }

  public DropDownItem getShip() {
    return ship;
  }

  public void setShip(DropDownItem ship) {
    DropDownItem oldShip = this.ship;
    if (!Objects.equals(ship, oldShip)) {
      this.ship = ship;
      fireChangeEvent(Events.UPDATE_SHIP, oldShip, ship);
    }
  }

  public String getSegment() {
    return segment;
  }

  public void setSegment(String segment) {
    String oldSegment = this.segment;
    if (!Objects.equals(segment, oldSegment)) {
      this.segment = segment;
      fireChangeEvent(Events.UPDATE_SEGMENT, oldSegment, segment);
    }
  }

  public LocalDate getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDate departureDate) {
    LocalDate oldDepartureDate = this.departureDate;
    if (!Objects.equals(departureDate, oldDepartureDate)) {
      this.departureDate = departureDate;
      fireChangeEvent(Events.UPDATE_DEPARTURE_DATE, oldDepartureDate, departureDate);
    }
  }

  public LocalDate getArrivalDate() {
    return arrivalDate;
  }

  public void setArrivalDate(LocalDate arrivalDate) {
    LocalDate oldArrivalDate = this.arrivalDate;
    if (!Objects.equals(arrivalDate, oldArrivalDate)) {
      this.arrivalDate = arrivalDate;
      fireChangeEvent(Events.UPDATE_ARRIVAL_DATE, oldArrivalDate, arrivalDate);
    }
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(LocalDate releaseDate) {
    LocalDate oldReleaseDate = this.releaseDate;
    if (!Objects.equals(releaseDate, oldReleaseDate)) {
      this.releaseDate = releaseDate;
      fireChangeEvent(Events.UPDATE_RELEASE_DATE, oldReleaseDate, releaseDate);
    }
  }

  public Path getPackageDirectory() {
    return packageDirectory;
  }

  public void setPackageDirectory(Path packageDirectory) {
    Path oldPackageDirectory = this.packageDirectory;
    if (!Objects.equals(packageDirectory, oldPackageDirectory)) {
      this.packageDirectory = packageDirectory;
      fireChangeEvent(Events.UPDATE_PACKAGE_DIRECTORY, oldPackageDirectory, packageDirectory);
    }
  }
}
