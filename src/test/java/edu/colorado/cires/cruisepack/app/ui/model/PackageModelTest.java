package edu.colorado.cires.cruisepack.app.ui.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.DropDownItemPanel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class PackageModelTest extends PropertyChangeModelTest<PackageModel> {

  @Override
  protected PackageModel createModel() {
    return new PackageModel();
  }

  @Test
  void restoreDefaults() {
    LocalDate date1 = LocalDate.now();
    LocalDate date2 = date1.minusDays(1);
    LocalDate date3 = date1.plusDays(1);
    
    model.setCruiseId("cruise id");
    model.setCruiseIdError("cruise id error");
    model.setSegment("segment");
    model.setSegmentError("segment error");
    model.setSea(new DropDownItem("1", "sea 1"));
    model.setSeaError("sea error");
    model.setArrivalPort(new DropDownItem("1", "arrival port 1"));
    model.setArrivalPortError("arrival port error");
    model.setDeparturePort(new DropDownItem("1", "departure port 1"));
    model.setDeparturePortError("departure port error");
    model.setShip(new DropDownItem("1", "ship 1"));
    model.setShipError("ship error");
    model.setDepartureDate(date1);
    model.setDepartureDateError("departure date error");
    model.setArrivalDate(date2);
    model.setArrivalDateError("arrival date error");
    model.setReleaseDate(date3);
    model.setReleaseDateError("release date error");
    Path packageDirectory = Paths.get("package directory");
    model.setPackageDirectory(packageDirectory);
    model.setPackageDirectoryError("package directory error");
    model.setProjectsError("projects error");
    model.setExistingRecord(new DropDownItem("1", "cruise 1"));
    
    List<DropDownItem> options = List.of(
        new DropDownItem("1", "value 1"), new DropDownItem("2", "value 2")
    );
    DropDownItem defaultOption = new DropDownItem("", "default option");
    DropDownItemPanel panel1 = new DropDownItemPanel(options, defaultOption);
    DropDownItemPanel panel2 = new DropDownItemPanel(options, defaultOption);
    model.addProject(panel1);
    model.addProject(panel2);
    
    List<String> projectErrors = List.of(
        "error 1", "error 2"
    );
    model.setProjectErrors(projectErrors);
    
    clearMap();
    model.restoreDefaults();
    
    assertChangeEvent(Events.UPDATE_CRUISE_ID, "cruise id", null);
    assertNull(model.getCruiseId());
    assertChangeEvent(Events.UPDATE_CRUISE_ID_ERROR, "cruise id error", null);
    assertNull(model.getCruiseIdError());
    assertChangeEvent(Events.UPDATE_SEGMENT, "segment", null);
    assertNull(model.getSegment());
    assertChangeEvent(Events.UPDATE_SEGMENT_ERROR, "segment error", null);
    assertNull(model.getSegmentError());
    assertChangeEvent(Events.UPDATE_SEA, new DropDownItem("1", "sea 1"), SeaDatastore.UNSELECTED_SEA);
    assertEquals(SeaDatastore.UNSELECTED_SEA, model.getSea());
    assertChangeEvent(Events.UPDATE_ARRIVAL_PORT, new DropDownItem("1", "arrival port 1"), PortDatastore.UNSELECTED_PORT);
    assertEquals(PortDatastore.UNSELECTED_PORT, model.getArrivalPort());
    assertChangeEvent(Events.UPDATE_ARRIVAL_PORT_ERROR, "arrival port error", null);
    assertNull(model.getArrivalPortError());
    assertChangeEvent(Events.UPDATE_DEPARTURE_PORT, new DropDownItem("1", "departure port 1"), PortDatastore.UNSELECTED_PORT);
    assertEquals(PortDatastore.UNSELECTED_PORT, model.getDeparturePort());
    assertChangeEvent(Events.UPDATE_DEPARTURE_PORT_ERROR, "departure port error", null);
    assertNull(model.getDeparturePortError());
    assertChangeEvent(Events.UPDATE_SHIP, new DropDownItem("1", "ship 1"), ShipDatastore.UNSELECTED_SHIP);
    assertEquals(ShipDatastore.UNSELECTED_SHIP, model.getShip());
    assertChangeEvent(Events.UPDATE_SHIP_ERROR, "ship error", null);
    assertNull(model.getShipError());
    assertChangeEvent(Events.UPDATE_DEPARTURE_DATE, date1, null);
    assertNull(model.getDepartureDate());
    assertChangeEvent(Events.UPDATE_DEPARTURE_DATE_ERROR, "departure date error", null);
    assertNull(model.getDepartureDateError());
    assertChangeEvent(Events.UPDATE_ARRIVAL_DATE, date2, null);
    assertNull(model.getArrivalDate());
    assertChangeEvent(Events.UPDATE_ARRIVAL_DATE_ERROR, "arrival date error", null);
    assertNull(model.getArrivalDateError());
    assertChangeEvent(Events.UPDATE_RELEASE_DATE, date3, null);
    assertNull(model.getReleaseDate());
    assertChangeEvent(Events.UPDATE_RELEASE_DATE_ERROR, "release date error", null);
    assertNull(model.getReleaseDateError());
    assertChangeEvent(Events.UPDATE_PACKAGE_DIRECTORY, packageDirectory, null);
    assertNull(model.getPackageDirectory());
    assertChangeEvent(Events.UPDATE_PACKAGE_DIRECTORY_ERROR, "package directory error", null);
    assertNull(model.getPackageDirectoryError());
    assertChangeEvent(Events.UPDATE_PROJECTS_ERROR, "projects error", null);
    assertNull(model.getProjectsError());
    assertChangeEvent(Events.UPDATE_EXISTING_RECORD, new DropDownItem("1", "cruise 1"), CruiseDataDatastore.UNSELECTED_CRUISE);
    assertEquals(CruiseDataDatastore.UNSELECTED_CRUISE, model.getExistingRecord());
    assertChangeEvent(Events.CLEAR_PROJECTS, List.of(
        panel1.getModel(), panel2.getModel()
    ), Collections.emptyList());
    assertEquals(Collections.emptyList(), model.getProjects());
  }

  @Test
  void updateFormState() {
    Path packageDirectory = Paths.get("package directory");
    
    CruiseData cruiseData = CruiseData.builder()
        .withPackageId("package id")
        .withCruiseId("cruise id")
        .withSegmentId("segment id")
        .withSeaArea("sea area")
        .withArrivalPort("arrival port")
        .withDeparturePort("departure port")
        .withShip("ship")
        .withShipUuid(UUID.randomUUID().toString())
        .withDepartureDate(LocalDate.now().minusDays(1).toString())
        .withArrivalDate(LocalDate.now().minusDays(1).toString())
        .withMasterReleaseDate(LocalDate.now().plusDays(1).toString())
        .withPackageDirectory(packageDirectory.toString())
        .withProjects(List.of(
            "project 1", "project 2"
        ))
        .build();
    
    List<DropDownItem> projects = List.of(
        new DropDownItem("1", "project 1"), new DropDownItem("2", "project 2"), new DropDownItem("3", "project 3")
    );

    model.updateFormState(
        cruiseData,
        projects,
        List.of(
            new DropDownItem("1", "arrival port"), new DropDownItem("2", "departure port"), new DropDownItem("3", "another arrival port")
        ),
        List.of(
            new DropDownItem(cruiseData.getShipUuid(), "ship"), new DropDownItem("2", "another ship")
        ),
        List.of(
            new DropDownItem("1", "sea area"), new DropDownItem("2", "another sea area")
        )
    );

    assertChangeEvent(Events.UPDATE_CRUISE_ID, null, "cruise id");
    assertEquals("cruise id", model.getCruiseId());
    assertChangeEvent(Events.UPDATE_SEGMENT, null, "segment id");
    assertEquals("segment id", model.getSegment());
    assertChangeEvent(Events.UPDATE_SEA, SeaDatastore.UNSELECTED_SEA, new DropDownItem("1", "sea area"));
    assertEquals(new DropDownItem("1", "sea area"), model.getSea());
    assertChangeEvent(Events.UPDATE_ARRIVAL_PORT, PortDatastore.UNSELECTED_PORT, new DropDownItem("1", "arrival port"));
    assertEquals(new DropDownItem("1", "arrival port"), model.getArrivalPort());
    assertChangeEvent(Events.UPDATE_DEPARTURE_PORT, PortDatastore.UNSELECTED_PORT, new DropDownItem("2", "departure port"));
    assertEquals(new DropDownItem("2", "departure port"), model.getDeparturePort());
    assertChangeEvent(Events.UPDATE_SHIP, ShipDatastore.UNSELECTED_SHIP, new DropDownItem(cruiseData.getShipUuid(), "ship"));
    assertEquals(new DropDownItem(cruiseData.getShipUuid(), "ship"), model.getShip());
    assertChangeEvent(Events.UPDATE_DEPARTURE_DATE, null, LocalDate.parse(cruiseData.getDepartureDate()));
    assertEquals(cruiseData.getDepartureDate(), model.getDepartureDate().toString());
    assertChangeEvent(Events.UPDATE_ARRIVAL_DATE, null, LocalDate.parse(cruiseData.getArrivalDate()));
    assertEquals(cruiseData.getArrivalDate(), model.getArrivalDate().toString());
    assertChangeEvent(Events.UPDATE_RELEASE_DATE, null, LocalDate.parse(cruiseData.getMasterReleaseDate()));
    assertEquals(cruiseData.getMasterReleaseDate(), model.getReleaseDate().toString());
    assertChangeEvent(Events.UPDATE_PACKAGE_DIRECTORY, null, packageDirectory);
    assertEquals(packageDirectory.toString(), model.getPackageDirectory().toString());
    assertChangeEvent(Events.UPDATE_EXISTING_RECORD, CruiseDataDatastore.UNSELECTED_CRUISE, new DropDownItem("package id", "package id"));
    assertEquals(new DropDownItem("package id", "package id"), model.getExistingRecord());
    assertEquals(List.of(
        projects.get(0), projects.get(1)
    ), model.getProjects().stream().map(DropDownItemModel::getItem).collect(Collectors.toList()));
  }

  @Test
  void setCruiseId() {
    assertPropertyChange(Events.UPDATE_CRUISE_ID, model::getCruiseId, model::setCruiseId, "value1", "value2", null);
  }

  @Test
  void setArrivalPort() {
    assertPropertyChange(Events.UPDATE_ARRIVAL_PORT, model::getArrivalPort, model::setArrivalPort, new DropDownItem("1", "value1"), new DropDownItem("2", "value2"), PortDatastore.UNSELECTED_PORT);
  }

  @Test
  void setDeparturePort() {
    assertPropertyChange(Events.UPDATE_DEPARTURE_PORT, model::getDeparturePort, model::setDeparturePort, new DropDownItem("1", "value1"), new DropDownItem("2", "value2"), PortDatastore.UNSELECTED_PORT);
  }

  @Test
  void setSea() {
    assertPropertyChange(Events.UPDATE_SEA, model::getSea, model::setSea, new DropDownItem("1", "value1"), new DropDownItem("2", "value2"), SeaDatastore.UNSELECTED_SEA);
  }

  @Test
  void setShip() {
    assertPropertyChange(Events.UPDATE_SHIP, model::getShip, model::setShip, new DropDownItem("1", "value1"), new DropDownItem("2", "value2"), ShipDatastore.UNSELECTED_SHIP);
  }

  @Test
  void setSegment() {
    assertPropertyChange(Events.UPDATE_SEGMENT, model::getSegment, model::setSegment, "value1", "value2", null);
  }

  @Test
  void setSegmentError() {
    assertPropertyChange(Events.UPDATE_SEGMENT_ERROR, model::getSegmentError, model::setSegmentError, "value1", "value2", null);
  }

  @Test
  void setDepartureDate() {
    assertPropertyChange(Events.UPDATE_DEPARTURE_DATE, model::getDepartureDate, model::setDepartureDate, LocalDate.now(), LocalDate.now().minusDays(1), null);
  }

  @Test
  void setArrivalDate() {
    assertPropertyChange(Events.UPDATE_ARRIVAL_DATE, model::getArrivalDate, model::setArrivalDate, LocalDate.now(), LocalDate.now().minusDays(1), null);
  }

  @Test
  void setReleaseDate() {
    assertPropertyChange(Events.UPDATE_RELEASE_DATE, model::getReleaseDate, model::setReleaseDate, LocalDate.now(), LocalDate.now().minusDays(1), null);
  }

  @Test
  void setPackageDirectory() {
    assertPropertyChange(Events.UPDATE_PACKAGE_DIRECTORY, model::getPackageDirectory, model::setPackageDirectory, Paths.get("path1"), Paths.get("path2"), null);
  }

  @Test
  void setCruiseIdError() {
    assertPropertyChange(Events.UPDATE_CRUISE_ID_ERROR, model::getCruiseIdError, model::setCruiseIdError, "value1", "value2", null);
  }

  @Test
  void setPackageDirectoryError() {
    assertPropertyChange(Events.UPDATE_PACKAGE_DIRECTORY_ERROR, model::getPackageDirectoryError, model::setPackageDirectoryError, "value1", "value2", null);
  }

  @Test
  void setReleaseDateError() {
    assertPropertyChange(Events.UPDATE_RELEASE_DATE_ERROR, model::getReleaseDateError, model::setReleaseDateError, "value1", "value2", null);
  }

  @Test
  void setSeaError() {
    assertPropertyChange(Events.UPDATE_SEA_ERROR, model::getSeaError, model::setSeaError, "value1", "value2", null);
  }

  @Test
  void setArrivalPortError() {
    assertPropertyChange(Events.UPDATE_ARRIVAL_PORT_ERROR, model::getArrivalPortError, model::setArrivalPortError, "value1", "value2", null);
  }

  @Test
  void setDeparturePortError() {
    assertPropertyChange(Events.UPDATE_DEPARTURE_PORT_ERROR, model::getDeparturePortError, model::setDeparturePortError, "value1", "value2", null);
  }

  @Test
  void setShipError() {
    assertPropertyChange(Events.UPDATE_SHIP_ERROR, model::getShipError, model::setShipError, "value1", "value2", null);
  }

  @Test
  void setDepartureDateError() {
    assertPropertyChange(Events.UPDATE_DEPARTURE_DATE_ERROR, model::getDepartureDateError, model::setDepartureDateError, "value1", "value2", null);
  }

  @Test
  void setArrivalDateError() {
    assertPropertyChange(Events.UPDATE_ARRIVAL_DATE_ERROR, model::getArrivalDateError, model::setArrivalDateError, "value1", "value2", null);
  }

  @Test
  void setProjectsError() {
    assertPropertyChange(Events.UPDATE_PROJECTS_ERROR, model::getProjectsError, model::setProjectsError, "value1", "value2", null);
  }

  @Test
  void setProjectErrors() {
    DropDownItem defaultItem = new DropDownItem("", "default item");
    List<DropDownItem> options = List.of(
        new DropDownItem("1", "item 1"),
        new DropDownItem("2", "item 2")
    );
    
    DropDownItemPanel panel1 = new DropDownItemPanel(options, defaultItem);
    DropDownItemPanel panel2 = new DropDownItemPanel(options, defaultItem);
    
    addChangeListener(panel1.getModel());
    addChangeListener(panel2.getModel());
    
    model.addProject(panel1);
    model.addProject(panel2);
    
    List<String> errors = List.of(
        "error 1", "error 2"
    );
    
    model.setProjectErrors(errors);
    
    assertChangeEvents(
        DropDownItemModel.UPDATE_ITEM_ERROR,
        errors.stream()
            .map(e -> (String) null)
            .toList(),
        errors,
        (v) -> v
    );
    assertEquals("error 1", panel1.getModel().getItemError());
    assertEquals("error 2", panel2.getModel().getItemError());
  }

  @Test
  void clearProjects() {
    List<DropDownItem> options = List.of(
        new DropDownItem("1", "value 1"),
        new DropDownItem("2", "value 2")
    );
    
    DropDownItem defaultOption = new DropDownItem(
        "", "default option"
    );
    
    DropDownItemPanel panel1 = new DropDownItemPanel(options, defaultOption);
    DropDownItemPanel panel2 = new DropDownItemPanel(options, defaultOption);
    
    List<DropDownItemModel> models = List.of(
        panel1.getModel(), panel2.getModel()
    );
    
    model.addProject(panel1);
    model.addProject(panel2);
    
    model.clearProjects();
    
    assertChangeEvent(Events.CLEAR_PROJECTS, models, Collections.emptyList());
    assertEquals(Collections.emptyList(), model.getProjects());
  }

  @Test
  void addProject() {
    List<DropDownItem> options = List.of(
        new DropDownItem("1", "value 1"),
        new DropDownItem("2", "value 2")
    );
    
    DropDownItem defaultOption = new DropDownItem(
        "", "default option"
    );
    
    DropDownItemPanel panel = new DropDownItemPanel(options, defaultOption);
    
    model.addProject(panel);
    
    assertChangeEvent(Events.ADD_PROJECT, null, panel);
    assertEquals(panel.getModel(), model.getProjects().get(0));
    assertEquals(1, model.getProjects().size());
  }

  @Test
  void removeProject() {
    List<DropDownItem> options = List.of(
        new DropDownItem("1", "value 1"),
        new DropDownItem("2", "value 2")
    );

    DropDownItem defaultOption = new DropDownItem(
        "", "default option"
    );

    DropDownItemPanel panel = new DropDownItemPanel(options, defaultOption);

    model.addProject(panel);
    model.removeProject(panel);
    
    assertChangeEvent(Events.REMOVE_PROJECT, panel, null);
    assertEquals(Collections.emptyList(), model.getProjects());
  }

  @Test
  void setExistingRecord() {
    assertPropertyChange(
        Events.UPDATE_EXISTING_RECORD, 
        model::getExistingRecord,
        model::setExistingRecord,
        new DropDownItem("1", "value 1"),
        new DropDownItem("2", "value 2"),
        CruiseDataDatastore.UNSELECTED_CRUISE
    );
  }
}