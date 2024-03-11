package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.privatejgoodies.common.base.Objects;

import edu.colorado.cires.cruisepack.app.datastore.WaterColumnCalibrationStateDatastore;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Target({ FIELD, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = ValidWaterColumnCalibrationStateDropDownItem.ValidWaterColumnCalibrationStateDropDownItemValidator.class)
@Documented
public @interface ValidWaterColumnCalibrationStateDropDownItem {

    String message() default "invalid value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ FIELD, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ValidWaterColumnCalibrationStateDropDownItem[] value();
    }

    class ValidWaterColumnCalibrationStateDropDownItemValidator implements ConstraintValidator<ValidWaterColumnCalibrationStateDropDownItem, DropDownItem> {

        @Override
        public boolean isValid(DropDownItem value, ConstraintValidatorContext context) {
            return !Objects.equals(value, WaterColumnCalibrationStateDatastore.UNSELECTED_CALIBRATION_STATE);
        }

    }
    
}
