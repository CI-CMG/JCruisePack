package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import edu.colorado.cires.cruisepack.app.ui.model.ExpectedAnalysesModel;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidExpectedAnalyses.ValidExpectedAnalysesValidator.class)
@Documented
public @interface ValidExpectedAnalyses {

    String message() default "no value selected";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ValidExpectedAnalysesValidator implements ConstraintValidator<ValidExpectedAnalyses, ExpectedAnalysesModel> {
        @Override
        public boolean isValid(ExpectedAnalysesModel expectedAnalysesModel, ConstraintValidatorContext constraintValidatorContext) {
            return expectedAnalysesModel.isBarcoding() ||
                expectedAnalysesModel.isGenomics() ||
                expectedAnalysesModel.isTranscriptomics() ||
                expectedAnalysesModel.isProteomics() ||
                expectedAnalysesModel.isMetabolomics() ||
                expectedAnalysesModel.isEpigenetics() ||
                expectedAnalysesModel.isOther() ||
                expectedAnalysesModel.isMetabarcoding() ||
                expectedAnalysesModel.isMetagenomics() ||
                expectedAnalysesModel.isMetatranscriptomics() ||
                expectedAnalysesModel.isMetaproteomics() ||
                expectedAnalysesModel.isMetametabolomics() ||
                expectedAnalysesModel.isMicrobiome();
        }
    }
    
}
