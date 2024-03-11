package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.xml.person.Person;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.PersonModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Component
public class PersonService {

    private final Validator validator;
    private final PersonDatastore personDatastore;

    public PersonService(PersonDatastore personDatastore, Validator validator) {
        this.validator = validator;
        this.personDatastore = personDatastore;
    }

    public ResponseStatus save(PersonModel personModel) {
        Set<ConstraintViolation<PersonModel>> violations = validator.validate(personModel);
        String nameError = null;
        String positionError = null;
        String organizationError = null;
        String streetError = null;
        String cityError = null;
        String stateError = null;
        String zipError = null;
        String countryError = null;
        String phoneError = null;
        String emailError = null;
        String orcidIDError = null;
        String uuidError = null;
        for (ConstraintViolation<PersonModel> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            if (propertyPath.startsWith("name")) {
                nameError = message;
            } else if (propertyPath.startsWith("position")) {
                positionError = message;
            } else if (propertyPath.startsWith("organization")) {
                organizationError = message;
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
            } else if (propertyPath.startsWith("orcidID")) {
                orcidIDError = message;
            } else if (propertyPath.startsWith("uuid")) {
                uuidError = message;
            }
        }

        personModel.setNameError(nameError);
        personModel.setPositionError(positionError);
        personModel.setOrganizationError(organizationError);
        personModel.setStreetError(streetError);
        personModel.setCityError(cityError);
        personModel.setStateError(stateError);
        personModel.setZipError(zipError);
        personModel.setCountryError(countryError);
        personModel.setPhoneError(phoneError);
        personModel.setEmailError(emailError);
        personModel.setOrcidIDError(orcidIDError);
        personModel.setUuidError(uuidError);
        
        if (violations.isEmpty()) {
            boolean personWithNameExists = personDatastore.getAllPersonDropDowns().stream().anyMatch(p -> p.getValue().equals(personModel.getName()));
            
            if (personModel.getUuid() == null || personModel.getUuid().isBlank()) {
                if (personWithNameExists) {
                    personModel.emitPersonName();
                    return ResponseStatus.CONFLICT;
                }
                personModel.setUuid(UUID.randomUUID().toString());
                
            }
            Optional<Person> maybePerson = personDatastore.getByUUID(personModel.getUuid());
            if (maybePerson.isPresent()) {
                Person existingPerson = maybePerson.get();
                if (!existingPerson.getName().equals(personModel.getName()) && personWithNameExists) {
                    personModel.emitPersonName();
                    return ResponseStatus.CONFLICT;
                }
            }
            personDatastore.save(personModel);
            personModel.emitPersonName();
            return ResponseStatus.SUCCESS;
        }

        return ResponseStatus.ERROR;
    }
    
}
