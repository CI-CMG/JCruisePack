package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Target({ FIELD, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(ValidPersonDropDownItem.List.class)
@Constraint(validatedBy = ValidPersonDropDownItem.ValidPersonDropDownItemValidator.class)
@Documented
public @interface ValidPersonDropDownItem {
    
    String message() default "invalid value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ FIELD, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		ValidPersonDropDownItem[] value();
	}

    @Component
    class ValidPersonDropDownItemValidator implements ConstraintValidator<ValidPersonDropDownItem, DropDownItem> {

        @Override
        public boolean isValid(DropDownItem value, ConstraintValidatorContext context) {
            return !Objects.equals(value, PersonDatastore.UNSELECTED_PERSON);
        }
    
    
    }

}
