package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Objects;

import edu.colorado.cires.cruisepack.app.datastore.ShipDatastore;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Target({ FIELD, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = ValidShipDropDownItem.ValidShipDropDownItemValidator.class)
@Documented
public @interface ValidShipDropDownItem {

    String message() default "invalid value";
    
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ FIELD, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ValidShipDropDownItem[] value();
    }

    class ValidShipDropDownItemValidator implements ConstraintValidator<ValidShipDropDownItem, DropDownItem> {
        @Override
        public boolean isValid(DropDownItem value, ConstraintValidatorContext constraintValidatorContext) {
            return !Objects.equals(value, ShipDatastore.UNSELECTED_SHIP);
        }
    }
}
