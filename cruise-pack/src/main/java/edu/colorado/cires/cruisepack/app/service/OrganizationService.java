package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.xml.organization.Organization;
import java.util.Optional;
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

    public ResponseStatus save(OrganizationModel organizationModel) {
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
            boolean organizationWithNameExists = organizationDatastore.getAllOrganizationDropDowns().stream().anyMatch(p -> p.getValue().equals(organizationModel.getName()));
            
            if (organizationModel.getUuid() == null || organizationModel.getUuid().isBlank()) {
                if (organizationWithNameExists) {
                    organizationModel.emitOrgName();
                    return ResponseStatus.CONFLICT;
                }
                
                organizationModel.setUuid(UUID.randomUUID().toString());
            }

            Optional<Organization> maybeOrganization = organizationDatastore.getByUUID(organizationModel.getUuid());
            if (maybeOrganization.isPresent()) {
                Organization existingOrganization = maybeOrganization.get();
                if (!existingOrganization.getName().equals(organizationModel.getName()) && organizationWithNameExists) {
                    organizationModel.emitOrgName();
                    return ResponseStatus.CONFLICT;
                }
            }

            organizationDatastore.save(organizationModel);
            organizationModel.emitOrgName();
            return ResponseStatus.SUCCESS;
        }

        return ResponseStatus.ERROR;
    }
    
}
