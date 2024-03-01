package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.service.ResponseStatus;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.service.PersonService;
import edu.colorado.cires.cruisepack.app.ui.model.PersonModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import jakarta.annotation.PostConstruct;

@Component
public class PersonController implements PropertyChangeListener {

    private final ReactiveViewRegistry reactiveViewRegistry;
    private final PersonModel personModel;
    private final PersonService personService;

    public PersonController(ReactiveViewRegistry reactiveViewRegistry, PersonModel personModel, PersonService personService) {
        this.reactiveViewRegistry = reactiveViewRegistry;
        this.personModel = personModel;
        this.personService = personService;
    }

    @PostConstruct
    public void init() {
        personModel.addChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (ReactiveView view : reactiveViewRegistry.getViews()) {
            view.onChange(evt);
        }
    }

    public void setName(String name) {
        personModel.setName(name);
    }

    public void setPosition(String position) {
        personModel.setPosition(position);
    }

    public void setOrganization(String organization) {
        personModel.setOrganization(organization);
    }

    public void setStreet(String street) {
        personModel.setStreet(street);
    }

    public void setCity(String city) {
        personModel.setCity(city);
    }

    public void setState(String state) {
        personModel.setState(state);
    }

    public void setZip(String zip) {
        personModel.setZip(zip);
    }

    public void setCountry(String country) {
        personModel.setCountry(country);
    }

    public void setPhone(String phone) {
        personModel.setPhone(phone);
    }

    public void setEmail(String email) {
        personModel.setEmail(email);
    }

    public void setOrcidID(String orcidID) {
        personModel.setOrcidID(orcidID);
    }

    public void setUUID(String uuid) {
        personModel.setUuid(uuid);
    }

    public void setUse(boolean use) {
        personModel.setUse(use);
    }

    public void restoreDefaults() {
        personModel.restoreDefaults();
    }

    public ResponseStatus submit() {
        return personService.save(personModel);
    }
    
}
