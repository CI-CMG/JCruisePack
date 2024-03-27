package edu.colorado.cires.cruisepack.app.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.colorado.cires.cruisepack.app.config.JacksonFilePathModule;
import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ProjectDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.service.metadata.OmicsData;
import edu.colorado.cires.cruisepack.app.ui.view.common.OptionPaneGenerator;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import edu.colorado.cires.cruisepack.xml.organization.Organization;
import edu.colorado.cires.cruisepack.xml.organization.OrganizationData;
import edu.colorado.cires.cruisepack.xml.person.Person;
import edu.colorado.cires.cruisepack.xml.person.PersonData;
import edu.colorado.cires.cruisepack.xml.projects.Project;
import edu.colorado.cires.cruisepack.xml.projects.ProjectData;
import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class SqliteMigratorTest {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .setDefaultPropertyInclusion(Include.NON_NULL)
      .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
      .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
      .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
      .registerModule(new JacksonFilePathModule());


  @Test
  public void testNoConflicts() throws Exception {
    Path oldCp = Paths.get("src/test/resources/oldcp");
    Path expected = oldCp.resolve("expected");

    int[] uuid = {0};
    Supplier<String> uuidGenerator = () -> {
      uuid[0] = uuid[0] + 1;
      return String.valueOf(uuid[0]);
    };

    CruiseDataDatastore cruiseDataDatastore = mock(CruiseDataDatastore.class);
    OrganizationDatastore organizationDatastore = mock(OrganizationDatastore.class);
    PersonDatastore personDatastore = mock(PersonDatastore.class);
    ProjectDatastore projectDatastore = mock(ProjectDatastore.class);
    ShipDatastore shipDatastore = mock(ShipDatastore.class);
    InstrumentDatastore instrumentDatastore = mock(InstrumentDatastore.class);

    List<Organization> organizations = new ArrayList<>();
    List<Person> people = new ArrayList<>();
    List<Project> projects = new ArrayList<>();

    when(organizationDatastore.findByName(any()))
        .thenAnswer((invocation) -> organizations.stream().filter(o -> o.getName().equals(invocation.getArgument(0, String.class))).findFirst());
    doAnswer((invocation) -> {
      organizations.add(invocation.getArgument(0, Organization.class));
      return null;
    }).when(organizationDatastore).save(any(Organization.class));

    when(personDatastore.findByName(any()))
        .thenAnswer((invocation) -> people.stream().filter(o -> o.getName().equals(invocation.getArgument(0, String.class))).findFirst());
    doAnswer((invocation) -> {
      people.add(invocation.getArgument(0, Person.class));
      return null;
    }).when(personDatastore).save(any(Person.class));

    when(projectDatastore.findByName(any()))
        .thenAnswer((invocation) -> projects.stream().filter(o -> o.getName().equals(invocation.getArgument(0, String.class))).findFirst());
    doAnswer((invocation) -> {
      projects.add(invocation.getArgument(0, Project.class));
      return null;
    }).when(projectDatastore).save(any(Project.class));

    when(shipDatastore.getShipUuidForName("Henry B. Bigelow (HB)")).thenReturn("5db5f560-edf2-11e1-aff1-0800200c9a66");
    when(shipDatastore.getShipUuidForName("Okeanos Explorer (EX)")).thenReturn("66132cd0-6a9c-11e0-ae3e-0800200c9a66");
    when(shipDatastore.getShipUuidForName("Unlisted Ship")).thenReturn("9999");
    when(shipDatastore.getShipUuidForName("Falkor (FK)")).thenReturn("391b3824-b82f-40c1-9a53-ef1cdfd9f0c6");
    when(shipDatastore.getShipUuidForName("Oscar Dyson (DY)")).thenReturn("a24caa2a-1c39-41b9-ba7a-eede10ccc4f6");

    when(instrumentDatastore.getInstrumentUuidForDatasetTypeAndInstrumentName(InstrumentGroupName.fromLongName("CTD").getShortName(), "SBE 911plus")).thenReturn("45080730-69c6-11e0-ae3e-0800200c9a66");
    when(instrumentDatastore.getInstrumentUuidForDatasetTypeAndInstrumentName(InstrumentGroupName.fromLongName("Water Column Sonar").getShortName(), "Simrad EK60")).thenReturn("b7ae3f00-701c-11e0-a1f0-0800200c9a66");
    when(instrumentDatastore.getInstrumentUuidForDatasetTypeAndInstrumentName(InstrumentGroupName.fromLongName("Water Column Sonar").getShortName(), "Simrad EK80")).thenReturn("cfc2ac19-b32a-4753-a934-efe8a57cc496");
    when(instrumentDatastore.getInstrumentUuidForDatasetTypeAndInstrumentName(InstrumentGroupName.fromLongName("Water Column Sonar").getShortName(), "Kongsberg EM302")).thenReturn("794feae0-679a-11e0-ae3e-0800200c9a66");
    when(instrumentDatastore.getInstrumentUuidForDatasetTypeAndInstrumentName(InstrumentGroupName.fromLongName("Navigation").getShortName(), "C-NAV 3050 GPS")).thenReturn("41f56f00-69d1-11e0-ae3e-0800200c9a66");
    when(instrumentDatastore.getInstrumentUuidForDatasetTypeAndInstrumentName(InstrumentGroupName.fromLongName("Multibeam Bathymetry").getShortName(), "Simrad ME70")).thenReturn("26fca400-154a-11e1-be50-0800200c9a66");
    when(instrumentDatastore.getInstrumentUuidForDatasetTypeAndInstrumentName(InstrumentGroupName.fromLongName("Water Column Sonar").getShortName(), "Simrad ME70")).thenReturn("26fca400-154a-11e1-be50-0800200c9a66");
    when(instrumentDatastore.getInstrumentUuidForDatasetTypeAndInstrumentName(InstrumentGroupName.fromLongName("Multibeam Bathymetry").getShortName(), "Kongsberg EM2040")).thenReturn("69bc77a-9244-43fe-b1b8-d0c937017f5c");
    when(instrumentDatastore.getInstrumentUuidForDatasetTypeAndInstrumentName(InstrumentGroupName.fromLongName("Multibeam Bathymetry").getShortName(), "Kongsberg EM304")).thenReturn("69bc77a-9244-43fe-b1b8-d0c937017f5c");
    when(instrumentDatastore.getInstrumentUuidForDatasetTypeAndInstrumentName(InstrumentGroupName.fromLongName("Magnetics").getShortName(), "Kokusai Electronics PMM-100")).thenReturn("5de5f056-73e4-4505-8949-a0387ecfde72");


    SqliteMigrator migrator = new SqliteMigrator(
        uuidGenerator,
        objectMapper,
        cruiseDataDatastore,
        organizationDatastore,
        personDatastore,
        projectDatastore,
        shipDatastore,
        instrumentDatastore,
        mock(OptionPaneGenerator.class)
    );
    migrator.migrate(oldCp);

    List<String> expectedOrgs = readOrganizations(expected.resolve("organizations.xml")).getOrganizations().getOrganizations().stream().map(SqliteMigratorTest::toXml).toList();
    ArgumentCaptor<Organization> orgCaptor = ArgumentCaptor.forClass(Organization.class);
    verify(organizationDatastore, times(expectedOrgs.size())).save(orgCaptor.capture());
    List<String> capturedOrgs = orgCaptor.getAllValues().stream().map(SqliteMigratorTest::toXml).collect(Collectors.toList());
    assertEquals(expectedOrgs, capturedOrgs);

    List<String> expectedPeople = readPeople(expected.resolve("people.xml")).getPeople().getPersons().stream().map(SqliteMigratorTest::toXml).collect(Collectors.toList());
    ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
    verify(personDatastore, times(expectedPeople.size())).save(personCaptor.capture());
    List<String> capturedPeople = personCaptor.getAllValues().stream().map(SqliteMigratorTest::toXml).collect(Collectors.toList());
    assertEquals(expectedPeople, capturedPeople);

    List<String> expectedProjects = readProjects(expected.resolve("projects.xml")).getProjects().getProjects().stream().map(SqliteMigratorTest::toXml).collect(Collectors.toList());
    ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
    verify(projectDatastore, times(expectedProjects.size())).save(projectCaptor.capture());
    List<String> capturedProjects = projectCaptor.getAllValues().stream().map(SqliteMigratorTest::toXml).collect(Collectors.toList());
    assertEquals(expectedProjects, capturedProjects);

    List<String> expectedCruises = new ArrayList<>();
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("EX2120.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("EX2102.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("Cruise01.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("test4.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("EX2126.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1304_Leg_1.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1304_Leg_2.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1304_Leg_3.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1304_Leg_4.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1506_Leg_1.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1506_Leg_2.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1601_Leg_1.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1601_Leg_2.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1601_Leg_3.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1603_Leg_1.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1603_Leg_2.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1604_Leg_1.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1604_Leg_3.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1604_Leg_4.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1604_Leg_5.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1701.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1702_Leg_1.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1702_Leg_2.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1702_Leg_3.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1703.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1806_Leg_2.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1806_Leg_3.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1806_Leg_4.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB1907.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB2006.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB2101_Leg_1.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB2101_Leg_2.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB2101_Leg_3.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB2102_Leg_1.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB2102_Leg_2.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB2103_Leg_1.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("HB2103_Leg_2.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("sdsdsd_tttttt.json").toFile(), CruiseData.class)));
    expectedCruises.add(objectMapper.writeValueAsString(objectMapper.readValue(expected.resolve("sdf_sdd.json").toFile(), CruiseData.class)));

    ArgumentCaptor<CruiseData> saveCaptor = ArgumentCaptor.forClass(CruiseData.class);
    verify(cruiseDataDatastore, times(expectedCruises.size())).saveCruise(saveCaptor.capture());
    List<String> capturedSaves = saveCaptor.getAllValues().stream().map(cruiseData -> {
      try {
        return objectMapper.writeValueAsString(cruiseData);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }).collect(Collectors.toList());

    assertEquals(expectedCruises.size(), capturedSaves.size());
    for (int i = 0; i < expectedCruises.size(); i++) {
      CruiseData exp = objectMapper.readValue(expectedCruises.get(i), CruiseData.class);
      CruiseData act = objectMapper.readValue(capturedSaves.get(i), CruiseData.class);
      if (exp.getOmics() != null) {
        assertEquals(((OmicsData) exp.getOmics()).trackingPath(), ((OmicsData) act.getOmics()).trackingPath());
        assertNotNull(((OmicsData) exp.getOmics()).trackingPath());
        assertEquals(exp.getOmics().ncbiAccession(), act.getOmics().ncbiAccession());
        assertNotNull(exp.getOmics().ncbiAccession());
        assertTrue(((OmicsData) act.getOmics()).samplingConducted());
        assertEquals(((OmicsData) exp.getOmics()).samplingConducted(), ((OmicsData) act.getOmics()).samplingConducted());
        assertEquals(expectedCruises.get(i), capturedSaves.get(i));
      }
    }


  }

  private static String toXml(Object o) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JAXB.marshal(o, outputStream);
    return outputStream.toString(StandardCharsets.UTF_8);
  }

  private OrganizationData readOrganizations(Path path) throws IOException, JAXBException {
    try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      return (OrganizationData) JAXBContext.newInstance(OrganizationData.class)
          .createUnmarshaller().unmarshal(reader);
    }
  }

  private PersonData readPeople(Path path) throws IOException, JAXBException {
    try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      return (PersonData) JAXBContext.newInstance(PersonData.class)
          .createUnmarshaller().unmarshal(reader);
    }
  }

  private ProjectData readProjects(Path path) throws IOException, JAXBException {
    try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      return (ProjectData) JAXBContext.newInstance(ProjectData.class)
          .createUnmarshaller().unmarshal(reader);
    }
  }
}