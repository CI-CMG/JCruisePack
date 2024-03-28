package edu.colorado.cires.cruisepack.app.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.model.OrganizationModel;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModelTest;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.organization.Organization;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class OrganizationServiceTest extends PropertyChangeModelTest<OrganizationModel> {
  
  private static final Validator VALIDATOR = mock(Validator.class);
  private static final OrganizationDatastore DATASTORE = mock(OrganizationDatastore.class);
  
  private final OrganizationService service = new OrganizationService(VALIDATOR, DATASTORE);
  
  @BeforeEach
  public void beforeEach() {
    super.beforeEach();
    reset(VALIDATOR);
    reset(DATASTORE);
  }

  @Test
  void saveNewOrganization() {
    when(VALIDATOR.validate(model)).thenReturn(
        Collections.emptySet()
    );

    when(DATASTORE.getAllOrganizationDropDowns()).thenReturn(
        Collections.emptyList()
    );

    when(DATASTORE.findByUUID(any())).thenReturn(
        Optional.empty()
    );

    populateModel(true);

    ResponseStatus status = service.save(model);
    assertEquals(ResponseStatus.SUCCESS, status);

    ArgumentCaptor<OrganizationModel> organizationModelArgumentCaptor = ArgumentCaptor.forClass(OrganizationModel.class);
    verify(DATASTORE, times(1)).save(organizationModelArgumentCaptor.capture());

    List<OrganizationModel> modelArguments = organizationModelArgumentCaptor.getAllValues();
    assertEquals(1, modelArguments.size());

    assertEquals("name", modelArguments.get(0).getName());
    assertNull(modelArguments.get(0).getNameError());
    assertEquals("street", modelArguments.get(0).getStreet());
    assertNull(modelArguments.get(0).getStreetError());
    assertEquals("city", modelArguments.get(0).getCity());
    assertNull(modelArguments.get(0).getCityError());
    assertEquals("state", modelArguments.get(0).getState());
    assertNull(modelArguments.get(0).getStateError());
    assertEquals("zip", modelArguments.get(0).getZip());
    assertNull(modelArguments.get(0).getZipError());
    assertEquals("UK", modelArguments.get(0).getCountry());
    assertNull(modelArguments.get(0).getCountryError());
    assertEquals("phone", modelArguments.get(0).getPhone());
    assertNull(modelArguments.get(0).getPhoneError());
    assertEquals("email", modelArguments.get(0).getEmail());
    assertNull(modelArguments.get(0).getEmailError());
    assertNotNull(modelArguments.get(0).getUuid());
    assertNull(modelArguments.get(0).getUuidError());
    assertDoesNotThrow(() -> UUID.fromString(modelArguments.get(0).getUuid())); // valid uuid generated by service
    assertTrue(modelArguments.get(0).isUse());

    assertChangeEvent(Events.EMIT_ORG_NAME, null, model.getName());
  }
  
  @Test
  void saveWithErrors() {
    populateModel(false);

    Set<ConstraintViolation<OrganizationModel>> constraintViolations = Set.of(
        new CustomConstraintViolation<>("name-error", "name"),
        new CustomConstraintViolation<>("street-error", "street"),
        new CustomConstraintViolation<>("city-error", "city"),
        new CustomConstraintViolation<>("state-error", "state"),
        new CustomConstraintViolation<>("zip-error", "zip"),
        new CustomConstraintViolation<>("UK-error", "country"),
        new CustomConstraintViolation<>("phone-error", "phone"),
        new CustomConstraintViolation<>("email-error", "email"),
        new CustomConstraintViolation<>("uuid-error", "uuid")
    );

    when(VALIDATOR.validate(model)).thenReturn(constraintViolations);

    ResponseStatus status = service.save(model);
    assertEquals(ResponseStatus.ERROR, status);

    verify(DATASTORE, times(0)).getAllOrganizationDropDowns();
    verify(DATASTORE, times(0)).findByUUID(any());
    verify(DATASTORE, times(0)).save(any(OrganizationModel.class));
  }

  @Test
  void saveNewOrganizationConflict() {
    when(VALIDATOR.validate(model)).thenReturn(
        Collections.emptySet()
    );

    populateModel(false);

    when(DATASTORE.getAllOrganizationDropDowns()).thenReturn(Collections.singletonList(
        new DropDownItem("uuid", model.getName())
    ));

    ResponseStatus status = service.save(model);
    assertEquals(ResponseStatus.CONFLICT, status);

    verify(DATASTORE, times(0)).findByUUID(any());
    verify(DATASTORE, times(0)).save(any(OrganizationModel.class));

    assertChangeEvent(Events.EMIT_ORG_NAME, null, model.getName());
  }
  
  @Test
  void updateOrganizationConflict() {
    when(VALIDATOR.validate(model)).thenReturn(
        Collections.emptySet()
    );

    String uuid = UUID.randomUUID().toString();
    populateModel(false);
    model.setUuid(uuid);


    DropDownItem existingItem1 = new DropDownItem(uuid, "TEST");
    DropDownItem existingItem2 = new DropDownItem(UUID.randomUUID().toString(), model.getName());
    when(DATASTORE.getAllOrganizationDropDowns()).thenReturn(List.of(
        existingItem1, existingItem2
    ));

    Organization existingOrganization = new Organization();
    existingOrganization.setName(existingItem1.getValue());
    existingOrganization.setUuid(existingItem1.getId());
    when(DATASTORE.findByUUID(uuid)).thenReturn(Optional.of(
        existingOrganization
    ));

    ResponseStatus status = service.save(model);
    assertEquals(ResponseStatus.CONFLICT, status);

    verify(DATASTORE, times(0)).save(any(OrganizationModel.class));

    assertChangeEvent(Events.EMIT_ORG_NAME, null, model.getName());
  }

  @Override
  protected OrganizationModel createModel() {
    return new OrganizationModel();
  }

  private void populateModel(boolean withErrors) {
    model.setName("name");
    model.setStreet("street");
    model.setCity("city");
    model.setState("state");
    model.setZip("zip");
    model.setCountry("UK");
    model.setPhone("phone");
    model.setEmail("email");
    model.setUse(true);

    if (withErrors) {
      model.setNameError("name-error");
      model.setStreetError("street-error");
      model.setCityError("city-error");
      model.setStateError("state-error");
      model.setZipError("zip-error");
      model.setCountryError("UK-error");
      model.setPhoneError("phone-error");
      model.setEmailError("email-error");
    }
  }
}