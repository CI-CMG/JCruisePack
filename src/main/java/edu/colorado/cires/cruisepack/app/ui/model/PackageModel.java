package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ProjectDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.Cruise;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathExists;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsDirectory;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidDropDownItemModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidPortDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidSeaDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidShipDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.DropDownItemPanel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PackageModel extends PropertyChangeModel {

  @NotBlank //TODO add more validation, length, underscores, etc?
  private String cruiseId = null;
  private String cruiseIdError = null;
  @NotBlank // TODO: Remove after testing, this can be null
  private String segment = null;
  private String segmentError = null;
  @ValidSeaDropDownItem
  private DropDownItem sea = SeaDatastore.UNSELECTED_SEA;
  private String seaError = null;
  @ValidPortDropDownItem
  private DropDownItem arrivalPort = PortDatastore.UNSELECTED_PORT;
  private String arrivalPortError = null;
  @ValidPortDropDownItem
  private DropDownItem departurePort = PortDatastore.UNSELECTED_PORT;
  private String departurePortError = null;
  @ValidShipDropDownItem
  private DropDownItem ship = ShipDatastore.UNSELECTED_SHIP;
  private String shipError = null;
  @NotNull(message = "must not be blank")
  private LocalDate departureDate = null;
  private String departureDateError = null;
  @NotNull(message = "must not be blank")
  private LocalDate arrivalDate = null;
  private String arrivalDateError = null;

  @NotNull(message = "must not be blank") //TODO validate in the future
  private LocalDate releaseDate = null;
  private String releaseDateError = null;

  @NotNull(message = "must not be blank") //TODO add more validation, space?
  @PathExists
  @PathIsDirectory
  private Path packageDirectory = null;
  private String packageDirectoryError = null;

  @NotEmpty
  private List<@ValidDropDownItemModel DropDownItemModel> projects = new ArrayList<>(0);
  private String projectsError = null;
  
  private DropDownItem existingRecord = CruiseDataDatastore.UNSELECTED_CRUISE;

  public void restoreDefaults() {
    setCruiseId(null);
    setCruiseIdError(null);

    setSegment(null);
    setSegmentError(null);

    setSea(SeaDatastore.UNSELECTED_SEA);
    setSeaError(null);

    setArrivalPort(PortDatastore.UNSELECTED_PORT);
    setArrivalPortError(null);

    setDeparturePort(PortDatastore.UNSELECTED_PORT);
    setDeparturePortError(null);

    setShip(ShipDatastore.UNSELECTED_SHIP);
    setShipError(null);

    setDepartureDate(null);
    setDepartureDateError(null);

    setArrivalDate(null);
    setArrivalDateError(null);

    setReleaseDate(null);
    setReleaseDateError(null);

    setPackageDirectory(null);
    setPackageDirectoryError(null);

    clearProjects();
    setProjectsError(null);
    
    setExistingRecord(CruiseDataDatastore.UNSELECTED_CRUISE);
  }

  public void updateFormState(Cruise cruiseMetadata, ProjectDatastore projectDatastore, PortDatastore portDatastore, ShipDatastore shipDatastore, SeaDatastore seaDatastore, CruiseDataDatastore cruiseDataDatastore) {
    setCruiseId(cruiseMetadata.getCruiseId());
    setSegment(cruiseMetadata.getSegmentId());
    if (cruiseMetadata instanceof CruiseData) {
      setPackageDirectory(((CruiseData) cruiseMetadata).getPackageDirectory());
    }
    if (cruiseMetadata.getShipUuid() != null && cruiseMetadata.getShip() != null) {
      DropDownItem dropDownItem = shipDatastore.getShipDropDowns().stream()
        .filter(i -> i.getId().equals(cruiseMetadata.getShipUuid())).findFirst().orElse(null);
      setShip(dropDownItem);
    }
    if (cruiseMetadata.getDeparturePort() != null) {
      DropDownItem dropDownItem = portDatastore.getPortDropDowns().stream()
        .filter(i -> i.getValue().equals(cruiseMetadata.getDeparturePort())).findFirst().orElse(null);
      setDeparturePort(dropDownItem);
    }
    if (cruiseMetadata.getDepartureDate() != null) {
      setDepartureDate(LocalDate.parse(cruiseMetadata.getDepartureDate()));
    }
    if (cruiseMetadata.getSeaArea() != null) {
      DropDownItem dropDownItem = seaDatastore.getSeaDropDowns().stream()
        .filter(i -> i.getValue().equals(cruiseMetadata.getSeaArea())).findFirst().orElse(null);
        setSea(dropDownItem);
    }
    if (cruiseMetadata.getArrivalPort() != null) {
      DropDownItem dropDownItem = portDatastore.getPortDropDowns().stream()
        .filter(i -> i.getValue().equals(cruiseMetadata.getArrivalPort())).findFirst().orElse(null);
      setArrivalPort(dropDownItem);
    }
    if (cruiseMetadata.getArrivalDate() != null) {
      setArrivalDate(LocalDate.parse(cruiseMetadata.getArrivalDate()));
    }
    if (!cruiseMetadata.getProjects().isEmpty()) {
      clearProjects();
      for (DropDownItem item : projectDatastore.getProjectDropDownsMatchingNames(cruiseMetadata.getProjects())) {
        DropDownItemPanel panel = new DropDownItemPanel(projectDatastore.getProjectDropDowns(), ProjectDatastore.UNSELECTED_PROJECT);
        panel.getModel().setItem(item);
        addProject(panel);
      }
    }
    if (cruiseMetadata.getMasterReleaseDate() != null) {
      setReleaseDate(LocalDate.parse(cruiseMetadata.getMasterReleaseDate()));
    }
    
    setExistingRecord(
        cruiseDataDatastore.getByPackageId(
            cruiseMetadata.getPackageId()
        ).map(cd -> new DropDownItem(cd.getPackageId(), cd.getPackageId()))
        .orElse(CruiseDataDatastore.UNSELECTED_CRUISE)
    );
  }

  public String getCruiseId() {
    return cruiseId;
  }

  public void setCruiseId(String cruiseId) {
    cruiseId = normalizeString(cruiseId);
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
    segment = normalizeString(segment);
    setIfChanged(Events.UPDATE_SEGMENT, segment, () -> this.segment, (nv) -> this.segment = nv);
  }

  public void setSegmentError(String segmentError) {
    setIfChanged(Events.UPDATE_SEGMENT_ERROR, segmentError, () -> this.segmentError, (e) -> this.segmentError = e);
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

  public void setCruiseIdError(String cruiseIdError) {
    setIfChanged(Events.UPDATE_CRUISE_ID_ERROR, cruiseIdError, () -> this.cruiseIdError, (nv) -> this.cruiseIdError = nv);

  }

  public void setPackageDirectoryError(String packageDirectoryError) {
    setIfChanged(Events.UPDATE_PACKAGE_DIRECTORY_ERROR, packageDirectoryError, () -> this.packageDirectoryError, (nv) -> this.packageDirectoryError = nv);
  }

  public void setReleaseDateError(String releaseDateError) {
    setIfChanged(Events.UPDATE_RELEASE_DATE_ERROR, releaseDateError, () -> this.releaseDateError, (nv) -> this.releaseDateError = nv);
  }

  public void setSeaError(String seaError) {
      setIfChanged(Events.UPDATE_SEA_ERROR, seaError, () -> this.seaError, (e) -> this.seaError = e);
  }

  public void setArrivalPortError(String arrivalPortError) {
      setIfChanged(Events.UPDATE_ARRIVAL_PORT_ERROR, arrivalPortError, () -> this.arrivalPortError, (e) -> this.arrivalPortError = e);
  }

  public void setDeparturePortError(String departurePortError) {
      setIfChanged(Events.UPDATE_DEPARTURE_PORT_ERROR, departurePortError, () -> this.departurePortError, (e) -> this.departurePortError = e);
  }

  public void setShipError(String shipError) {
      setIfChanged(Events.UPDATE_SHIP_ERROR, shipError, () -> this.shipError, (e) -> this.shipError = e);
  }

  public void setDepartureDateError(String departureDateError) {
    setIfChanged(Events.UPDATE_DEPARTURE_DATE_ERROR, departureDateError, () -> this.departureDateError, (e) -> this.departureDateError = e);
  }

  public void setArrivalDateError(String arrivalDateError) {
    setIfChanged(Events.UPDATE_ARRIVAL_DATE_ERROR, arrivalDateError, () -> this.arrivalDateError, (e) -> this.arrivalDateError = e);
  }

  public List<DropDownItemModel> getProjects() {
      return projects;
  }

  public void setProjectsError(String projectsError) {
      setIfChanged(Events.UPDATE_PROJECTS_ERROR, projectsError, () -> this.projectsError, (e) -> this.projectsError = e);
  }

  public void setProjectErrors(List<String> projectErrors) {
    for (int i = 0; i < projectErrors.size(); i++) {
      projects.get(i).setItemError(
          projectErrors.get(i)
      );
    }
  }

  public void clearProjects() {
    fireChangeEvent(Events.CLEAR_PROJECTS, null, Collections.emptyList());
  }
  
  public void addProject(DropDownItemPanel panel) {
    List<DropDownItemModel> oldModels = new ArrayList<>(projects);
    DropDownItemModel model = panel.getModel();
    if (!oldModels.contains(model)) {
      List<DropDownItemModel> newModels = new ArrayList<>(projects);
      newModels.add(model);
      projects = newModels;
      fireChangeEvent(Events.ADD_PROJECT, null, panel);
    }
  }
  
  public void removeProject(DropDownItemPanel panel) {
    List<DropDownItemModel> oldModels = new ArrayList<>(projects);
    DropDownItemModel model = panel.getModel();
    if (oldModels.contains(model)) {
      List<DropDownItemModel> newModels = new ArrayList<>(projects);
      newModels.remove(model);
      projects = newModels;
      fireChangeEvent(Events.REMOVE_PROJECT, panel, null);
    }
  }

  public DropDownItem getExistingRecord() {
    return existingRecord;
  }

  public void setExistingRecord(DropDownItem existingRecord) {
    setIfChanged(Events.UPDATE_EXISTING_RECORD, existingRecord, () -> this.existingRecord, (dd) -> this.existingRecord = dd);
  }
}
