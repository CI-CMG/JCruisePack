package edu.colorado.cires.cruisepack.app.ui.model;

import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Component
public class OrganizationModel extends PropertyChangeModel {

    @NotBlank
    private String name;
    private String nameError = null;

    @NotBlank
    private String street;
    private String streetError = null;

    @NotBlank
    private String city;
    private String cityError = null;

    @NotBlank
    private String state;
    private String stateError = null;

    @NotBlank
    private String zip;
    private String zipError = null;

    @NotBlank
    private String country = "USA";
    private String countryError = null;

    @NotBlank
    private String phone;
    private String phoneError = null;

    @NotBlank
    private String email;
    private String emailError = null;

    @NotBlank
    private String uuid;
    private String uuidError = null;

    @NotNull
    private boolean use = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        setIfChanged(Events.UPDATE_ORG_NAME, name, () -> this.name, (n) -> this.name = n);
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        setIfChanged(Events.UPDATE_ORG_STREET, street, () -> this.street, (s) -> this.street = s);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        setIfChanged(Events.UPDATE_ORG_CITY, city, () -> this.city, (c) -> this.city = c);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        setIfChanged(Events.UPDATE_ORG_STATE, state, () -> this.state, (s) -> this.state = s);
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        setIfChanged(Events.UPDATE_ORG_ZIP, zip, () -> this.zip, (z) -> this.zip = z);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        setIfChanged(Events.UPDATE_ORG_COUNTRY, country, () -> this.country, (c) -> this.country = c);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        setIfChanged(Events.UPDATE_ORG_PHONE, phone, () -> this.phone, (p) -> this.phone = p);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        setIfChanged(Events.UPDATE_ORG_EMAIL, email, () -> this.email, (e) -> this.email = e);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        setIfChanged(Events.UPDATE_ORG_UUID, uuid, () -> this.uuid, (u) -> this.uuid = u);
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        setIfChanged(Events.UPDATE_ORG_USE, use, () -> this.use, (u) -> this.use = u);
    }

    public String getNameError() {
        return nameError;
    }

    public void setNameError(String nameError) {
        setIfChanged(Events.UPDATE_ORG_NAME_ERROR, nameError, () -> this.nameError, (e) -> this.nameError = e);
    }

    public String getStreetError() {
        return streetError;
    }

    public void setStreetError(String streetError) {
        setIfChanged(Events.UPDATE_ORG_STREET_ERROR, streetError, () -> this.streetError, (e) -> this.streetError = e);
    }

    public String getCityError() {
        return cityError;
    }

    public void setCityError(String cityError) {
        setIfChanged(Events.UPDATE_ORG_CITY_ERROR, cityError, () -> this.cityError, (e) -> this.cityError = e);
    }

    public String getStateError() {
        return stateError;
    }

    public void setStateError(String stateError) {
        setIfChanged(Events.UPDATE_ORG_STATE_ERROR, stateError, () -> this.stateError, (e) -> this.stateError = e);
    }

    public String getZipError() {
        return zipError;
    }

    public void setZipError(String zipError) {
        setIfChanged(Events.UPDATE_ORG_ZIP_ERROR, zipError, () -> this.zipError, (e) -> this.zipError = e);
    }

    public String getCountryError() {
        return countryError;
    }

    public void setCountryError(String countryError) {
        setIfChanged(Events.UPDATE_ORG_COUNTRY_ERROR, countryError, () -> this.countryError, (e) -> this.countryError = e);
    }

    public String getPhoneError() {
        return phoneError;
    }

    public void setPhoneError(String phoneError) {
        setIfChanged(Events.UPDATE_ORG_PHONE_ERROR, phoneError, () -> this.phoneError, (e) -> this.phoneError = e);
    }

    public String getEmailError() {
        return emailError;
    }

    public void setEmailError(String emailError) {
        setIfChanged(Events.UPDATE_ORG_EMAIL_ERROR, emailError, () -> this.emailError, (e) -> this.emailError = e);
    }

    public String getUuidError() {
        return uuidError;
    }

    public void setUuidError(String uuidError) {
        setIfChanged(Events.UPDATE_ORG_UUID_ERROR, uuidError, () -> this.uuidError, (e) -> this.uuidError = e);
    }

    public void restoreDefaults() {
        setName(null);
        setStreet(null);
        setCity(null);
        setState(null);
        setZip(null);
        setCountry("USA");
        setPhone(null);
        setEmail(null);
        setUuid(null);
        setUse(false);
    }
    
}
