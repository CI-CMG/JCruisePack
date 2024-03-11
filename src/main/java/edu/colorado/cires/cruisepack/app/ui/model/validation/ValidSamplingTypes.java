package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import edu.colorado.cires.cruisepack.app.ui.model.SamplingTypesModel;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidSamplingTypes.ValidSamplingTypesValidator.class)
@Documented
public @interface ValidSamplingTypes {

    String message() default "no value selected";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ValidSamplingTypesValidator implements ConstraintValidator<ValidSamplingTypes, SamplingTypesModel> {
        @Override
        public boolean isValid(SamplingTypesModel samplingTypesModel, ConstraintValidatorContext context) {
            return samplingTypesModel.isOrganicTissue() || samplingTypesModel.isSoilSediment() || samplingTypesModel.isWater();
        }
    }
    
}
