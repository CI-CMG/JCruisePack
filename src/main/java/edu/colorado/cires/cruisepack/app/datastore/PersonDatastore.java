package edu.colorado.cires.cruisepack.app.datastore;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.model.PersonModel;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.person.Person;
import edu.colorado.cires.cruisepack.xml.person.PersonData;
import edu.colorado.cires.cruisepack.xml.person.PersonList;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonDatastore extends PropertyChangeModel {

    public static final DropDownItem UNSELECTED_PERSON = new DropDownItem("", "Select Person");

    private final ServiceProperties serviceProperties;
    private List<DropDownItem> personDropDowns;
  private List<Person> people;

    @Autowired
    public PersonDatastore(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    @PostConstruct
    public void init() {
        load();
    }
    
    private void load() {
        people = mergeDropDownItemLists(readPeople("data"), readPeople("local-data"));
        List<DropDownItem> items = people.stream()
                .map(p -> new DropDownItem(p.getUuid(), p.getName()))
                .collect(Collectors.toList());
        items.add(0, UNSELECTED_PERSON);

        setPersonDropDowns(items);
    }

    private void setPersonDropDowns(List<DropDownItem> items) {
        setIfChanged(Events.UPDATE_PERSON_DATA_STORE, items, () -> new ArrayList<DropDownItem>(0), (i) -> this.personDropDowns = i);
    }

    private Optional<PersonData> readPeople(String dir) {
        Path workDir = Paths.get(serviceProperties.getWorkDir());
        Path dataDir = workDir.resolve(dir);
        Path peopleFile = dataDir.resolve("people.xml");
        if (!Files.isRegularFile(peopleFile)) {
            return Optional.empty();
        }
        PersonData personData;
        try (Reader reader = Files.newBufferedReader(peopleFile, StandardCharsets.UTF_8)) {
            personData = (PersonData) JAXBContext.newInstance(PersonData.class)
                .createUnmarshaller().unmarshal(reader);
        } catch (IOException | JAXBException e) {
            throw new IllegalStateException("Unable to parse " + peopleFile, e);
        }
        
        return Optional.of(personData);
    }

    public List<DropDownItem> getEnabledPersonDropDowns() {
        return personDropDowns.stream()
            .filter((dd) -> 
                findByUUID(dd.getId())
                    .map(Person::isUse)
                    .orElse(dd.equals(UNSELECTED_PERSON))
            ).collect(Collectors.toList());
    }

    public List<DropDownItem> getAllPersonDropDowns() {
        return personDropDowns;
    }

    private List<Person> mergeDropDownItemLists(Optional<PersonData> defaults, Optional<PersonData> overrides) {
        Map<String, Person> merged = new HashMap<>(0);
        defaults.map(pd -> pd.getPeople().getPersons()).ifPresent(p -> p.forEach(i -> merged.put(i.getUuid(), i)));
        overrides.map(pd -> pd.getPeople().getPersons()).ifPresent(p -> p.forEach(i -> merged.put(i.getUuid(), i)));

        return merged.values().stream()
            .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
            .collect(Collectors.toList());
    }

    public void save(Person person) {
        PersonData newPersonData = new PersonData();
        PersonList newPersonList = new PersonList();
        List<Person> listWithNewPerson = newPersonList.getPersons();
        listWithNewPerson.add(person);
        newPersonData.setPeople(newPersonList);
        List<Person> mergedPeople = mergeDropDownItemLists(
            readPeople("local-data"),
            Optional.of(newPersonData)
        );

        PersonData personData = new PersonData();
        personData.setDataVersion("1.0");
        PersonList personList = new PersonList();
        List<Person> people = personList.getPersons();
        people.addAll(
            mergedPeople
        );
        personData.setPeople(personList);

        Path workDir = Paths.get(serviceProperties.getWorkDir());
        Path dataDir = workDir.resolve("local-data");
        Path peopleFile = dataDir.resolve("people.xml");

        try (OutputStream outputStream = new FileOutputStream(peopleFile.toFile())) {
            JAXB.marshal(personData, outputStream);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to save drop down items: ", e);
        }

        load();
    }

    public void save(PersonModel personModel) {
        save(personFromModel(personModel));
    }

    private Person personFromModel(PersonModel personModel) {
        Person person = new Person();
        person.setName(personModel.getName());
        person.setPosition(personModel.getPosition());
        person.setOrganization(personModel.getOrganization());
        person.setStreet(personModel.getStreet());
        person.setCity(personModel.getCity());
        person.setState(personModel.getState());
        person.setZip(personModel.getZip());
        person.setCountry(personModel.getCountry());
        person.setPhone(personModel.getPhone());
        person.setEmail(personModel.getEmail());
        person.setOrcid(personModel.getOrcidID());
        person.setUuid(personModel.getUuid());
        person.setUse(personModel.isUse());
        person.setUuid(personModel.getUuid());
        return person;
    }
    
    public Optional<Person> findByUUID(String uuid) {
        return people.stream()
            .filter(p -> p.getUuid().equals(uuid))
            .findFirst();
    }
    
    public Optional<Person> findByName(String name) {
        return people.stream()
            .filter(p -> p.getName().equals(name))
            .findFirst();
    }
    
}
