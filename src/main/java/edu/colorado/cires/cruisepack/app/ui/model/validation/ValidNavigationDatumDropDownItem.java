package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.privatejgoodies.common.base.Objects;

import edu.colorado.cires.cruisepack.app.datastore.NavigationDatumDatastore;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Target({ FIELD, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = ValidNavigationDatumDropDownItem.ValidNavigationDatumDropDownItemValidator.class)
@Documented
public @interface ValidNavigationDatumDropDownItem {

    String message() default "invalid value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ FIELD, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ValidNavigationDatumDropDownItem[] value();
    }

    class ValidNavigationDatumDropDownItemValidator implements ConstraintValidator<ValidNavigationDatumDropDownItem, DropDownItem> {

        @Override
        public boolean isValid(DropDownItem value, ConstraintValidatorContext context) {
            return !Objects.equals(value, NavigationDatumDatastore.UNSELECTED_NAVIGATION_DATUM);
        }}
    
}
