package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidWaterColumnFields.ValidWaterColumnFieldsValidator.class)
@Documented
public @interface ValidWaterColumnFields {
  
  String message() default "calibration date and files must be specified";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  
  class ValidWaterColumnFieldsValidator implements ConstraintValidator<ValidWaterColumnFields, WaterColumnAdditionalFieldsModel> {
    @Override
    public boolean isValid(WaterColumnAdditionalFieldsModel value, ConstraintValidatorContext context) {
      DropDownItem calibrationState = value.getCalibrationState();
      if (calibrationState != null) {
        boolean valid = true;

        if (calibrationState.getValue().contains("w/ calibration data") && (value.getCalibrationReportPath() == null || value.getCalibrationDataPath() == null)) {
          context.disableDefaultConstraintViolation();
          
          if (value.getCalibrationDataPath() == null) {
            context.buildConstraintViolationWithTemplate("must not be blank")
                .addPropertyNode("calibrationDataPath")
                .addConstraintViolation();
          }
          
          if (value.getCalibrationReportPath() == null) {
            context.buildConstraintViolationWithTemplate("must not be blank")
                .addPropertyNode("calibrationReportPath")
                .addConstraintViolation();
          }
          
          valid = false;
        }
        
        if (calibrationState.getValue().contains("Calibrated") && value.getCalibrationDate() == null) {
          context.disableDefaultConstraintViolation();
          
          context.buildConstraintViolationWithTemplate("must not be blank")
              .addPropertyNode("calibrationDate")
              .addConstraintViolation();
          
          valid = false;
        }
        
        return valid;
      }
      
      return true;
    }
  } 

}
