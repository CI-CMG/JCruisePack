package edu.colorado.cires.cruisepack.app.service;

import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.mkDir;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.model.DatasetsModel;
import edu.colorado.cires.cruisepack.app.ui.model.FooterControlModel;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import jakarta.validation.Validator;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class PackagingValidationServiceTest {
  
  private static final Path TEST_PATH = Paths.get("target").resolve("test-data");
  
  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setDocumentFilesErrorThreshold(50);
    SERVICE_PROPERTIES.setDocumentFilesWarningThreshold(10);
  }
  
  private final PackageModel PACKAGE_MODEL = new PackageModel();
  private final OmicsModel OMICS_MODEL = new OmicsModel();
  private final CruiseInformationModel CRUISE_INFORMATION_MODEL = new CruiseInformationModel();
  private final DatasetsModel DATASETS_MODEL = new DatasetsModel(null);
  private final InstrumentDatastore INSTRUMENT_DATASTORE = new InstrumentDatastore(SERVICE_PROPERTIES);
  private final PeopleModel PEOPLE_MODEL = new PeopleModel();
  private final PersonDatastore PERSON_DATASTORE = new PersonDatastore(SERVICE_PROPERTIES, mock(ReactiveViewRegistry.class));
  private final FooterControlModel FOOTER_CONTROL_MODEL = new FooterControlModel();
  private final Map<String, PropertyChangeEvent> eventMap = new HashMap<>(0);
  
  private final PropertyChangeListener mapListener = evt -> eventMap.put(evt.getPropertyName(), evt);
  
  private void addListener() {
    PACKAGE_MODEL.addChangeListener(mapListener);
    OMICS_MODEL.addChangeListener(mapListener);
    CRUISE_INFORMATION_MODEL.addChangeListener(mapListener);
    DATASETS_MODEL.addChangeListener(mapListener);
    PEOPLE_MODEL.addChangeListener(mapListener);
    FOOTER_CONTROL_MODEL.addChangeListener(mapListener);
  }
  
  private void removeListener() {
    PACKAGE_MODEL.removeChangeListener(mapListener);
    OMICS_MODEL.removeChangeListener(mapListener);
    CRUISE_INFORMATION_MODEL.removeChangeListener(mapListener);
    DATASETS_MODEL.removeChangeListener(mapListener);
    PEOPLE_MODEL.removeChangeListener(mapListener);
    FOOTER_CONTROL_MODEL.removeChangeListener(mapListener);
  }
  
  @BeforeEach
  public void beforeEach() {
    FileUtils.deleteQuietly(
        TEST_PATH.toFile()
    );
    
    eventMap.clear();
    addListener();
  }
  
  @AfterEach
  public void afterEach() {
    FileUtils.deleteQuietly(
        TEST_PATH.toFile()
    );
    
    eventMap.clear();
    removeListener();
  }
  
  @Test
  public void testNumberOfDocumentsAtWarningThreshold() {
    Path docsDir = TEST_PATH.resolve("docs-dir");
    
    createDir(
        docsDir,
        10
    );
    
    try (MockedStatic<PackJobUtils> utils = Mockito.mockStatic(PackJobUtils.class)) {
      PackagingValidationService service = new PackagingValidationService(
          mock(Validator.class),
          PACKAGE_MODEL,
          OMICS_MODEL,
          CRUISE_INFORMATION_MODEL,
          DATASETS_MODEL,
          INSTRUMENT_DATASTORE,
          PEOPLE_MODEL,
          PERSON_DATASTORE,
          FOOTER_CONTROL_MODEL,
          SERVICE_PROPERTIES
      );
      
      utils.when(() -> PackJobUtils.create(
          PACKAGE_MODEL,
          PEOPLE_MODEL,
          OMICS_MODEL,
          CRUISE_INFORMATION_MODEL,
          DATASETS_MODEL,
          INSTRUMENT_DATASTORE,
          PERSON_DATASTORE
      )).thenReturn(PackJob.builder()
          .setCruiseId("test-cruise-id")
          .setDocumentsPath(docsDir)
          .build());
      
      
      service.validate();
      
      assertNull(eventMap.get(Events.UPDATE_JOB_WARNINGS));
    }
  }
  
  @Test
  public void testNumberOfDocumentsAboveWarningThreshold() {
    Path docsDir = TEST_PATH.resolve("docs-dir");

    createDir(
        docsDir,
        11
    );

    try (MockedStatic<PackJobUtils> utils = Mockito.mockStatic(PackJobUtils.class)) {
      PackagingValidationService service = new PackagingValidationService(
          mock(Validator.class),
          PACKAGE_MODEL,
          OMICS_MODEL,
          CRUISE_INFORMATION_MODEL,
          DATASETS_MODEL,
          INSTRUMENT_DATASTORE,
          PEOPLE_MODEL,
          PERSON_DATASTORE,
          FOOTER_CONTROL_MODEL,
          SERVICE_PROPERTIES
      );

      utils.when(() -> PackJobUtils.create(
          PACKAGE_MODEL,
          PEOPLE_MODEL,
          OMICS_MODEL,
          CRUISE_INFORMATION_MODEL,
          DATASETS_MODEL,
          INSTRUMENT_DATASTORE,
          PERSON_DATASTORE
      )).thenReturn(PackJob.builder()
          .setCruiseId("test-cruise-id")
          .setDocumentsPath(docsDir)
          .build());


      service.validate();

      PropertyChangeEvent event = eventMap.get(Events.UPDATE_JOB_WARNINGS);
      assertNotNull(event);
      assertEquals(Collections.emptyList(), event.getOldValue());
      assertEquals(List.of(String.format(
          "More than %s files located in documents directory: %s",
          SERVICE_PROPERTIES.getDocumentFilesWarningThreshold(), docsDir
      )), event.getNewValue());
    }
  }
  
  private static void createDir(Path path, int nFiles) {
    mkDir(path);
    
    for (int i = 0; i < nFiles; i++) {
      String fileName = String.format(
          "test-%s", i
      );
      try (FileWriter writer = new FileWriter(
        path.resolve(fileName).toFile(),
        StandardCharsets.UTF_8
      )) {
        writer.write(String.format(
            "test content\t\t%s",
            i
        ));
      } catch (IOException e) {
        throw new IllegalStateException(String.format(
            "Unable to write to file: %s", path.resolve(fileName)
        ));
      }
    }
  }

}
