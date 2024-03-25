package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import org.junit.jupiter.api.Test;

public class PersonModelTest extends PropertyChangeModelTest<PersonModel> {

  @Override
  protected PersonModel createModel() {
    return new PersonModel();
  }
  
  @Test
  void testSetName() {
    assertPropertyChange(
        Events.UPDATE_PERSON_NAME,
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
        Events.UPDATE_PERSON_NAME_ERROR,
        model::getNameError,
        model::setNameError,
        "value1",
        "value2",
        null
    );
  }
  
  @Test
  void testSetPosition() {
    assertPropertyChange(
        Events.UPDATE_PERSON_POSITION,
        model::getPosition,
        model::setPosition,
        "value1",
        "value2",
        null
    );
  }
  
  @Test
  void testSetPositionError() {
    assertPropertyChange(
        Events.UPDATE_PERSON_POSITION_ERROR,
        model::getPositionError,
        model::setPositionError,
        "value1",
        "value2",
        null
    );
  }
  
  @Test
  void testSetOrganization() {
    assertPropertyChange(
        Events.UPDATE_PERSON_ORGANIZATION,
        model::getOrganization,
        model::setOrganization,
        "value1",
        "value2",
        null
    );
  }
  
  @Test
  void testSetOrganizationError() {
    assertPropertyChange(
        Events.UPDATE_PERSON_ORGANIZATION_ERROR,
        model::getOrganizationError,
        model::setOrganizationError,
        "value1",
        "value2",
        null
    );
  }
  
  @Test
  void testSetStreet() {
    assertPropertyChange(
        Events.UPDATE_PERSON_STREET,
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
        Events.UPDATE_PERSON_STREET_ERROR,
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
        Events.UPDATE_PERSON_CITY,
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
        Events.UPDATE_PERSON_CITY_ERROR,
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
        Events.UPDATE_PERSON_STATE,
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
        Events.UPDATE_PERSON_STATE_ERROR,
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
        Events.UPDATE_PERSON_ZIP,
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
        Events.UPDATE_PERSON_ZIP_ERROR,
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
        Events.UPDATE_PERSON_COUNTRY,
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
        Events.UPDATE_PERSON_COUNTRY_ERROR,
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
        Events.UPDATE_PERSON_PHONE,
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
        Events.UPDATE_PERSON_PHONE_ERROR,
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
        Events.UPDATE_PERSON_EMAIL,
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
        Events.UPDATE_PERSON_EMAIL_ERROR,
        model::getEmailError,
        model::setEmailError,
        "value1",
        "value2",
        null
    );
  }
  
  @Test
  void testSetOrcidID() {
    assertPropertyChange(
        Events.UPDATE_PERSON_ORCIDID,
        model::getOrcidID,
        model::setOrcidID,
        "value1",
        "value2",
        null
    );
  }
  
  @Test
  void testSetOrcidIDError() {
    assertPropertyChange(
        Events.UPDATE_PERSON_ORCID_ID_ERROR,
        model::getOrcidIDError,
        model::setOrcidIDError,
        "value1",
        "value2",
        null
    );
  }
  
  @Test
  void testSetUUID() {
    assertPropertyChange(
        Events.UPDATE_PERSON_UUID,
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
        Events.UPDATE_PERSON_UUID_ERROR,
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
        Events.UPDATE_PERSON_USE,
        model::isUse,
        model::setUse,
        true,
        false,
        false
    );
  }
  
  @Test
  void testEmitPersonName() {
    String name = "test-name";
    model.setName(name);
    
    model.emitPersonName();
    assertChangeEvent(Events.EMIT_PERSON_NAME, null, name);
    
    clearEvents();
    
    model.emitPersonName();
    assertChangeEvent(Events.EMIT_PERSON_NAME, null, name);
  }
  
  @Test
  void testRestoreDefaults() {
    String name = "test-name";
    String nameError = "test-name-error";
    String position = "test-position";
    String positionError = "test-position-error";
    String organization = "test-organization";
    String organizationError = "test-organization-error";
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
    String orcidID = "test-orcidID";
    String orcidIDError = "test-orcidID-error";
    String uuid = "test-uuid";
    String uuidError = "test-uuid-error";
    boolean use = true;
    
    model.setName(name);
    model.setNameError(nameError);
    model.setPosition(position);
    model.setPositionError(positionError);
    model.setOrganization(organization);
    model.setOrganizationError(organizationError);
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
    model.setOrcidID(orcidID);
    model.setOrcidIDError(orcidIDError);
    model.setUuid(uuid);
    model.setUuidError(uuidError);
    model.setUse(use);
    
    clearEvents();
    
    model.restoreDefaults();
    
    assertChangeEvent(Events.UPDATE_PERSON_NAME, name, null);
    assertChangeEvent(Events.UPDATE_PERSON_NAME_ERROR, nameError, null);
    assertChangeEvent(Events.UPDATE_PERSON_POSITION, position, null);
    assertChangeEvent(Events.UPDATE_PERSON_POSITION_ERROR, positionError, null);
    assertChangeEvent(Events.UPDATE_PERSON_ORGANIZATION, organization, null);
    assertChangeEvent(Events.UPDATE_PERSON_ORGANIZATION_ERROR, organizationError, null);
    assertChangeEvent(Events.UPDATE_PERSON_STREET, street, null);
    assertChangeEvent(Events.UPDATE_PERSON_STREET_ERROR, streetError, null);
    assertChangeEvent(Events.UPDATE_PERSON_CITY, city, null);
    assertChangeEvent(Events.UPDATE_PERSON_CITY_ERROR, cityError, null);
    assertChangeEvent(Events.UPDATE_PERSON_STATE, state, null);
    assertChangeEvent(Events.UPDATE_PERSON_STATE_ERROR, stateError, null);
    assertChangeEvent(Events.UPDATE_PERSON_ZIP, zip, null);
    assertChangeEvent(Events.UPDATE_PERSON_ZIP_ERROR, zipError, null);
    assertChangeEvent(Events.UPDATE_PERSON_COUNTRY, country, "USA");
    assertChangeEvent(Events.UPDATE_PERSON_COUNTRY_ERROR, countryError, null);
    assertChangeEvent(Events.UPDATE_PERSON_PHONE, phone, null);
    assertChangeEvent(Events.UPDATE_PERSON_PHONE_ERROR, phoneError, null);
    assertChangeEvent(Events.UPDATE_PERSON_EMAIL, email, null);
    assertChangeEvent(Events.UPDATE_PERSON_EMAIL_ERROR, emailError, null);
    assertChangeEvent(Events.UPDATE_PERSON_ORCIDID, orcidID, null);
    assertChangeEvent(Events.UPDATE_PERSON_ORCID_ID_ERROR, orcidIDError, null);
    assertChangeEvent(Events.UPDATE_PERSON_UUID, uuid, null);
    assertChangeEvent(Events.UPDATE_PERSON_UUID_ERROR, uuidError, null);
    assertChangeEvent(Events.UPDATE_PERSON_USE, true, false);
  }
}
