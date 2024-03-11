package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidPersonDropDownItem.ValidPersonDropDownItemValidator;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.nio.file.Path;
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

    @Override
    public boolean isValid(OmicsModel value, ConstraintValidatorContext context) {
      if (!value.isSamplingConducted()) {
        return true;
      }
      
      context.disableDefaultConstraintViolation();

      boolean validPerson = new ValidPersonDropDownItemValidator().isValid(value.getContact(), context);
      if (!validPerson) {
        context.buildConstraintViolationWithTemplate("invalid value")
            .addPropertyNode("contact")
            .addConstraintViolation();
      }
      
      boolean validTrackingSheet = new NotNullValidator().isValid(value.getSampleTrackingSheet(), context);
      if (!validTrackingSheet) {
        context.buildConstraintViolationWithTemplate("must not be blank")
            .addPropertyNode("sampleTrackingSheet")
            .addConstraintViolation();
      } else {
        validTrackingSheet = new PathExists.PathExistsValidator().isValid(value.getSampleTrackingSheet(), context);
        if (!validTrackingSheet) {
          context.buildConstraintViolationWithTemplate("path does not exist")
              .addPropertyNode("sampleTrackingSheet")
              .addConstraintViolation();
        } else {
          validTrackingSheet = new PathIsFile.PathIsFileValidator().isValid(value.getSampleTrackingSheet(), context);
          if (!validTrackingSheet) {
            context.buildConstraintViolationWithTemplate("path is not file")
                .addPropertyNode("sampleTrackingSheet")
                .addConstraintViolation();
          }
        }
      }
      
      boolean validProjectAccession = new NotBlankValidator().isValid(value.getBioProjectAccession(), context);
      if (!validProjectAccession) {
        context.buildConstraintViolationWithTemplate("must not be blank")
            .addPropertyNode("bioProjectAccession")
            .addConstraintViolation();
      }
      
      boolean validSamplingTypes = new ValidSamplingTypes.ValidSamplingTypesValidator().isValid(value.getSamplingTypes(), context);
      if (!validSamplingTypes) {
        context.buildConstraintViolationWithTemplate("no value selected")
            .addPropertyNode("samplingTypes")
            .addConstraintViolation();
      }
      
      boolean validExpectedAnalyses = new ValidExpectedAnalyses.ValidExpectedAnalysesValidator().isValid(value.getExpectedAnalyses(), context);
      if (!validExpectedAnalyses) {
        context.buildConstraintViolationWithTemplate("no value selected")
            .addPropertyNode("expectedAnalyses")
            .addConstraintViolation();
      }
      
      
      boolean validAdditionalSamplingInformation = new NotBlankValidator().isValid(value.getAdditionalSamplingInformation(), context); 
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
