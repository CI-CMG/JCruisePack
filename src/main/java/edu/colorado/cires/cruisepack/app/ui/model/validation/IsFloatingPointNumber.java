package edu.colorado.cires.cruisepack.app.ui.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

@Target({ ElementType.FIELD, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsFloatingPointNumber.IsFloatingPointNumberValidator.class)
@Documented
public @interface IsFloatingPointNumber {
  
  String message() default "must be a floating point number";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  
  class IsFloatingPointNumberValidator implements ConstraintValidator<IsFloatingPointNumber, String> {

    private static boolean isFloat(String str) {
      String floatRegex = "^-?[0-9]*\\.?[0-9]+$";
      return Pattern.matches(floatRegex, str);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (StringUtils.isBlank(value)) {
        return true; // responsibility of separate validator
      }
      return isFloat(value);
    }
  }

}
