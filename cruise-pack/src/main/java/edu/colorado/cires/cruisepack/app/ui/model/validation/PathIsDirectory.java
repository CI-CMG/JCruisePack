package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsDirectory.PathIsDirectoryValidator;
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

@Target({ ElementType.FIELD, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(PathIsDirectory.List.class)
@Constraint(validatedBy = PathIsDirectoryValidator.class)
@Documented
public @interface PathIsDirectory {
  
  String message() default "path is not a directory";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  
  class PathIsDirectoryValidator implements ConstraintValidator<PathIsDirectory, Path> {

    @Override
    public boolean isValid(Path value, ConstraintValidatorContext context) {
      if (value == null) {
        return true; // null checked in different annotation
      }
      return value.toFile().isDirectory();
    }
  }

  @Target({ ElementType.FIELD, ElementType.TYPE_USE })
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface List {
    PathIsDirectory[] value();
  }

}
