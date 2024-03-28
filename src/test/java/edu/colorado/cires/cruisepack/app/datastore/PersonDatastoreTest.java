package edu.colorado.cires.cruisepack.app.datastore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.model.PersonModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.person.Person;
import edu.colorado.cires.cruisepack.xml.person.PersonData;
import edu.colorado.cires.cruisepack.xml.person.PersonList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class PersonDatastoreTest extends OverridableXMLDatastoreTest<PersonData> {
  
  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(TEST_PATH.toString());
  }
  
  private final PersonDatastore datastore = new PersonDatastore(SERVICE_PROPERTIES);

  @Test
  void init() {
    datastore.init();

    Set<NameUUIDPair> expected = readFile(PersonData.class).getPeople().getPersons().stream()
        .map(p -> new NameUUIDPair(p.getUuid(), p.getName()))
        .collect(Collectors.toSet());
    expected.add(new NameUUIDPair(
        PersonDatastore.UNSELECTED_PERSON.getId(), PersonDatastore.UNSELECTED_PERSON.getValue()
    ));
    
    Set<NameUUIDPair> actual = datastore.getAllPersonDropDowns().stream()
        .map(d -> new NameUUIDPair(d.getId(), d.getValue()))
        .collect(Collectors.toSet());
    
    assertEquals(expected, actual);
  }

  @Test
  void getEnabledPersonDropDowns() {
    datastore.init();
    
    Set<NameUUIDPair> expected = readFile(PersonData.class).getPeople().getPersons().stream()
        .filter(Person::isUse)
        .map(p -> new NameUUIDPair(p.getUuid(), p.getName()))
        .collect(Collectors.toSet());
    expected.add(new NameUUIDPair(
        PersonDatastore.UNSELECTED_PERSON.getId(), PersonDatastore.UNSELECTED_PERSON.getValue()
    ));
    
    Set<NameUUIDPair> actual = datastore.getEnabledPersonDropDowns().stream()
        .map(d -> new NameUUIDPair(d.getId(), d.getValue()))
        .collect(Collectors.toSet());
    
    assertEquals(expected, actual);
  }

  @Test
  void savePerson() {
    datastore.init();
    
    Person person = createPerson("1", true);
    
    datastore.save(person);

    Optional<DropDownItem> maybeSavedItem = datastore.getEnabledPersonDropDowns().stream()
        .filter(d -> d.getId().equals(person.getUuid()) && d.getValue().equals(person.getName()))
        .findFirst();
    assertTrue(maybeSavedItem.isPresent());

    Optional<Person> maybeSavedPerson = readFile(PersonData.class).getPeople().getPersons().stream()
        .filter(p -> p.getUuid().equals(person.getUuid()) && p.getName().equals(person.getName()))
        .findFirst();
    assertTrue(maybeSavedPerson.isEmpty()); // should not be in default data file
    
    maybeSavedPerson = readLocalFile(PersonData.class).getPeople().getPersons().stream()
        .filter(p -> p.getUuid().equals(person.getUuid()) && p.getName().equals(person.getName()))
        .findFirst();
    assertTrue(maybeSavedPerson.isPresent()); // should be stored in overrides file
    
    Person savedPerson = maybeSavedPerson.get();
    assertPeopleEqual(person, savedPerson);
  }
  
  @Test
  void savePersonOverride() {
    datastore.init();
    
    Person person = datastore.findByUUID("8503be40-572f-11e1-b86c-0800200c9a66").orElseThrow(
        () -> new IllegalStateException("Person not found")
    );
    assertFalse(person.isUse());
    person.setUse(true);
    
    datastore.save(person);
    
    Optional<DropDownItem> maybeSavedItem = datastore.getEnabledPersonDropDowns().stream()
        .filter(d -> d.getId().equals(person.getUuid()) && d.getValue().equals(person.getName()))
        .findFirst();
    assertTrue(maybeSavedItem.isPresent());

    Optional<Person> maybeSavedPerson = readFile(PersonData.class).getPeople().getPersons().stream()
        .filter(p -> p.getUuid().equals(person.getUuid()) && p.getName().equals(person.getName()))
        .findFirst();
    assertTrue(maybeSavedPerson.isPresent()); // should be present in default file without edits
    assertFalse(maybeSavedPerson.get().isUse());

    maybeSavedPerson = readLocalFile(PersonData.class).getPeople().getPersons().stream()
        .filter(p -> p.getUuid().equals(person.getUuid()) && p.getName().equals(person.getName()))
        .findFirst();
    assertTrue(maybeSavedPerson.isPresent()); // should be stored in overrides file with edits
    assertTrue(maybeSavedPerson.get().isUse());

    Person savedPerson = maybeSavedPerson.get();
    assertPeopleEqual(person, savedPerson);
  }

  @Test
  void saveModel() {
    datastore.init();
    
    PersonModel person = createModel("1", true);
    
    datastore.save(person);

    Optional<DropDownItem> maybeSavedItem = datastore.getEnabledPersonDropDowns().stream()
        .filter(d -> d.getId().equals(person.getUuid()) && d.getValue().equals(person.getName()))
        .findFirst();
    assertTrue(maybeSavedItem.isPresent());

    Optional<Person> maybeSavedPerson = readFile(PersonData.class).getPeople().getPersons().stream()
        .filter(p -> p.getUuid().equals(person.getUuid()) && p.getName().equals(person.getName()))
        .findFirst();
    assertTrue(maybeSavedPerson.isEmpty()); // should not be in default data file

    maybeSavedPerson = readLocalFile(PersonData.class).getPeople().getPersons().stream()
        .filter(p -> p.getUuid().equals(person.getUuid()) && p.getName().equals(person.getName()))
        .findFirst();
    assertTrue(maybeSavedPerson.isPresent()); // should be stored in overrides file

    Person savedPerson = maybeSavedPerson.get();
    assertPersonEqualsPersonModelEqual(person, savedPerson);
  }

  @Test
  void getByUUID() {
    datastore.init();
    
    String uuid = "8503be40-572f-11e1-b86c-0800200c9a66";
    Optional<Person> maybePerson = datastore.findByUUID(uuid);
    assertTrue(maybePerson.isPresent());
    assertEquals(uuid, maybePerson.get().getUuid());
    
    assertTrue(datastore.findByUUID("TEST").isEmpty());
  }

  @Test
  void findByName() {
    datastore.init();
    
    String name = "Adam Skarke";
    Optional<Person> maybePerson = datastore.findByName(name);
    assertTrue(maybePerson.isPresent());
    assertEquals(name, maybePerson.get().getName());
    
    assertTrue(datastore.findByName("TEST").isEmpty());
  }

  @Override
  protected String getXMLFilename() {
    return "people.xml";
  }
  
  private static Person createPerson(String suffix, boolean use) {
    Person person = new Person();
    
    person.setUuid(String.format("uuid-%s", suffix));
    person.setOrcid(String.format("orcid-%s", suffix));
    person.setName(String.format("person-%s", suffix));
    person.setPosition(String.format("position-%s", suffix));
    person.setOrganization(String.format("organization-%s", suffix));
    person.setStreet(String.format("street-%s", suffix));
    person.setCity(String.format("city-%s", suffix));
    person.setState(String.format("state-%s", suffix));
    person.setZip(String.format("zip-%s", suffix));
    person.setCountry(String.format("country-%s", suffix));
    person.setPhone(String.format("phone-%s", suffix));
    person.setEmail(String.format("email-%s", suffix));
    person.setUse(use);
    
    return person;
  }
  
  private static PersonModel createModel(String suffix, boolean use) {
    PersonModel personModel = new PersonModel();
    
    personModel.setUuid(String.format("uuid-%s", suffix));
    personModel.setOrcidID(String.format("orcid-%s", suffix));
    personModel.setName(String.format("person-%s", suffix));
    personModel.setPosition(String.format("position-%s", suffix));
    personModel.setOrganization(String.format("organization-%s", suffix));
    personModel.setStreet(String.format("street-%s", suffix));
    personModel.setCity(String.format("city-%s", suffix));
    personModel.setState(String.format("state-%s", suffix));
    personModel.setZip(String.format("zip-%s", suffix));
    personModel.setCountry(String.format("country-%s", suffix));
    personModel.setPhone(String.format("phone-%s", suffix));
    personModel.setEmail(String.format("email-%s", suffix));
    personModel.setUse(use);
    
    return personModel;
  }
  
  private void assertPeopleEqual(Person expected, Person actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getOrcid(), actual.getOrcid());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getPosition(), actual.getPosition());
    assertEquals(expected.getOrganization(), actual.getOrganization());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getZip(), actual.getZip());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getPhone(), actual.getPhone());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.isUse(), actual.isUse());
  }

  private void assertPersonEqualsPersonModelEqual(PersonModel expected, Person actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getOrcidID(), actual.getOrcid());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getPosition(), actual.getPosition());
    assertEquals(expected.getOrganization(), actual.getOrganization());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getZip(), actual.getZip());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getPhone(), actual.getPhone());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.isUse(), actual.isUse());
  }

  @Override
  protected PersonData createDataObject() {
    PersonData personData = new PersonData();
    personData.setDataVersion("1.0");
    personData.setPeople(new PersonList());
    return personData;
  }
}