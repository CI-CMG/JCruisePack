package edu.colorado.cires.cruisepack.app.migration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ProjectDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
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
import java.util.List;
import java.util.Objects;
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
      .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);


  @Test
  public void test() throws Exception {
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
    SqliteMigrator migrator = new SqliteMigrator(
        uuidGenerator,
        objectMapper,
        cruiseDataDatastore,
        organizationDatastore,
        personDatastore,
        projectDatastore);
    migrator.migrate(oldCp);

    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("EX2120.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("EX2102.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("Cruise01.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("test4.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("EX2126.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1304_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1304_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1304_Leg_3.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1304_Leg_4.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1506_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1506_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1601_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1601_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1601_Leg_3.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1603_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1603_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1604_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1604_Leg_3.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1604_Leg_4.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1604_Leg_5.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1701.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1702_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1702_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1702_Leg_3.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1703.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1806_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1806_Leg_3.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1806_Leg_4.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB1907.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2006.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2101_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2101_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2101_Leg_3.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2102_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2102_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2103_Leg_1.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("HB2103_Leg_2.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("sdsdsd_tttttt.json").toFile(), CruiseData.class));
    verify(cruiseDataDatastore).saveCruise(objectMapper.readValue(expected.resolve("sdf_sdd.json").toFile(), CruiseData.class));

    verifyNoMoreInteractions(cruiseDataDatastore);

    List<String> expectedOrgs = readOrganizations(expected.resolve("organizations.xml")).getOrganizations().getOrganizations().stream().map(SqliteMigratorTest::toXml).collect(Collectors.toList());
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