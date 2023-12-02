package edu.colorado.cires.cruisepack.startup;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.core.config.Configurator;

public class CruisePackInitializer {

  public static void initialize() throws IOException {
    Path configHome = resolveHome();
    if (!Files.isDirectory(configHome)) {
      Files.createDirectories(configHome);
    }
    setupLogging(configHome);
  }

  private static Path resolveHome() {
    String cruisePackHome = System.getProperty("cruisepack.home");
    Path configHome;
    if (cruisePackHome == null || cruisePackHome.trim().isEmpty()) {
      configHome = Paths.get(System.getProperty("user.dir"), ".cruisepack");
      cruisePackHome = configHome.toString().replaceAll("\\\\", "/");
      System.setProperty("cruisepack.home", cruisePackHome);
    } else {
      cruisePackHome = cruisePackHome.toString().trim().replaceAll("\\\\", "/");
      configHome = Paths.get(cruisePackHome);
      System.setProperty("cruisepack.home", cruisePackHome);
    }
    return configHome.toAbsolutePath().normalize();
  }

  private static void setupLogging(Path configHome) throws IOException {
    System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
    Path log4jFile = configHome.resolve("log4j2.xml");
    if (!Files.exists(log4jFile)) {
      try (InputStream in = CruisePackInitializer.class.getResourceAsStream("log4j2.xml")) {
        Files.copy(in, log4jFile);
      }
    }
    Configurator.reconfigure(log4jFile.toUri());
  }

}
