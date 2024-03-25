package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.ui.model.validation.PathExists.PathExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Path;
import org.springframework.stereotype.Component;

@Target({ ElementType.FIELD, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(PathExists.List.class)
@Constraint(validatedBy = PathExistsValidator.class)
@Documented
public @interface PathExists {

  String message() default "path does not exist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
  
  @Target({ElementType.FIELD, ElementType.TYPE_USE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface List {
    PathExists[] value();
  }
  @Component
  class PathExistsValidator implements ConstraintValidator<PathExists, Path> {

    @Override
    public boolean isValid(Path value, ConstraintValidatorContext context) {
      if (value == null) {
        return true; // should be checking null in a different validator
      }
      
      return value.toFile().exists();
    }
  }

}
