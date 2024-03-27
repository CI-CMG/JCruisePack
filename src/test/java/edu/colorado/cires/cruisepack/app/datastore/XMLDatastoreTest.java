package edu.colorado.cires.cruisepack.app.datastore;

import edu.colorado.cires.cruisepack.app.init.CruisePackDataInitializer;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.core.io.Resource;

abstract class XMLDatastoreTest<T> {
  
  protected static final Path TEST_PATH = Paths.get("target").resolve("test-data");
  
  @BeforeEach
  void beforeEach() throws IOException {
    FileUtils.deleteQuietly(TEST_PATH.toFile());
    load();
  }
  
  @AfterEach
  void afterEach() {
    FileUtils.deleteQuietly(TEST_PATH.toFile());
  }
  
  protected abstract String getXMLFilename();
  
  private void load() throws IOException {
    Resource[] resources = CruisePackDataInitializer.getPackagedData();
    for (Resource resource : resources) {
      if (!resource.getFilename().equals(getXMLFilename())) {
        continue;
      }
      Path dataFile = TEST_PATH.resolve("data").resolve(resource.getFilename());
      FileUtils.createParentDirectories(dataFile.toFile());
      if (!Files.isRegularFile(dataFile)) {
        System.out.println("Initializing " + resource.getFilename());
        Files.write(dataFile, resource.getContentAsByteArray());
      }
    }
  }
  
  protected T readFile(Class<T> clazz) {
    Path path = TEST_PATH.resolve("data").resolve(getXMLFilename());
    try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      Object deserialized = JAXBContext.newInstance(clazz).createUnmarshaller().unmarshal(reader);
      return (T) deserialized;
    } catch (JAXBException | IOException e) {
      throw new IllegalStateException("Unable to parse " + path, e);
    }
  }
  
  record NameUUIDPair(String uuid, String name) {}

}
