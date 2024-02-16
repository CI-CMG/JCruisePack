package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Objects;

import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatasore;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Target({ FIELD, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = ValidOrganizationDropDownItem.ValidOrganizationDropDownItemValidator.class)
@Documented
public @interface ValidOrganizationDropDownItem {

    String message() default "invalid value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ FIELD, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		ValidOrganizationDropDownItem[] value();
	}

    class ValidOrganizationDropDownItemValidator implements ConstraintValidator<ValidOrganizationDropDownItem, DropDownItem> {

        @Override
        public boolean isValid(DropDownItem value, ConstraintValidatorContext context) {
            return !Objects.equals(value, OrganizationDatasore.UNSELECTED_ORGANIZATION);
        }
    
    }
    
}
