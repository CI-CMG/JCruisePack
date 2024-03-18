package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

@Component
public class WaterColumnTemplateService {

  private static final String TEMPLATE_FILE_NAME = "sonar_calibration_information.xlsx";
  
  private final Path templatePath;

  public WaterColumnTemplateService(ServiceProperties serviceProperties) {
    this.templatePath = getTemplatePath(serviceProperties);
  }
  
  private Path getTemplatePath(ServiceProperties serviceProperties) {
    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path configDir = workDir.resolve("config");
    return configDir.resolve(TEMPLATE_FILE_NAME);
  }

  public void saveTemplate(Path path) {
    if (!Objects.requireNonNull(path, "path must not be null").toFile().isDirectory()) {
      throw new IllegalArgumentException("Path must be directory: " + path);
    }
    try (
        InputStream inputStream = new FileInputStream(templatePath.toFile());
        OutputStream outputStream = new FileOutputStream(path.resolve(TEMPLATE_FILE_NAME).toFile())
    ) {
      IOUtils.copy(
          Objects.requireNonNull(inputStream, "Failed to open template: " + TEMPLATE_FILE_NAME),
          outputStream
      );
    } catch (IOException e) {
      throw new IllegalStateException("Could not read import template", e);
    }
  }
}
