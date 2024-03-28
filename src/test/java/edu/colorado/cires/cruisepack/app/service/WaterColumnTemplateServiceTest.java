package edu.colorado.cires.cruisepack.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WaterColumnTemplateServiceTest {
  
  private static final Path TEST_PATH = Paths.get("target").resolve("test-dir");
  private static final Path WORK_DIR = TEST_PATH.resolve("work-dir");
  private static final Path CONFIG_DIR = WORK_DIR.resolve("config");
  private static final Path OUTPUT_DIR = TEST_PATH.resolve("output-dir");
  private static final String TEMPLATE_NAME = "sonar_calibration_information.xlsx";
  
  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(WORK_DIR.toString());
  }
  
  private final WaterColumnTemplateService service = new WaterColumnTemplateService(SERVICE_PROPERTIES);
  
  @BeforeEach
  void beforeEach() throws IOException {
    FileUtils.deleteQuietly(TEST_PATH.toFile());
    
    FileUtils.forceMkdir(CONFIG_DIR.toFile());
    FileUtils.forceMkdir(OUTPUT_DIR.toFile());
    
    FileUtils.copyFile(
        Paths.get("src/main/resources/edu/colorado/cires/cruisepack/app/init").resolve(TEMPLATE_NAME).toFile(),
        CONFIG_DIR.resolve(TEMPLATE_NAME).toFile()
    );
  }
  
  @AfterEach
  void afterEach() {
    FileUtils.deleteQuietly(TEST_PATH.toFile());
  }

  @Test
  void saveTemplate() {
    Path outputFile = OUTPUT_DIR;
    
    service.saveTemplate(outputFile);
    
    try (
        InputStream sourceFile = new FileInputStream(CONFIG_DIR.resolve(TEMPLATE_NAME).toFile());
        InputStream savedFile = new FileInputStream(outputFile.resolve(TEMPLATE_NAME).toFile())
    ) {
      assertEquals(
          DigestUtils.sha256Hex(sourceFile),
          DigestUtils.sha256Hex(savedFile)
      );
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read template files", e);
    }
  }

  @Test
  void saveTemplateFileArgument() {
    Path outputFile = OUTPUT_DIR.resolve(TEMPLATE_NAME);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> service.saveTemplate(outputFile));
    assertEquals(String.format(
        "Path must be directory: %s", outputFile
    ), exception.getMessage());
  }
  
  @Test
  void saveTemplateTemplateNotFound() throws IOException {
    Path outputFile = OUTPUT_DIR;
    
    FileUtils.delete(CONFIG_DIR.resolve(TEMPLATE_NAME).toFile());

    Exception exception = assertThrows(IllegalStateException.class, () -> service.saveTemplate(outputFile));
    assertEquals(String.format(
        "Could not read import template: %s", CONFIG_DIR.resolve(TEMPLATE_NAME)
    ), exception.getMessage());
  }
}