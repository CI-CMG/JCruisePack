package edu.colorado.cires.cruisepack.app.datastore;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.person.Person;
import edu.colorado.cires.cruisepack.xml.person.PersonData;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

@Component
public class PersonDatastore {

    public static final DropDownItem UNSELECTED_PERSON = new DropDownItem("", "Select Person");

    private final ServiceProperties serviceProperties;
    private List<DropDownItem> personDropDowns;

    @Autowired
    public PersonDatastore(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    @PostConstruct
    public void init() {
        Path workDir = Paths.get(serviceProperties.getWorkDir());
        Path dataDir = workDir.resolve("data");
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
        personDropDowns = new ArrayList<>(personData.getPeople().getPersons().size() + 1);
        personDropDowns.add(UNSELECTED_PERSON);
        personData.getPeople().getPersons().stream()
            .filter(Person::isUse)
            .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
            .map(person -> new DropDownItem(person.getUuid(), person.getName()))
            .forEach(personDropDowns::add);
    }

    public List<DropDownItem> getPersonDropDowns() {
        return personDropDowns;
    }
    
}
