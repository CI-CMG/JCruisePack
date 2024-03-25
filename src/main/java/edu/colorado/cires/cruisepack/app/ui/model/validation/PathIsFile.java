package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsFile.PathIsFileValidator;
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

@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(PathIsFile.List.class)
@Constraint(validatedBy = PathIsFileValidator.class)
@Documented
public @interface PathIsFile {
  
  String message() default "path is not file";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  
  @Component
  class PathIsFileValidator implements ConstraintValidator<PathIsFile, Path> {

    @Override
    public boolean isValid(Path value, ConstraintValidatorContext context) {
      if (value == null) {
        return true; // null check in different annotation
      }
      
      return value.toFile().isFile();
    }
  }

  @Target({ElementType.FIELD, ElementType.TYPE_USE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface List {
    PathIsFile[] value();
  }

}
