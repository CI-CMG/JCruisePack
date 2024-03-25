package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathExists.PathExistsValidator;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsFile.PathIsFileValidator;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidExpectedAnalyses.ValidExpectedAnalysesValidator;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidPersonDropDownItem.ValidPersonDropDownItemValidator;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidSamplingTypes.ValidSamplingTypesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.NotNullValidator;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidOmicsModel.ValidOmicsModelValidator.class)
@Documented
public @interface ValidOmicsModel {
  
  String message() default "Invalid fields";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  
  class ValidOmicsModelValidator implements ConstraintValidator<ValidOmicsModel, OmicsModel> {
    
    private final ValidPersonDropDownItemValidator validPersonDropDownItemValidator;
    private final NotNullValidator notNullValidator;
    private final PathExistsValidator pathExistsValidator;
    private final PathIsFileValidator pathIsFileValidator;
    private final NotBlankValidator notBlankValidator;
    private final ValidSamplingTypesValidator validSamplingTypesValidator;
    private final ValidExpectedAnalysesValidator validExpectedAnalysesValidator;

    public ValidOmicsModelValidator(ValidPersonDropDownItemValidator validPersonDropDownItemValidator, NotNullValidator notNullValidator,
        PathExistsValidator pathExistsValidator, PathIsFileValidator pathIsFileValidator, NotBlankValidator notBlankValidator,
        ValidSamplingTypesValidator validSamplingTypesValidator, ValidExpectedAnalysesValidator validExpectedAnalysesValidator) {
      this.validPersonDropDownItemValidator = validPersonDropDownItemValidator;
      this.notNullValidator = notNullValidator;
      this.pathExistsValidator = pathExistsValidator;
      this.pathIsFileValidator = pathIsFileValidator;
      this.notBlankValidator = notBlankValidator;
      this.validSamplingTypesValidator = validSamplingTypesValidator;
      this.validExpectedAnalysesValidator = validExpectedAnalysesValidator;
    }

    @Override
    public boolean isValid(OmicsModel value, ConstraintValidatorContext context) {
      if (!value.isSamplingConducted()) {
        return true;
      }
      
      context.disableDefaultConstraintViolation();

      boolean validPerson = validPersonDropDownItemValidator.isValid(value.getContact(), context);
      if (!validPerson) {
        context.buildConstraintViolationWithTemplate("invalid value")
            .addPropertyNode("contact")
            .addConstraintViolation();
      }
      
      boolean validTrackingSheet = notNullValidator.isValid(value.getSampleTrackingSheet(), context);
      if (!validTrackingSheet) {
        context.buildConstraintViolationWithTemplate("must not be blank")
            .addPropertyNode("sampleTrackingSheet")
            .addConstraintViolation();
      } else {
        validTrackingSheet = pathExistsValidator.isValid(value.getSampleTrackingSheet(), context);
        if (!validTrackingSheet) {
          context.buildConstraintViolationWithTemplate("path does not exist")
              .addPropertyNode("sampleTrackingSheet")
              .addConstraintViolation();
        } else {
          validTrackingSheet = pathIsFileValidator.isValid(value.getSampleTrackingSheet(), context);
          if (!validTrackingSheet) {
            context.buildConstraintViolationWithTemplate("path is not file")
                .addPropertyNode("sampleTrackingSheet")
                .addConstraintViolation();
          }
        }
      }
      
      boolean validProjectAccession = notBlankValidator.isValid(value.getBioProjectAccession(), context);
      if (!validProjectAccession) {
        context.buildConstraintViolationWithTemplate("must not be blank")
            .addPropertyNode("bioProjectAccession")
            .addConstraintViolation();
      }
      
      boolean validSamplingTypes = validSamplingTypesValidator.isValid(value.getSamplingTypes(), context);
      if (!validSamplingTypes) {
        context.buildConstraintViolationWithTemplate("no value selected")
            .addPropertyNode("samplingTypes")
            .addConstraintViolation();
      }
      
      boolean validExpectedAnalyses = validExpectedAnalysesValidator.isValid(value.getExpectedAnalyses(), context);
      if (!validExpectedAnalyses) {
        context.buildConstraintViolationWithTemplate("no value selected")
            .addPropertyNode("expectedAnalyses")
            .addConstraintViolation();
      }
      
      
      boolean validAdditionalSamplingInformation = notBlankValidator.isValid(value.getAdditionalSamplingInformation(), context); 
      if (!validAdditionalSamplingInformation) {
        context.buildConstraintViolationWithTemplate("must not be blank")
            .addPropertyNode("additionalSamplingInformation")
            .addConstraintViolation();
      }
      
      return validAdditionalSamplingInformation && validExpectedAnalyses && validSamplingTypes && validProjectAccession &&
          validTrackingSheet && validPerson;
    }
  }

}
