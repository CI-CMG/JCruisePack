package edu.colorado.cires.cruisepack.app.ui.model.validation;

import edu.colorado.cires.cruisepack.app.service.InstrumentDetail;
import edu.colorado.cires.cruisepack.app.service.InstrumentDetailPackageKey;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.ui.model.validation.EnoughDiskSpace.EnoughDiskSpaceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.io.FileUtils;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnoughDiskSpaceValidator.class)
@Documented
public @interface EnoughDiskSpace {
  
  String message() default "Not enough disk space";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
  class EnoughDiskSpaceValidator implements ConstraintValidator<EnoughDiskSpace, PackJob> {

    @Override
    public boolean isValid(PackJob value, ConstraintValidatorContext context) {
      try {
        BigInteger available = BigInteger.valueOf(
            Files.getFileStore(
                value.getPackageDirectory()
            ).getUsableSpace()
        );
        
        available = subtractIfExists(available, value.getDocumentsPath());
        available = subtractIfExists(available, value.getOmicsSampleTrackingSheetPath());
        
        for (Entry<InstrumentDetailPackageKey, List<InstrumentDetail>> entry : value.getInstruments().entrySet() ) {
          for (InstrumentDetail instrumentDetail : entry.getValue()) {
              available = subtractIfExists(available, instrumentDetail.getDataPath());
          }
        }
        
        return available.signum() == 1;
      } catch (IOException e) {
        throw new IllegalStateException("Unable to execute disk space check: " + e);
      }
    }
    
    private static BigInteger subtractIfExists(BigInteger bigInteger, Path path) {
      BigInteger directorySize = getDirectorySize(path);
      
      if (directorySize != null) {
        bigInteger = bigInteger.subtract(directorySize);
      }
      
      return bigInteger;
    }
    
    private static BigInteger getDirectorySize(Path path) {
      if (path != null) {
        File file = path.toFile();
        return FileUtils.sizeOfDirectoryAsBigInteger(file);
      }
      
      return null;
    }
  }

}
