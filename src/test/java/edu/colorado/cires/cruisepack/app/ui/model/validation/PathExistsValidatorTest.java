package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.cruisepack.app.ui.model.validation.PathExists.PathExistsValidator;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PathExistsValidatorTest extends ConstraintValidatorTest<Path, PathExistsValidator> {
  
  private static final Path TEST_PATH = Paths.get("target/test/test.txt");
  
  @BeforeAll
  static void beforeAll() throws IOException {
    FileUtils.createParentDirectories(TEST_PATH.toFile());
    FileUtils.writeStringToFile(TEST_PATH.toFile(), "data", StandardCharsets.UTF_8);
  }
  
  @AfterAll
  static void afterAll() {
    FileUtils.deleteQuietly(TEST_PATH.toFile());
  }
  
  @Test
  void testValidator() {
    assertTrue(validator.isValid(TEST_PATH, VALIDATOR_CONTEXT));
    FileUtils.deleteQuietly(TEST_PATH.toFile());
    assertFalse(validator.isValid(TEST_PATH, VALIDATOR_CONTEXT));
    assertTrue(validator.isValid(null, VALIDATOR_CONTEXT));
  }

  @Override
  protected PathExistsValidator createValidator() {
    return new PathExistsValidator();
  }
}
