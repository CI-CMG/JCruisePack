package edu.colorado.cires.cruisepack.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.ImportModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.OptionPaneGenerator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.metadata.ConstraintDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.io.FileUtils;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ImportServiceTest {
  
  private static final Path TEST_DIR = Paths.get("target/test-dir");
  
  private static final ServiceProperties SERVICE_PROPERTIES = new ServiceProperties();
  static {
    SERVICE_PROPERTIES.setWorkDir(TEST_DIR.toString());
  }
  
  private static final CruiseDataDatastore datastore = mock(CruiseDataDatastore.class);
  
  private final Validator validator = mock(Validator.class);
  private final OptionPaneGenerator optionPaneGenerator = mock(OptionPaneGenerator.class);
  
  private final ImportService service = new ImportService(
      validator,
      datastore,
      SERVICE_PROPERTIES,
      optionPaneGenerator
  );
  
  @BeforeEach
  void beforeEach() throws IOException {
    reset(validator);
    FileUtils.deleteQuietly(TEST_DIR.toFile());
    FileUtils.forceMkdir(TEST_DIR.toFile());
  }
  
  @AfterEach
  void afterEach() {
    FileUtils.deleteQuietly(TEST_DIR.toFile());
  }

  @Test
  void saveTemplate() throws IOException {
    String templateName = "cruise_import.xlsx";
    Path sourceDocsPath = TEST_DIR.resolve("config").resolve(templateName);
    FileUtils.createParentDirectories(sourceDocsPath.toFile());
    try (FileWriter fileWriter = new FileWriter(sourceDocsPath.toFile(), StandardCharsets.UTF_8)) {
      fileWriter.write("test template content");
    }
    
    Path targetDocsPath = TEST_DIR.resolve("out");
    FileUtils.forceMkdir(targetDocsPath.toFile());
    service.saveTemplate(targetDocsPath);
    
    String templateContent = FileUtils.readFileToString(targetDocsPath.resolve(templateName).toFile(), StandardCharsets.UTF_8);
    assertEquals("test template content", templateContent);
  }

  @Test
  void importCruises() throws Exception {
    Path spreadsheetPath = TEST_DIR.resolve("to_import.xlsx");
    
    List<ImportRow> importRows = IntStream.range(0, 10).boxed()
        .map(i -> createImportRow(i.toString(), 2))
        .toList();
    
    createExcelFile(
        spreadsheetPath,
        importRows
    );
    
    Path destinationPath = TEST_DIR.resolve("destination");
    FileUtils.forceMkdir(destinationPath.toFile());

    ImportModel importModel = new ImportModel();
    importModel.setImportPath(spreadsheetPath);
    importModel.setDestinationPath(destinationPath);
    importModel.setMetadataAuthor(new DropDownItem("1", "test-metadata-author"));

    when(datastore.saveCruise(
        any(), any(), any(), any(Boolean.class), any(Boolean.class)
    )).thenReturn(ResponseStatus.SUCCESS);
    
    service.importCruises(importModel);

    ArgumentCaptor<ImportRow> importRowArgumentCaptor = ArgumentCaptor.forClass(ImportRow.class);
    ArgumentCaptor<Path> destinationPathArgumentCaptor = ArgumentCaptor.forClass(Path.class);
    ArgumentCaptor<String> metadataAuthorNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Boolean> overwriteArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
    ArgumentCaptor<Boolean> skipArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
    verify(datastore, times(10)).saveCruise(
        importRowArgumentCaptor.capture(),
        destinationPathArgumentCaptor.capture(),
        metadataAuthorNameArgumentCaptor.capture(),
        overwriteArgumentCaptor.capture(),
        skipArgumentCaptor.capture()
    );
    
    List<ImportRow> importRowArguments = importRowArgumentCaptor.getAllValues();
    List<Path> destinationPathArguments = destinationPathArgumentCaptor.getAllValues();
    List<String> metadataAuthorNameArguments = metadataAuthorNameArgumentCaptor.getAllValues();
    List<Boolean> overwriteArguments = overwriteArgumentCaptor.getAllValues();
    List<Boolean> skipArguments = skipArgumentCaptor.getAllValues();
    assertEquals(importRowArguments.size(), destinationPathArguments.size());
    assertEquals(destinationPathArguments.size(), metadataAuthorNameArguments.size());
    assertEquals(overwriteArguments.size(), destinationPathArguments.size());
    assertEquals(skipArguments.size(), overwriteArguments.size());
    
    for (int i = 0; i < importRowArguments.size(); i++) {
      assertEquals(importModel.getDestinationPath(), destinationPathArguments.get(i));
      assertEquals(importModel.getMetadataAuthor().getValue(), metadataAuthorNameArguments.get(i));
      assertFalse(overwriteArguments.get(i));
      assertFalse(skipArguments.get(i));
      assertImportRowsEqual(importRows.get(i), importRowArguments.get(i));
    }
  }

  @Test
  public void testImportEmptyModel() throws Exception {
    ImportModel importModel = new ImportModel();
    when(validator.validate(importModel)).thenReturn(Set.of(
        new CustomConstrainViolation("invalid author", "metadataAuthor"),
        new CustomConstrainViolation("invalid destination path", "destinationPath"),
        new CustomConstrainViolation("invalid import path", "importPath")
    ));
    
    service.importCruises(importModel);
    verify(datastore, times(0)).saveCruise(any(), any(), any(), any(Boolean.class), any(Boolean.class));
    
    assertEquals("invalid author", importModel.getMetadataAuthorError());
    assertEquals("invalid destination path", importModel.getDestinationPathError());
    assertEquals("invalid import path", importModel.getImportPathError());
  }
  
  private void assertImportRowsEqual(ImportRow expected, ImportRow actual) {
    assertEquals(expected.getShipName(), actual.getShipName());
    assertEquals(expected.getCruiseID(), actual.getCruiseID());
    assertEquals(expected.getLeg(), actual.getLeg());
    assertEquals(expected.getChiefScientist(), actual.getChiefScientist());
    assertEquals(expected.getSponsorOrganization(), actual.getSponsorOrganization());
    assertEquals(expected.getFundingOrganization(), actual.getFundingOrganization());
    assertEquals(expected.getDeparturePort(), actual.getDeparturePort());
    assertEquals(expected.getStartDate(), actual.getStartDate());
    assertEquals(expected.getArrivalPort(), actual.getArrivalPort());
    assertEquals(expected.getEndDate(), actual.getEndDate());
    assertEquals(expected.getSeaArea(), actual.getSeaArea());
    assertEquals(expected.getProjectName(), actual.getProjectName());
    assertEquals(expected.getCruiseTitle(), actual.getCruiseTitle());
    assertEquals(expected.getCruisePurpose(), actual.getCruisePurpose());
    assertEquals(expected.getCTDInstruments(), actual.getCTDInstruments());
    assertEquals(expected.getMBESInstruments(), actual.getMBESInstruments());
    assertEquals(expected.getSBESInstruments(), actual.getSBESInstruments());
    assertEquals(expected.getWaterColumnInstruments(), actual.getWaterColumnInstruments());
    assertEquals(expected.getADCPInstruments(), actual.getADCPInstruments());
    assertEquals(expected.getComments(), actual.getComments());
  }
  
  private void createExcelFile(Path excelPath, List<ImportRow> rows) throws IOException {
    try (
        OutputStream outputStream = new FileOutputStream(excelPath.toFile());
        Workbook workbook = new Workbook(outputStream, "CruisePack-test", "0.0")
    ) {
      Worksheet worksheet = workbook.newWorksheet("test-sheet");
      
      List<ImportRow> rowsWithEmptyRow = new ArrayList<>(rows);
      rowsWithEmptyRow.add(createEmptyRow());
      
      worksheet.value(0, 0, "template help text");
      worksheet.value(1, 0, "template headers");
      
      for (int i = 0; i < rowsWithEmptyRow.size(); i++) {
        ImportRow row = rowsWithEmptyRow.get(i);
        
        worksheet.value(i + 2, 0, row.getShipName());
        worksheet.value(i + 2, 1, row.getCruiseID());
        worksheet.value(i + 2, 2, row.getLeg());
        worksheet.value(i + 2, 3, row.getChiefScientist());
        worksheet.value(i + 2, 4, row.getSponsorOrganization());
        worksheet.value(i + 2, 5, row.getFundingOrganization());
        worksheet.value(i + 2, 6, row.getDeparturePort());
        worksheet.value(i + 2, 7, row.getStartDate());
        worksheet.value(i + 2, 8, row.getArrivalPort());
        worksheet.value(i + 2, 9, row.getEndDate());
        worksheet.value(i + 2, 10, row.getSeaArea());
        worksheet.value(i + 2, 11, row.getProjectName());
        worksheet.value(i + 2, 12, row.getCruiseTitle());
        worksheet.value(i + 2, 13, row.getCruisePurpose());
        worksheet.value(i + 2, 14, row.getCTDInstruments() == null ? null : String.join(
            ";", row.getCTDInstruments()
        ));
        worksheet.value(i + 2, 15, row.getMBESInstruments() == null ? null : String.join(
            ";", row.getMBESInstruments()
        ));
        worksheet.value(i + 2, 16, row.getSBESInstruments() == null ? null : String.join(
            ";", row.getSBESInstruments()
        ));
        worksheet.value(i + 2, 17, row.getWaterColumnInstruments() == null ? null : String.join(
            ";", row.getWaterColumnInstruments()
        ));
        worksheet.value(i + 2, 18, row.getADCPInstruments() == null ? null : String.join(
            ";", row.getADCPInstruments()
        ));
        worksheet.value(i + 2, 19, row.getComments());
      }
    }
  }
  
  private static ImportRow createImportRow(String suffix, int nInstrumentsPerType) {
    return new ImportRow() {
      @Override
      public String getShipName() {
        return String.format(
            "cruise-name-%s", suffix
        );
      }

      @Override
      public String getCruiseID() {
        return String.format(
            "cruise-id-%s", suffix
        );
      }

      @Override
      public String getLeg() {
        return String.format(
            String.format(
                "leg-%s", suffix
            )
        );
      }

      @Override
      public String getChiefScientist() {
        return String.format(
            "scientist-%s", suffix
        );
      }

      @Override
      public String getSponsorOrganization() {
        return String.format(
            "sponsor-%s", suffix
        );
      }

      @Override
      public String getFundingOrganization() {
        return String.format(
            "funder-%s", suffix
        );
      }

      @Override
      public String getDeparturePort() {
        return String.format(
            "departure-port-%s", suffix
        );
      }

      @Override
      public String getStartDate() {
        return String.format(
            "start-date-%s", suffix
        );
      }

      @Override
      public String getArrivalPort() {
        return String.format(
            "arrival-port-%s", suffix
        );
      }

      @Override
      public String getEndDate() {
        return String.format(
            "end-date-%s", suffix
        );
      }

      @Override
      public String getSeaArea() {
        return String.format(
            "sea-area-%s", suffix
        );
      }

      @Override
      public String getProjectName() {
        return String.format(
            "project-name-%s", suffix
        );
      }

      @Override
      public String getCruiseTitle() {
        return String.format(
            "cruise-title-%s", suffix
        );
      }

      @Override
      public String getCruisePurpose() {
        return String.format(
            "cruise-purpose-%s", suffix
        );
      }

      @Override
      public List<String> getCTDInstruments() {
        return IntStream.range(0, nInstrumentsPerType).boxed()
            .map(i -> String.format(
                "CTD-instrument-%s-%s", suffix, i
            )).toList();
      }

      @Override
      public List<String> getMBESInstruments() {
        return IntStream.range(0, nInstrumentsPerType).boxed()
            .map(i -> String.format(
                "MBES-instrument-%s-%s", suffix, i
            )).toList();
      }

      @Override
      public List<String> getSBESInstruments() {
        return IntStream.range(0, nInstrumentsPerType).boxed()
            .map(i -> String.format(
                "SBES-instrument-%s-%s", suffix, i
            )).toList();
      }

      @Override
      public List<String> getWaterColumnInstruments() {
        return IntStream.range(0, nInstrumentsPerType).boxed()
            .map(i -> String.format(
                "water-column-instrument-%s-%s", suffix, i
            )).toList();
      }

      @Override
      public List<String> getADCPInstruments() {
        return IntStream.range(0, nInstrumentsPerType).boxed()
            .map(i -> String.format(
                "ADCP-instrument-%s-%s", suffix, i
            )).collect(Collectors.toList());
      }

      @Override
      public String getComments() {
        return String.format(
            "comment-%s", suffix
        );
      }
    };
  }

  private ImportRow createEmptyRow() {
    return new ImportRow() {
      @Override
      public String getShipName() {
        return null;
      }

      @Override
      public String getCruiseID() {
        return null;
      }

      @Override
      public String getLeg() {
        return null;
      }

      @Override
      public String getChiefScientist() {
        return null;
      }

      @Override
      public String getSponsorOrganization() {
        return null;
      }

      @Override
      public String getFundingOrganization() {
        return null;
      }

      @Override
      public String getDeparturePort() {
        return null;
      }

      @Override
      public String getStartDate() {
        return null;
      }

      @Override
      public String getArrivalPort() {
        return null;
      }

      @Override
      public String getEndDate() {
        return null;
      }

      @Override
      public String getSeaArea() {
        return null;
      }

      @Override
      public String getProjectName() {
        return null;
      }

      @Override
      public String getCruiseTitle() {
        return null;
      }

      @Override
      public String getCruisePurpose() {
        return null;
      }

      @Override
      public List<String> getCTDInstruments() {
        return null;
      }

      @Override
      public List<String> getMBESInstruments() {
        return null;
      }

      @Override
      public List<String> getSBESInstruments() {
        return null;
      }

      @Override
      public List<String> getWaterColumnInstruments() {
        return null;
      }

      @Override
      public List<String> getADCPInstruments() {
        return null;
      }

      @Override
      public String getComments() {
        return null;
      }
    };
  }
  
  private static class CustomConstrainViolation implements ConstraintViolation<ImportModel> {

    private final String message;
    private final jakarta.validation.Path propertyPath;

    private CustomConstrainViolation(String message, String propertyPath) {
      this.message = message;
      this.propertyPath = PathImpl.createPathFromString(propertyPath);
    }

    @Override
    public String getMessage() {
      return message;
    }

    @Override
    public String getMessageTemplate() {
      return null;
    }

    @Override
    public ImportModel getRootBean() {
      return null;
    }

    @Override
    public Class<ImportModel> getRootBeanClass() {
      return null;
    }

    @Override
    public Object getLeafBean() {
      return null;
    }

    @Override
    public Object[] getExecutableParameters() {
      return new Object[0];
    }

    @Override
    public Object getExecutableReturnValue() {
      return null;
    }

    @Override
    public jakarta.validation.Path getPropertyPath() {
      return propertyPath;
    }

    @Override
    public Object getInvalidValue() {
      return null;
    }

    @Override
    public ConstraintDescriptor<?> getConstraintDescriptor() {
      return null;
    }

    @Override
    public <U> U unwrap(Class<U> type) {
      return null;
    }
  }
}