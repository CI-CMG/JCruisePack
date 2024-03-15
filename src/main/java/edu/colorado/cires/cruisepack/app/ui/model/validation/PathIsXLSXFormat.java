package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsXLSXFormat.PathIsXLSXFormatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Path;

@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PathIsXLSXFormatValidator.class)
@Documented
public @interface PathIsXLSXFormat {
  
  String message() default "not xlsx file type";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
  
  
  class PathIsXLSXFormatValidator implements ConstraintValidator<PathIsXLSXFormat, Path> {

    @Override
    public boolean isValid(Path value, ConstraintValidatorContext context) {
      if (value == null) {
        return true; // Checked in separate annotation
      }
      return value.toString().endsWith("xlsx");
    }
  }

}
