package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Target({ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DocumentsUnderMaxAllowed.DocumentsUnderMaxAllowedValidator.class)
@Documented
public @interface DocumentsUnderMaxAllowed {
  
  String message() default "documents exceeds max allowed documents";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  
  class DocumentsUnderMaxAllowedValidator implements ConstraintValidator<DocumentsUnderMaxAllowed, Path> {
    
    private final Integer maxAllowedDocuments;

    public DocumentsUnderMaxAllowedValidator(ServiceProperties serviceProperties) {
      this.maxAllowedDocuments = serviceProperties.getDocumentFilesErrorThreshold();
    }

    @Override
    public boolean isValid(Path value, ConstraintValidatorContext context) {
      if (value == null) {
        return true;
      }
      try (Stream<Path> paths = Files.walk(value)) {
        return paths
            .filter(p -> !Files.isDirectory(p))
            .filter(CruisePackFileUtils::filterHidden)
            .count() <= maxAllowedDocuments;
      } catch (IOException ignored) {
        return false;
      }
    }
  }

}
