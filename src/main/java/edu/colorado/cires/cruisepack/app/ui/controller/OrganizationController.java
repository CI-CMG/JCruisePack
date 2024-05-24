package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.service.ResponseStatus;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.service.OrganizationService;
import edu.colorado.cires.cruisepack.app.ui.model.OrganizationModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import jakarta.annotation.PostConstruct;

@Component
public class OrganizationController implements PropertyChangeListener {

    private final ReactiveViewRegistry reactiveViewRegistry;
    private final OrganizationModel organizationModel;
    private final OrganizationService organizationService;

    public OrganizationController(ReactiveViewRegistry reactiveViewRegistry, OrganizationModel organizationModel, OrganizationService organizationService) {
        this.reactiveViewRegistry = reactiveViewRegistry;
        this.organizationModel = organizationModel;
        this.organizationService = organizationService;
    }

    public void setName(String name) {
        organizationModel.setName(name);
    }

    public void setStreet(String street) {
        organizationModel.setStreet(street);
    }

    public void setCity(String city) {
        organizationModel.setCity(city);
    }

    public void setState(String state) {
        organizationModel.setState(state);
    }

    public void setZip(String zip) {
        organizationModel.setZip(zip);
    }

    public void setCountry(String country) {
        organizationModel.setCountry(country);
    }

    public void setPhone(String phone) {
        organizationModel.setPhone(phone);
    }

    public void setEmail(String email) {
        organizationModel.setEmail(email);
    }

    public void setUuid(String uuid) {
        organizationModel.setUuid(uuid);
    }

    public void setUse(boolean use) {
        organizationModel.setUse(use);
    }

    public void restoreDefaults() {
        organizationModel.restoreDefaults();
    }
    
    public void setDialogVisible(boolean dialogVisible) {
        organizationModel.setDialogVisible(dialogVisible);
    }

    public ResponseStatus submit() {
        return organizationService.save(organizationModel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (ReactiveView view : reactiveViewRegistry.getViews()) {
            view.onChange(evt);
        }
    }

    @PostConstruct
    public void init() {
        organizationModel.addChangeListener(this);
    }
    
}
