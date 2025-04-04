package edu.colorado.cires.cruisepack.app.datastore;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;

public abstract class OverridableXMLDatastoreTest<T> extends XMLDatastoreTest<T> {
  
  protected static final Path TEST_LOCAL_DATA_PATH = TEST_PATH.resolve("local-data");

  @Override
  void beforeEach() throws IOException {
    super.beforeEach();
    FileUtils.forceMkdir(TEST_LOCAL_DATA_PATH.toFile());
    
    T dataObject = createDataObject();
    ObjectMapper objectMapper = new ObjectMapper();
    try (OutputStream outputStream = new FileOutputStream(TEST_LOCAL_DATA_PATH.resolve(getJSONFilename()).toFile())) {
      objectMapper.writeValue(outputStream, dataObject);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to serialize data object", e);
    }
  }
  
  protected T readLocalFile(Class<T> clazz) {
    Path path = TEST_LOCAL_DATA_PATH.resolve(getJSONFilename());
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(Files.newBufferedReader(path, StandardCharsets.UTF_8), clazz);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to parse " + path, e);
    }
  }

  protected abstract T createDataObject();

}
