package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import org.junit.jupiter.api.Test;

public class OrganizationModelTest extends PropertyChangeModelTest<OrganizationModel> {

  @Override
  protected OrganizationModel createModel() {
    return new OrganizationModel();
  }

  @Test
  void testSetName() {
    assertPropertyChange(
        Events.UPDATE_ORG_NAME,
        model::getName,
        model::setName,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetNameError() {
    assertPropertyChange(
        Events.UPDATE_ORG_NAME_ERROR,
        model::getNameError,
        model::setNameError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetStreet() {
    assertPropertyChange(
        Events.UPDATE_ORG_STREET,
        model::getStreet,
        model::setStreet,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetStreetError() {
    assertPropertyChange(
        Events.UPDATE_ORG_STREET_ERROR,
        model::getStreetError,
        model::setStreetError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetCity() {
    assertPropertyChange(
        Events.UPDATE_ORG_CITY,
        model::getCity,
        model::setCity,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetCityError() {
    assertPropertyChange(
        Events.UPDATE_ORG_CITY_ERROR,
        model::getCityError,
        model::setCityError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetState() {
    assertPropertyChange(
        Events.UPDATE_ORG_STATE,
        model::getState,
        model::setState,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetStateError() {
    assertPropertyChange(
        Events.UPDATE_ORG_STATE_ERROR,
        model::getStateError,
        model::setStateError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetZip() {
    assertPropertyChange(
        Events.UPDATE_ORG_ZIP,
        model::getZip,
        model::setZip,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetZipError() {
    assertPropertyChange(
        Events.UPDATE_ORG_ZIP_ERROR,
        model::getZipError,
        model::setZipError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetCountry() {
    assertPropertyChange(
        Events.UPDATE_ORG_COUNTRY,
        model::getCountry,
        model::setCountry,
        "value1",
        "value2",
        "USA"
    );
  }

  @Test
  void testSetCountryError() {
    assertPropertyChange(
        Events.UPDATE_ORG_COUNTRY_ERROR,
        model::getCountryError,
        model::setCountryError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetPhone() {
    assertPropertyChange(
        Events.UPDATE_ORG_PHONE,
        model::getPhone,
        model::setPhone,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetPhoneError() {
    assertPropertyChange(
        Events.UPDATE_ORG_PHONE_ERROR,
        model::getPhoneError,
        model::setPhoneError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetEmail() {
    assertPropertyChange(
        Events.UPDATE_ORG_EMAIL,
        model::getEmail,
        model::setEmail,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetEmailError() {
    assertPropertyChange(
        Events.UPDATE_ORG_EMAIL_ERROR,
        model::getEmailError,
        model::setEmailError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetUUID() {
    assertPropertyChange(
        Events.UPDATE_ORG_UUID,
        model::getUuid,
        model::setUuid,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetUUIDError() {
    assertPropertyChange(
        Events.UPDATE_ORG_UUID_ERROR,
        model::getUuidError,
        model::setUuidError,
        "value1",
        "value2",
        null
    );
  }

  @Test
  void testSetUse() {
    assertPropertyChange(
        Events.UPDATE_ORG_USE,
        model::isUse,
        model::setUse,
        true,
        false,
        false
    );
  }

  @Test
  void testEmitOrgName() {
    String name = "test-name";
    model.setName(name);

    model.emitOrgName();
    assertChangeEvent(Events.EMIT_ORG_NAME, null, name);

    clearEvents();

    model.emitOrgName();
    assertChangeEvent(Events.EMIT_ORG_NAME, null, name);
  }

  @Test
  void testRestoreDefaults() {
    String name = "test-name";
    String nameError = "test-name-error";
    String street = "test-street";
    String streetError = "test-street-error";
    String city = "test-city";
    String cityError = "test-city-error";
    String state = "test-state";
    String stateError = "test-state-error";
    String zip = "test-zip";
    String zipError = "test-zip-error";
    String country = "test-country";
    String countryError = "test-country-error";
    String phone = "test-phone";
    String phoneError = "test-phone-error";
    String email = "test-email";
    String emailError = "test-email-error";
    String uuid = "test-uuid";
    String uuidError = "test-uuid-error";
    boolean use = true;

    model.setName(name);
    model.setNameError(nameError);
    model.setStreet(street);
    model.setStreetError(streetError);
    model.setCity(city);
    model.setCityError(cityError);
    model.setState(state);
    model.setStateError(stateError);
    model.setZip(zip);
    model.setZipError(zipError);
    model.setCountry(country);
    model.setCountryError(countryError);
    model.setPhone(phone);
    model.setPhoneError(phoneError);
    model.setEmail(email);
    model.setEmailError(emailError);
    model.setUuid(uuid);
    model.setUuidError(uuidError);
    model.setUse(use);
    
    clearEvents();

    model.restoreDefaults();

    assertChangeEvent(Events.UPDATE_ORG_NAME, name, null);
    assertChangeEvent(Events.UPDATE_ORG_NAME_ERROR, nameError, null);
    assertChangeEvent(Events.UPDATE_ORG_STREET, street, null);
    assertChangeEvent(Events.UPDATE_ORG_STREET_ERROR, streetError, null);
    assertChangeEvent(Events.UPDATE_ORG_CITY, city, null);
    assertChangeEvent(Events.UPDATE_ORG_CITY_ERROR, cityError, null);
    assertChangeEvent(Events.UPDATE_ORG_STATE, state, null);
    assertChangeEvent(Events.UPDATE_ORG_STATE_ERROR, stateError, null);
    assertChangeEvent(Events.UPDATE_ORG_ZIP, zip, null);
    assertChangeEvent(Events.UPDATE_ORG_ZIP_ERROR, zipError, null);
    assertChangeEvent(Events.UPDATE_ORG_COUNTRY, country, "USA");
    assertChangeEvent(Events.UPDATE_ORG_COUNTRY_ERROR, countryError, null);
    assertChangeEvent(Events.UPDATE_ORG_PHONE, phone, null);
    assertChangeEvent(Events.UPDATE_ORG_PHONE_ERROR, phoneError, null);
    assertChangeEvent(Events.UPDATE_ORG_EMAIL, email, null);
    assertChangeEvent(Events.UPDATE_ORG_EMAIL_ERROR, emailError, null);
    assertChangeEvent(Events.UPDATE_ORG_UUID, uuid, null);
    assertChangeEvent(Events.UPDATE_ORG_UUID_ERROR, uuidError, null);
    assertChangeEvent(Events.UPDATE_ORG_USE, true, false);
  }
}
