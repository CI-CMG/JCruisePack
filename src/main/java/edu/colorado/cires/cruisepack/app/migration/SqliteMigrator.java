package edu.colorado.cires.cruisepack.app.migration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ProjectDatastore;
import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.migration.jpa.CruiseDataEntity;
import edu.colorado.cires.cruisepack.app.migration.jpa.OrganizationEntity;
import edu.colorado.cires.cruisepack.app.migration.jpa.PersonEntity;
import edu.colorado.cires.cruisepack.app.migration.jpa.ProjectEntity;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.service.metadata.InstrumentData;
import edu.colorado.cires.cruisepack.app.service.metadata.MetadataAuthor;
import edu.colorado.cires.cruisepack.app.service.metadata.OmicsData;
import edu.colorado.cires.cruisepack.app.service.metadata.PeopleOrg;
import edu.colorado.cires.cruisepack.app.ui.model.ErrorModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.OptionPaneGenerator;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import edu.colorado.cires.cruisepack.xml.organization.Organization;
import edu.colorado.cires.cruisepack.xml.person.Person;
import edu.colorado.cires.cruisepack.xml.projects.Project;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SqliteMigrator {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(SqliteMigrator.class);

  private final Supplier<String> uuidGenerator;
  private final ObjectMapper objectMapper;
  private final CruiseDataDatastore cruiseDataDatastore;
  private final OrganizationDatastore organizationDatastore;
  private final PersonDatastore personDatastore;
  private final ProjectDatastore projectDatastore;
  private final ShipDatastore shipDatastore;
  private final InstrumentDatastore instrumentDatastore;
  private final OptionPaneGenerator optionPaneGenerator;

  @Autowired
  public SqliteMigrator(
      Supplier<String> uuidGenerator,
      ObjectMapper objectMapper,
      CruiseDataDatastore cruiseDataDatastore,
      OrganizationDatastore organizationDatastore,
      PersonDatastore personDatastore,
      ProjectDatastore projectDatastore,
      ShipDatastore shipDatastore,
      InstrumentDatastore instrumentDatastore, OptionPaneGenerator optionPaneGenerator) {
    this.uuidGenerator = uuidGenerator;
    this.objectMapper = objectMapper;
    this.cruiseDataDatastore = cruiseDataDatastore;
    this.organizationDatastore = organizationDatastore;
    this.personDatastore = personDatastore;
    this.projectDatastore = projectDatastore;
    this.shipDatastore = shipDatastore;
    this.instrumentDatastore = instrumentDatastore;
    this.optionPaneGenerator = optionPaneGenerator;
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

  private Project toProject(ProjectEntity db) {
    Project project = new Project();
    project.setUuid(uuidGenerator.get());
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

  private Person toPerson(PersonEntity db) {
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
    person.setUuid(StringUtils.isBlank(db.getUuid()) ? uuidGenerator.get() : db.getUuid().trim());
    person.setUse("Y".equals(db.getUse()));
    person.setOrcid(normalize(db.getOrcId()));
    return person;
  }

  private Organization toOrganization(OrganizationEntity db) {
    Organization organization = new Organization();
    organization.setName(normalize(db.getName()));
    organization.setStreet(normalize(db.getStreet()));
    organization.setCity(normalize(db.getCity()));
    organization.setState(normalize(db.getState()));
    organization.setZip(normalize(db.getZip()));
    organization.setCountry(normalize(db.getCountry()));
    organization.setEmail(normalize(db.getEmail()));
    organization.setPhone(normalize(db.getPhone()));
    organization.setUuid(StringUtils.isBlank(db.getUuid()) ? uuidGenerator.get() : db.getUuid().trim());
    organization.setUse("Y".equals(db.getUse()));
    return organization;
  }

  private PeopleOrg personUpdateUuidOrCreate(PeopleOrg person) {
    String uuid = personDatastore.findByName(person.getName()).map(Person::getUuid).orElse(null);
    if (uuid != null) {
      return PeopleOrg.builder(person).withUuid(uuid).build();
    }
    Person newPerson = new Person();
    newPerson.setName(person.getName());
    newPerson.setUuid(uuidGenerator.get());
    newPerson.setUse(true);
    personDatastore.save(newPerson);
    return PeopleOrg.builder().withName(newPerson.getName()).withUuid(newPerson.getUuid()).build();
  }

  private PeopleOrg organizationUpdateUuidOrCreate(PeopleOrg org) {
    String uuid = organizationDatastore.findByName(org.getName()).map(Organization::getUuid).orElse(null);
    if (uuid != null) {
      return PeopleOrg.builder(org).withUuid(uuid).build();
    }
    Organization newOrg = new Organization();
    newOrg.setName(org.getName());
    newOrg.setUuid(uuidGenerator.get());
    newOrg.setUse(true);
    organizationDatastore.save(newOrg);
    return PeopleOrg.builder().withName(newOrg.getName()).withUuid(newOrg.getUuid()).build();
  }

  private List<PeopleOrg> getPeople(String json) {
    if (StringUtils.isNotBlank(json)) {
      try {
        List<PeopleOrg> people = objectMapper.readValue(json, new TypeReference<>() {
        });
        List<PeopleOrg> checkedPeople = new ArrayList<>();
        for (PeopleOrg person : people) {
          checkedPeople.add(personUpdateUuidOrCreate(person));
        }
        return checkedPeople;
      } catch (JsonProcessingException e) {
        throw new RuntimeException("Unable to parse people", e);
      }
    }
    return Collections.emptyList();
  }

  private List<PeopleOrg> getOrgs(String json) {
    if (StringUtils.isNotBlank(json)) {
      try {
        List<PeopleOrg> orgs = objectMapper.readValue(json, new TypeReference<>() {
        });
        List<PeopleOrg> checkedOrgs = new ArrayList<>();
        for (PeopleOrg org : orgs) {
          checkedOrgs.add(organizationUpdateUuidOrCreate(org));
        }
        return checkedOrgs;
      } catch (JsonProcessingException e) {
        throw new RuntimeException("Unable to parse people / orgs", e);
      }
    }
    return Collections.emptyList();
  }

  private List<String> getProjects(String json) {
    if (StringUtils.isNotBlank(json)) {
      try {
        List<String> names = objectMapper.readValue(json, new TypeReference<>() {
        });
        for (String name : names) {
          projectCheckOrCreate(name);
        }
        return names;
      } catch (JsonProcessingException e) {
        throw new RuntimeException("Unable to parse projects", e);
      }
    }
    return Collections.emptyList();
  }

  private void projectCheckOrCreate(String project) {
    if (projectDatastore.findByName(project).isEmpty()) {
      Project newProject = new Project();
      newProject.setName(project);
      newProject.setUuid(uuidGenerator.get());
      newProject.setUse(true);
      projectDatastore.save(newProject);
    }
  }

  private OmicsData getOmics(String json) {
    if (StringUtils.isNotBlank(json)) {
      if (json.equals("{}")){
        return null;
      }
      try {
        ObjectNode obj = (ObjectNode) objectMapper.readTree(json);
        JsonNode poc = obj.get("omics_poc");
        if (poc != null) {
          ObjectNode newPoc = objectMapper.createObjectNode();
          String name = poc.textValue().trim();
          PeopleOrg person = personUpdateUuidOrCreate(PeopleOrg.builder().withName(name).build());
          newPoc.put("name", person.getName());
          newPoc.put("uuid", person.getUuid());
          obj.replace("omics_poc", newPoc);
        }
        return OmicsData.create(
            objectMapper.readValue(obj.toString(), OmicsData.class)
        );
      } catch (JsonProcessingException e) {
        throw new RuntimeException("Unable to parse omics", e);
      }
    }
    return null;
  }

  private List<InstrumentData> getInstruments(String json) {
    if (StringUtils.isNotBlank(json)) {
      try {
        List<InstrumentData> instruments = objectMapper.readValue(json, new TypeReference<>() {
        });
        List<InstrumentData> checkedInstruments = new ArrayList<>();
        for (InstrumentData instrument : instruments) {
          String uuid = instrumentDatastore.getInstrumentUuidForDatasetTypeAndInstrumentName(
              InstrumentGroupName.fromLongName(instrument.getType()).getShortName(), instrument.getInstrument()
          );
          if (uuid == null) {
            String message = "Unable to find instrument '" + instrument.getType() + ":" + instrument.getInstrument() + "'";
            optionPaneGenerator.createMessagePane(
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            throw new IllegalStateException(message);
          }
          checkedInstruments.add(InstrumentData.builder(instrument).withUuid(uuid).build());
        }
        return checkedInstruments;
      } catch (JsonProcessingException e) {
        throw new RuntimeException("Unable to parse instruments", e);
      }
    }
    return Collections.emptyList();
  }

  private CruiseData toCruiseMetadata(CruiseDataEntity db) {

    String metadataAuthorName = normalize(db.getMetadataAuthor(), "Select Metadata Author");
    MetadataAuthor metadataAuthor = null;
    if (metadataAuthorName != null) {
      PeopleOrg person = personUpdateUuidOrCreate(PeopleOrg.builder().withName(metadataAuthorName).build());
      metadataAuthor = MetadataAuthor.builder()
          .withName(person.getName())
          .withUuid(person.getUuid())
          .build();
    }

    String shipName = normalize(db.getShip(), "Select Ship Name");
    String shipUuid = shipName == null ? null : shipDatastore.getShipUuidForName(shipName);

    if (shipName != null && shipUuid == null) {
      String message = "Unable to find ship with name '" + shipName + "'";
      optionPaneGenerator.createMessagePane(
          message,
          "Error",
          JOptionPane.ERROR_MESSAGE
      );
      throw new IllegalStateException(message);
    }

    return CruiseData.builder()
        .withUse("Y".equals(db.getUse()))
        .withCruiseId(normalize(db.getCruiseId()))
        .withSegmentId(normalize(db.getSegmentId()))
        .withPackageId(normalize(db.getPackageId(), "Select Existing Record"))
        .withMasterReleaseDate(normalize(db.getMasterReleaseDate()))
        .withShip(shipName)
        .withShipUuid(shipUuid)
        .withDeparturePort(normalize(db.getDeparturePort(), "Select Departure Port"))
        .withDepartureDate(normalize(db.getDepartureTime()))
        .withArrivalPort(normalize(db.getArrivalPort(), "Select Arrival Port"))
        .withArrivalDate(normalize(db.getArrivalTime()))
        .withSeaArea(normalize(db.getSeaArea(), "Select Sea Name"))
        .withCruiseTitle(normalize(db.getCruiseTitle()))
        .withCruisePurpose(normalize(db.getPurposeText()))
        .withCruiseDescription(normalize(db.getAbstractText()))
        .withSponsors(getOrgs(db.getSponsors()))
        .withFunders(getOrgs(db.getFunders()))
        .withScientists(getPeople(db.getScientists()))
        .withProjects(getProjects(db.getProjects()))
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



    try (SessionFactory sessionFactory = createSessionFactory(
        localData,
        OrganizationEntity.class,
        PersonEntity.class,
        ProjectEntity.class
    )) {
      sessionFactory.inTransaction(session -> {
        session.createSelectionQuery("from PersonEntity", PersonEntity.class)
            .getResultList().stream()
            .map(this::toPerson)
            .filter(person -> personDatastore.findByName(person.getName()).isEmpty())
            .forEach(personDatastore::save);

        session.createSelectionQuery("from OrganizationEntity", OrganizationEntity.class)
            .getResultList().stream()
            .map(this::toOrganization)
            .filter(org -> organizationDatastore.findByName(org.getName()).isEmpty())
            .forEach(organizationDatastore::save);

        session.createSelectionQuery("from ProjectEntity", ProjectEntity.class)
            .getResultList().stream()
            .map(this::toProject)
            .filter(project -> projectDatastore.findByName(project.getName()).isEmpty())
            .forEach(projectDatastore::save);
      });
    }

    try (SessionFactory sessionFactory = createSessionFactory(cruiseData, CruiseDataEntity.class)) {
      sessionFactory.inTransaction(session -> {
        session.createSelectionQuery("from CruiseDataEntity", CruiseDataEntity.class)
            .getResultList().stream()
            .map(this::toCruiseMetadata)
            .filter(cruise -> cruise.getPackageId() != null)
            .forEach(d -> {
              try {
                cruiseDataDatastore.saveCruise(d);
              } catch (Exception e) {
                LOGGER.error("Failed to migrate cruise: {}", d.getPackageId());
                optionPaneGenerator.createMessagePane(
                    String.format(
                        "Failed to migrate %s: %s", d.getPackageId(), e.getMessage()
                    ),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
              }
            });
      });
    }

  }

}
