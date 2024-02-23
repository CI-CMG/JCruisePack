package edu.colorado.cires.cruisepack.app.service;

import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.OrganizationModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Component
public class OrganizationService {

    private final Validator validator;
    private final OrganizationDatastore organizationDatastore;

    public OrganizationService(Validator validator, OrganizationDatastore organizationDatastore) {
        this.validator = validator;
        this.organizationDatastore = organizationDatastore;
    }

    public boolean save(OrganizationModel organizationModel) {
        Set<ConstraintViolation<OrganizationModel>> violations = validator.validate(organizationModel);
        String nameError = null;
        String streetError = null;
        String cityError = null;
        String stateError = null;
        String zipError = null;
        String countryError = null;
        String phoneError = null;
        String emailError = null;
        String uuidError = null;
        for (ConstraintViolation<OrganizationModel> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            if (propertyPath.startsWith("name")) {
                nameError = message;
            } else if (propertyPath.startsWith("street")) {
                streetError = message;
            } else if (propertyPath.startsWith("city")) {
                cityError = message;
            } else if (propertyPath.startsWith("state")) {
                stateError = message;
            } else if (propertyPath.startsWith("zip")) {
                zipError = message;
            } else if (propertyPath.startsWith("country")) {
                countryError = message;
            } else if (propertyPath.startsWith("phone")) {
                phoneError = message;
            } else if (propertyPath.startsWith("email")) {
                emailError = message;
            } else if (propertyPath.startsWith("uuid")) {
                uuidError = message;
            }
        }

        organizationModel.setNameError(nameError);
        organizationModel.setStreetError(streetError);
        organizationModel.setCityError(cityError);
        organizationModel.setStateError(stateError);
        organizationModel.setZipError(zipError);
        organizationModel.setCountryError(countryError);
        organizationModel.setPhoneError(phoneError);
        organizationModel.setEmailError(emailError);
        organizationModel.setUuidError(uuidError);

        if (violations.isEmpty()) {
            if (organizationModel.getUuid() == null) {
                organizationModel.setUuid(UUID.randomUUID().toString());
            }

            organizationDatastore.save(organizationModel);

            return true;
        }

        return false;
    }
    
}
