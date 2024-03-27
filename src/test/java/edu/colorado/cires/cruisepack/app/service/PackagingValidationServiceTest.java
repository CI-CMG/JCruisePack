package edu.colorado.cires.cruisepack.app.service;

import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.mkDir;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.model.DatasetsModel;
import edu.colorado.cires.cruisepack.app.ui.model.ErrorModel;
import edu.colorado.cires.cruisepack.app.ui.model.FooterControlModel;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.OptionPaneGenerator;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
  private final PersonDatastore PERSON_DATASTORE = new PersonDatastore(SERVICE_PROPERTIES);
  private final FooterControlModel FOOTER_CONTROL_MODEL = new FooterControlModel();
  private final ErrorModel ERROR_MODEL = new ErrorModel();
  private final OptionPaneGenerator OPTION_PANE_GENERATOR = mock(OptionPaneGenerator.class);
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
          SERVICE_PROPERTIES,
          ERROR_MODEL,
          OPTION_PANE_GENERATOR
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
          SERVICE_PROPERTIES,
          ERROR_MODEL,
          OPTION_PANE_GENERATOR
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
      

      String expectedMessage = String.format(
          "More than %s files located in documents directory: %s",
          SERVICE_PROPERTIES.getDocumentFilesWarningThreshold(), docsDir
      );

      ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<String> titleCaptor = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<Integer> messageTypeCaptor = ArgumentCaptor.forClass(Integer.class);
      ArgumentCaptor<String> iconCaptor = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<String[]> choicesCaptor = ArgumentCaptor.forClass(String[].class);
      ArgumentCaptor<String> highlightedChoiceCaptor = ArgumentCaptor.forClass(String.class);
      verify(OPTION_PANE_GENERATOR, times(1)).createOptionPane(
          messageCaptor.capture(),
          titleCaptor.capture(),
          messageTypeCaptor.capture(),
          iconCaptor.capture(),
          choicesCaptor.capture(),
          highlightedChoiceCaptor.capture()
      );
      
      assertEquals(1, messageCaptor.getAllValues().size());
      assertEquals(expectedMessage, messageCaptor.getAllValues().get(0));
      
      assertEquals(1, titleCaptor.getAllValues().size());
      assertNull(titleCaptor.getAllValues().get(0));
      
      assertEquals(1, messageTypeCaptor.getAllValues().size());
      assertEquals(JOptionPane.WARNING_MESSAGE, messageTypeCaptor.getAllValues().get(0));
      
      assertEquals(1, iconCaptor.getAllValues().size());
      assertEquals("JOptionPane.warningIcon", iconCaptor.getAllValues().get(0));
      
      assertEquals(1, choicesCaptor.getAllValues().size());
      assertArrayEquals(new String[] {
          "Cancel Job", "Continue Job"
      }, choicesCaptor.getAllValues().get(0));
      
      assertEquals(1, highlightedChoiceCaptor.getAllValues().size());
      assertEquals("Cancel Job", highlightedChoiceCaptor.getAllValues().get(0));
    }
  }

  @Test
  public void testEmptyPackageWarning() throws IOException {
    Path docsDir = TEST_PATH.resolve("docs-dir");
    
    Path emptyDir1 = TEST_PATH.resolve("test-1");
    Path emptyDir2 = TEST_PATH.resolve("test-2");
    
    FileUtils.forceMkdir(emptyDir1.toFile());
    FileUtils.forceMkdir(emptyDir2.toFile());

    createDir(
        docsDir,
        8
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
          SERVICE_PROPERTIES,
          ERROR_MODEL,
          OPTION_PANE_GENERATOR
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
              .setInstruments(Map.of(
                  new InstrumentDetailPackageKey(
                      InstrumentGroupName.SUB_BOTTOM.getShortName(), "TEST-1"
                  ),
                  Collections.singletonList(InstrumentDetail.builder()
                          .setDataPath(emptyDir1.toString())
                      .build()),
                  new InstrumentDetailPackageKey(
                      InstrumentGroupName.XBT.getShortName(), "TEST-2"
                  ),
                  Collections.singletonList(InstrumentDetail.builder()
                          .setDataPath(emptyDir2.toString())
                      .build())
              ))
          .build());

      when(OPTION_PANE_GENERATOR.createOptionPane(any(), any(), any(Integer.class), any(), any(), any())).thenReturn(-1);

      service.validate();


      Set<String> expectedMessages = Stream.of(emptyDir1, emptyDir2)
          .map(Path::toAbsolutePath)
          .map(p -> String.format(
              "%s is empty and will not appear in output package", p
          ))
          .collect(Collectors.toSet());

      ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<String> titleCaptor = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<Integer> messageTypeCaptor = ArgumentCaptor.forClass(Integer.class);
      ArgumentCaptor<String> iconCaptor = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<String[]> choicesCaptor = ArgumentCaptor.forClass(String[].class);
      ArgumentCaptor<String> highlightedChoiceCaptor = ArgumentCaptor.forClass(String.class);
      verify(OPTION_PANE_GENERATOR, times(2)).createOptionPane(
          messageCaptor.capture(),
          titleCaptor.capture(),
          messageTypeCaptor.capture(),
          iconCaptor.capture(),
          choicesCaptor.capture(),
          highlightedChoiceCaptor.capture()
      );

      assertEquals(2, messageCaptor.getAllValues().size());
      assertEquals(expectedMessages, new HashSet<>(messageCaptor.getAllValues()));

      assertEquals(2, titleCaptor.getAllValues().size());
      titleCaptor.getAllValues().forEach(Assertions::assertNull);

      assertEquals(2, messageTypeCaptor.getAllValues().size());
      assertEquals(List.of(
          JOptionPane.WARNING_MESSAGE, JOptionPane.WARNING_MESSAGE
      ), messageTypeCaptor.getAllValues());

      assertEquals(2, iconCaptor.getAllValues().size());
      assertEquals(List.of(
          "JOptionPane.warningIcon", "JOptionPane.warningIcon"
      ), iconCaptor.getAllValues());

      assertEquals(2, choicesCaptor.getAllValues().size());
      choicesCaptor.getAllValues().forEach(v -> assertArrayEquals(new String[] {
          "Cancel Job", "Continue Job"
      }, v));
      assertEquals(2, highlightedChoiceCaptor.getAllValues().size());
      assertEquals(List.of(
          "Cancel Job", "Cancel Job"
      ), highlightedChoiceCaptor.getAllValues());
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
