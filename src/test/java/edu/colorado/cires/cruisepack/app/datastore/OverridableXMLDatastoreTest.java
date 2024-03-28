package edu.colorado.cires.cruisepack.app.datastore;

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
    try (OutputStream outputStream = new FileOutputStream(TEST_LOCAL_DATA_PATH.resolve(getXMLFilename()).toFile())) {
      JAXB.marshal(dataObject, outputStream);
    }
  }
  
  protected T readLocalFile(Class<T> clazz) {
    Path path = TEST_LOCAL_DATA_PATH.resolve(getXMLFilename());
    try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      Object deserialized = JAXBContext.newInstance(clazz).createUnmarshaller().unmarshal(reader);
      return (T) deserialized;
    } catch (JAXBException | IOException e) {
      throw new IllegalStateException("Unable to parse " + path, e);
    }
  }

  protected abstract T createDataObject();

}
