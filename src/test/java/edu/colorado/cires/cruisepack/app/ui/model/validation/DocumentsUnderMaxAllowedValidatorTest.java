package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.model.validation.DocumentsUnderMaxAllowed.DocumentsUnderMaxAllowedValidator;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DocumentsUnderMaxAllowedValidatorTest extends ConstraintValidatorTest<Path, DocumentsUnderMaxAllowedValidator> {
  
  private static final Path TEST_PATH = Paths.get("target/test");
  private static final int N_FILES = 10;
  
  @BeforeAll
  static void beforeAll() throws IOException {
    FileUtils.createParentDirectories(TEST_PATH.toFile());
    
    for (int i = 0; i < N_FILES; i++) {
      FileUtils.writeStringToFile(TEST_PATH.resolve(String.format(
          "test-%s.txt", i
      )).toFile(), "data", StandardCharsets.UTF_8);
      
      FileUtils.writeStringToFile(TEST_PATH.resolve(String.format(
          ".test-%s.txt", i
      )).toFile(), "data", StandardCharsets.UTF_8);
    }
  }
  
  @AfterAll
  static void afterAll() {
    FileUtils.deleteQuietly(TEST_PATH.toFile());
  }
  
  @Test
  void testValidator() throws IOException {
    assertTrue(validator.isValid(TEST_PATH, VALIDATOR_CONTEXT));
    
    FileUtils.writeStringToFile(TEST_PATH.resolve("test.txt").toFile(), "data", StandardCharsets.UTF_8);
    assertFalse(validator.isValid(TEST_PATH, VALIDATOR_CONTEXT));
    
    assertTrue(validator.isValid(null, VALIDATOR_CONTEXT));

    FileUtils.deleteQuietly(TEST_PATH.toFile());
    assertFalse(validator.isValid(TEST_PATH, VALIDATOR_CONTEXT));
  }

  @Override
  protected DocumentsUnderMaxAllowedValidator createValidator() {
    ServiceProperties serviceProperties = new ServiceProperties();
    serviceProperties.setDocumentFilesErrorThreshold(N_FILES);
    return new DocumentsUnderMaxAllowedValidator(serviceProperties);
  }
}
