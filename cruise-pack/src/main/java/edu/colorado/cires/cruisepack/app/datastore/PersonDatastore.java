package edu.colorado.cires.cruisepack.app.datastore;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.PersonModel;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.person.Person;
import edu.colorado.cires.cruisepack.xml.person.PersonData;
import edu.colorado.cires.cruisepack.xml.person.PersonList;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

@Component
public class PersonDatastore extends PropertyChangeModel implements PropertyChangeListener {

    public static final DropDownItem UNSELECTED_PERSON = new DropDownItem("", "Select Person");

    private final ServiceProperties serviceProperties;
    private List<DropDownItem> personDropDowns;
    private final ReactiveViewRegistry reactiveViewRegistry;

    @Autowired
    public PersonDatastore(ServiceProperties serviceProperties, ReactiveViewRegistry reactiveViewRegistry) {
        this.serviceProperties = serviceProperties;
        this.reactiveViewRegistry = reactiveViewRegistry;
    }

    @PostConstruct
    public void init() {
        addChangeListener(this);
        load();
    }
    
    private void load() {
        List<DropDownItem> items = mergeDropDownItemLists(readPeople("data"), readPeople("local-data"));
        items.add(0, UNSELECTED_PERSON);

        setPersonDropDowns(items);
    }

    private void setPersonDropDowns(List<DropDownItem> items) {
        setIfChanged(Events.UPDATE_PERSON_DATA_STORE, items, () -> new ArrayList<DropDownItem>(0), (i) -> this.personDropDowns = i);
    }

    private List<DropDownItem> readPeople(String dir) {
        Path workDir = Paths.get(serviceProperties.getWorkDir());
        Path dataDir = workDir.resolve(dir);
        Path peopleFile = dataDir.resolve("people.xml");
        if (!Files.isRegularFile(peopleFile)) {
            throw new IllegalStateException("Unable to read " + peopleFile);
        }
        PersonData personData;
        try (Reader reader = Files.newBufferedReader(peopleFile, StandardCharsets.UTF_8)) {
            personData = (PersonData) JAXBContext.newInstance(PersonData.class)
                .createUnmarshaller().unmarshal(reader);
        } catch (IOException | JAXBException e) {
            throw new IllegalStateException("Unable to parse " + peopleFile, e);
        }
        List<DropDownItem> dropDowns = new ArrayList<>(personData.getPeople().getPersons().size() + 1);
        personData.getPeople().getPersons().stream()
            .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
            .map(person -> new DropDownItem(person.getUuid(), person.getName(), person))
            .forEach(dropDowns::add);
        return dropDowns;
    }

    public List<DropDownItem> getEnabledPersonDropDowns() {
        return personDropDowns.stream()
            .filter((i) -> {
                Person person = (Person) i.getRecord();
                if (person == null) {
                    return true;
                }
                return person.isUse();
            }).collect(Collectors.toList());
    }

    public List<DropDownItem> getAllPersonDropDowns() {
        return personDropDowns;
    }

    private List<DropDownItem> mergeDropDownItemLists(List<DropDownItem> defaults, List<DropDownItem> overrides) {
        Map<String, DropDownItem> merged = new HashMap<>(0);
        defaults.forEach(i -> merged.put(i.getId(), i));
        overrides.forEach(i -> merged.put(i.getId(), i));

        return merged.values().stream()
            .sorted((p1, p2) -> p1.getValue().compareToIgnoreCase(p2.getValue()))
            .collect(Collectors.toList());
    }

    public void save(PersonModel personModel) {
        Person person = personFromModel(personModel);
        DropDownItem dropDownItem = new DropDownItem(person.getUuid(), person.getName(), person);
        List<DropDownItem> dropDownItems = mergeDropDownItemLists(
            readPeople("local-data"),
             Collections.singletonList(dropDownItem)
        );

        PersonData personData = new PersonData();
        personData.setDataVersion("1.0");
        PersonList personList = new PersonList();
        List<Person> people = personList.getPersons();
        people.addAll(
            dropDownItems.stream()
            .map(i -> (Person) i.getRecord())
            .collect(Collectors.toList())
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (ReactiveView view : reactiveViewRegistry.getViews()) {
            view.onChange(evt);
        }
    }
    
}
