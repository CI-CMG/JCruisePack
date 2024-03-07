package edu.colorado.cires.cruisepack.app.init;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.Resource;

public class CruisePackPreSpringStarter {

  private static void copyFromClassPath(Path target, String classPathFile) {
    try (InputStream inputStream = CruisePackPreSpringStarter.class.getResourceAsStream(classPathFile)) {
      Files.copy(inputStream, target);
    } catch (IOException e) {
      throw new RuntimeException("Unable to copy resource: "+ classPathFile, e);
    }
  }

  public static void start()  {
    try {
      Path workDir = Paths.get(System.getProperty("cruise-pack.work-dir"));
      Path logDir = workDir.resolve("log");
      Files.createDirectories(logDir);
      Path configDir = workDir.resolve("config");
      Files.createDirectories(configDir);
      Path dataDir = workDir.resolve("data");
      Files.createDirectories(dataDir);
      Path cruiseMetadataDir = workDir.resolve("local-data/cruise-metadata");
      Files.createDirectories(cruiseMetadataDir);


      Path applicationProperties = configDir.resolve("application.properties");
      Path log4jXml = configDir.resolve("log4j2.xml");

      if (!Files.isRegularFile(applicationProperties)) {
        System.out.println("Initializing application.properties");
        copyFromClassPath(applicationProperties, "/edu/colorado/cires/cruisepack/app/init/application.properties");
      }

      if (!Files.isRegularFile(log4jXml)) {
        System.out.println("Initializing log4j2.xml");
        copyFromClassPath(log4jXml, "/edu/colorado/cires/cruisepack/app/init/log4j2.xml");
      }

      Resource[] resources = CruisePackDataInitializer.getPackagedData();
      for (Resource resource : resources) {
        Path dataFile = dataDir.resolve(resource.getFilename());
        if(!Files.isRegularFile(dataFile)) {
          System.out.println("Initializing " + resource.getFilename());
          Files.write(dataFile, resource.getContentAsByteArray());
        }
      }

    } catch (Exception e) {
      throw new RuntimeException("Unable to start CruisePack", e);
    }

  }
}
