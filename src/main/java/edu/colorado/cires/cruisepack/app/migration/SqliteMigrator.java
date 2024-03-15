package edu.colorado.cires.cruisepack.app.migration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ProjectDatastore;
import edu.colorado.cires.cruisepack.app.migration.jpa.CruiseDataEntity;
import edu.colorado.cires.cruisepack.app.migration.jpa.OrganizationEntity;
import edu.colorado.cires.cruisepack.app.migration.jpa.PersonEntity;
import edu.colorado.cires.cruisepack.app.migration.jpa.ProjectEntity;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.service.metadata.InstrumentData;
import edu.colorado.cires.cruisepack.app.service.metadata.MetadataAuthor;
import edu.colorado.cires.cruisepack.app.service.metadata.Omics;
import edu.colorado.cires.cruisepack.app.service.metadata.PeopleOrg;
import edu.colorado.cires.cruisepack.xml.organization.Organization;
import edu.colorado.cires.cruisepack.xml.person.Person;
import edu.colorado.cires.cruisepack.xml.projects.Project;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class SqliteMigrator {

  private final ObjectMapper objectMapper;
  private final CruiseDataDatastore cruiseDataDatastore;
  private final OrganizationDatastore organizationDatastore;
  private final PersonDatastore personDatastore;
  private final ProjectDatastore projectDatastore;

  public SqliteMigrator(
      ObjectMapper objectMapper,
      CruiseDataDatastore cruiseDataDatastore,
      OrganizationDatastore organizationDatastore,
      PersonDatastore personDatastore,
      ProjectDatastore projectDatastore
  ) {
    this.objectMapper = objectMapper;
    this.cruiseDataDatastore = cruiseDataDatastore;
    this.organizationDatastore = organizationDatastore;
    this.personDatastore = personDatastore;
    this.projectDatastore = projectDatastore;
  }

  private static SessionFactory createSessionFactory(Path file, Class<?>... classes) {
    return new MetadataSources(
        new StandardServiceRegistryBuilder()
            .applySetting("hibernate.connection.url", "jdbc:sqlite:" + file.toString().replaceAll("\\\\", "/"))
            .build())
        .addAnnotatedClasses(classes)
        .buildMetadata()
        .buildSessionFactory();
  }

  private static Project toProject(ProjectEntity db) {
    Project project = new Project();
    project.setUuid(UUID.randomUUID().toString());
    project.setName(normalize(db.getName()));
    project.setUse("Y".equals(db.getUse()));
    return project;
  }

  private static String normalize(String str, String placeHolder) {
    String normalized = normalize(str);
    if (normalized == null || normalized.equals(placeHolder)) {
      return null;
    }
    return normalized;
  }

  private static String normalize(String str) {
    if (StringUtils.isBlank(str)) {
      return null;
    }
    return str.trim();
  }

  private static Person toPerson(PersonEntity db) {
    Person person = new Person();
    person.setName(normalize(db.getName()));
    person.setOrganization(normalize(db.getOrganization()));
    person.setPosition(normalize(db.getPosition()));
    person.setStreet(normalize(db.getStreet()));
    person.setCity(normalize(db.getCity()));
    person.setState(normalize(db.getState()));
    person.setZip(normalize(db.getZip()));
    person.setCountry(normalize(db.getCountry()));
    person.setEmail(normalize(db.getEmail()));
    person.setPhone(normalize(db.getPhone()));
    person.setUuid(StringUtils.isBlank(db.getUuid()) ? UUID.randomUUID().toString() : db.getUuid().trim());
    person.setUse("Y".equals(db.getUse()));
    person.setOrcid(normalize(db.getOrcId()));
    return person;
  }

  private static Organization toOrganization(OrganizationEntity db) {
    Organization organization = new Organization();
    organization.setName(normalize(db.getName()));
    organization.setStreet(normalize(db.getStreet()));
    organization.setCity(normalize(db.getCity()));
    organization.setState(normalize(db.getState()));
    organization.setZip(normalize(db.getZip()));
    organization.setCountry(normalize(db.getCountry()));
    organization.setEmail(normalize(db.getEmail()));
    organization.setPhone(normalize(db.getPhone()));
    organization.setUuid(StringUtils.isBlank(db.getUuid()) ? UUID.randomUUID().toString() : db.getUuid().trim());
    organization.setUse("Y".equals(db.getUse()));
    return organization;
  }

  private List<PeopleOrg> getPeopleOrgs(String json) {
    if (StringUtils.isNotBlank(json)) {
      try {
        return objectMapper.readValue(json, new TypeReference<>() {
        });
      } catch (JsonProcessingException e) {
        throw new RuntimeException("Unable to parse people / orgs", e);
      }
    }
    return Collections.emptyList();
  }

  private List<String> getStringList(String json) {
    if (StringUtils.isNotBlank(json)) {
      try {
        return objectMapper.readValue(json, new TypeReference<>() {
        });
      } catch (JsonProcessingException e) {
        throw new RuntimeException("Unable to parse projects", e);
      }
    }
    return Collections.emptyList();
  }

  private Omics getOmics(String json) {
    if (StringUtils.isNotBlank(json)) {
      if (json.equals("{}")){
        return null;
      }
      try {
        ObjectNode obj = (ObjectNode) objectMapper.readTree(json);
        JsonNode poc = obj.get("omics_poc");
        if (poc != null) {
          ObjectNode newPoc = objectMapper.createObjectNode();
          newPoc.put("name", poc.textValue());
          obj.replace("omics_poc", newPoc);
        }
        return objectMapper.readValue(obj.toString(), Omics.class);
      } catch (JsonProcessingException e) {
        throw new RuntimeException("Unable to parse omics", e);
      }
    }
    return null;
  }

  private List<InstrumentData> getInstruments(String json) {
    if (StringUtils.isNotBlank(json)) {
      try {
        return objectMapper.readValue(json, new TypeReference<>() {
        });
      } catch (JsonProcessingException e) {
        throw new RuntimeException("Unable to parse instruments", e);
      }
    }
    return Collections.emptyList();
  }

  private CruiseData toCruiseMetadata(CruiseDataEntity db) {

    String metadataAuthorName = normalize(db.getMetadataAuthor(), "Select Metadata Author");
    String metadataAuthorUuid = normalize(db.getMetadataAuthorUuid());
    MetadataAuthor metadataAuthor = null;
    if (metadataAuthorName != null && metadataAuthorUuid != null) {
      metadataAuthor = MetadataAuthor.builder()
          .withName(metadataAuthorName)
          .withUuid(metadataAuthorUuid)
          .build();
    }

    return CruiseData.builder()
        .withUse("Y".equals(db.getUse()))
        .withCruiseId(normalize(db.getCruiseId()))
        .withSegmentId(normalize(db.getSegmentId()))
        .withPackageId(normalize(db.getPackageId(), "Select Existing Record"))
        .withMasterReleaseDate(normalize(db.getMasterReleaseDate()))
        .withShip(normalize(db.getShip(), "Select Ship Name"))
        .withShipUuid(normalize(db.getShipUuid()))
        .withDeparturePort(normalize(db.getDeparturePort(), "Select Departure Port"))
        .withDepartureDate(normalize(db.getDepartureTime()))
        .withArrivalPort(normalize(db.getArrivalPort(), "Select Arrival Port"))
        .withArrivalDate(normalize(db.getArrivalTime()))
        .withSeaArea(normalize(db.getSeaArea(), "Select Sea Name"))
        .withCruiseTitle(normalize(db.getCruiseTitle()))
        .withCruisePurpose(normalize(db.getPurposeText()))
        .withCruiseDescription(normalize(db.getAbstractText()))
        .withSponsors(getPeopleOrgs(db.getSponsors()))
        .withFunders(getPeopleOrgs(db.getFunders()))
        .withScientists(getPeopleOrgs(db.getScientists()))
        .withProjects(getStringList(db.getProjects()))
        .withOmics(getOmics(db.getOmics()))
        .withMetadataAuthor(metadataAuthor)
        .withInstruments(getInstruments(db.getDatasets()))
        .withDocumentsPath(normalize(db.getDocsPath()))
        .withPackageDirectory(normalize(db.getDestinationPath()))
        .build();
  }

  public void migrate(Path oldCruisePackDir) {
    Path database = oldCruisePackDir.resolve("database").toAbsolutePath().normalize();
    Path cruiseData = database.resolve("cruiseData.sqlite");
    Path localData = database.resolve("localData.sqlite");

    try (SessionFactory sessionFactory = createSessionFactory(cruiseData, CruiseDataEntity.class)) {
      sessionFactory.inTransaction(session -> {
        session.createSelectionQuery("from CruiseDataEntity", CruiseDataEntity.class)
            .getResultList().stream()
            .map(this::toCruiseMetadata)
            .filter(cruise -> cruise.getPackageId() != null)
            .forEach(cruiseDataDatastore::saveCruise);
      });
    }

    try (SessionFactory sessionFactory = createSessionFactory(
        localData,
        OrganizationEntity.class,
        PersonEntity.class,
        ProjectEntity.class
    )) {
      sessionFactory.inTransaction(session -> {
        session.createSelectionQuery("from PersonEntity", PersonEntity.class)
            .getResultList().stream()
            .map(SqliteMigrator::toPerson)
            .forEach(personDatastore::save);

        session.createSelectionQuery("from OrganizationEntity", OrganizationEntity.class)
            .getResultList().stream()
            .map(SqliteMigrator::toOrganization)
            .forEach(organizationDatastore::save);

        session.createSelectionQuery("from ProjectEntity", ProjectEntity.class)
            .getResultList().stream()
            .map(SqliteMigrator::toProject)
            .forEach(projectDatastore::save);
      });
    }

  }

}
