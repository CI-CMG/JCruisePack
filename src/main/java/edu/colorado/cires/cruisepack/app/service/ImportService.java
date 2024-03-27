package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.ImportModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.OptionPaneGenerator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.swing.JOptionPane;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportService {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(ImportService.class);
  private static final String TEMPLATE_FILE_NAME = "cruise_import.xlsx";
  
  private final Validator validator;
  private final CruiseDataDatastore datastore;
  private final Path templatePath;
  private final OptionPaneGenerator optionPaneGenerator;

  @Autowired
  public ImportService(Validator validator, CruiseDataDatastore datastore, ServiceProperties serviceProperties,
      OptionPaneGenerator optionPaneGenerator) {
    this.validator = validator;
    this.datastore = datastore;
    this.templatePath = getTemplatePath(serviceProperties);
    this.optionPaneGenerator = optionPaneGenerator;
  }
  
  private Path getTemplatePath(ServiceProperties serviceProperties) {
    Path workDir = Paths.get(serviceProperties.getWorkDir());
    Path configDir = workDir.resolve("config");
    return configDir.resolve(TEMPLATE_FILE_NAME);
  }
  
  public void saveTemplate(Path path) throws IOException {
    try (InputStream inputStream = new FileInputStream(templatePath.toFile()); OutputStream outputStream = new FileOutputStream(path.resolve(TEMPLATE_FILE_NAME).toFile())
    ) {
      IOUtils.copy(
          inputStream,
          outputStream
      );
    }
  }

  public boolean importCruises(ImportModel model) throws Exception {
    Optional<ImportModel> validatedModel = validateModel(model);
    if (validatedModel.isPresent()) {
      importFile(validatedModel.get());
      return true;
    }
    return false;
  }
  
  private void importFile(ImportModel model) throws Exception {
    try (InputStream inputStream = new FileInputStream(model.getImportPath().toFile()); ReadableWorkbook workbook = new ReadableWorkbook(inputStream)) {
      importSheet(workbook.getFirstSheet(), model);
    }
  }
  
  private Optional<ImportModel> validateModel(ImportModel model) {
    model.clearErrors();
    Set<ConstraintViolation<ImportModel>> constraintViolations = validator.validate(model);
    if (constraintViolations.isEmpty()) {
      return Optional.of(model);
    }
    model.setErrors(constraintViolations);
    return Optional.empty();
  }
  
  private void importSheet(Sheet sheet, ImportModel model) throws Exception {
    try (Stream<Row> stream = sheet.openStream()) {
      List<Row> rows = stream.toList();
      boolean overwriteAll = false;
      boolean skipAll = false;
      for (int i = 2; i < rows.size(); i++) { // skip template instructions and header
        ImportRow importRow = fromRow(rows.get(i));
        if (StringUtils.isBlank(importRow.getCruiseID())) {
          LOGGER.warn(String.format(
              "Detected blank cruise ID. Row %s will be skipped", i
          ));
        } else {
          boolean stepOverwrite = overwriteAll;
          
          ResponseStatus status = datastore.saveCruise(importRow, model.getDestinationPath(), model.getMetadataAuthor().getValue(), stepOverwrite, skipAll);
          if (!status.equals(ResponseStatus.CONFLICT)) {
            continue;
          }

          int choice = optionPaneGenerator.createOptionPane(
              String.format("Cruise with package id %s already exists", PackJobUtils.resolvePackageId(importRow.getCruiseID(), importRow.getLeg())),
              "Conflict",
              JOptionPane.WARNING_MESSAGE,
              "JOptionPane.warningIcon",
              new String[]{"Skip", "Skip All", "Overwrite", "Overwrite All"},
              "Skip"
          );
          
          if (choice == 1) {
            skipAll = true;
          } else if (choice == 2) {
            stepOverwrite = true;
            datastore.saveCruise(importRow, model.getDestinationPath(), model.getMetadataAuthor().getValue(), stepOverwrite, skipAll);
          } else if (choice == 3) {
            overwriteAll = true;
            stepOverwrite = overwriteAll;
            datastore.saveCruise(importRow, model.getDestinationPath(), model.getMetadataAuthor().getValue(), stepOverwrite, skipAll);
          }
        }
      }
    }
  }
  
  private List<String> getDelimitedList(Row row, int cellIndex) {
    return Arrays.stream(row.getCellText(cellIndex).split(";"))
        .toList();
  }
  
  private ImportRow fromRow(Row row) {
    return new ImportRow() {
      @Override
      public String getShipName() {
        return row.getCellText(0);
      }

      @Override
      public String getCruiseID() {
        return row.getCellText(1);
      }

      @Override
      public String getLeg() {
        return row.getCellText(2);
      }

      @Override
      public String getChiefScientist() {
        return row.getCellText(3);
      }

      @Override
      public String getSponsorOrganization() {
        return row.getCellText(4);
      }

      @Override
      public String getFundingOrganization() {
        return row.getCellText(5);
      }

      @Override
      public String getDeparturePort() {
        return row.getCellText(6);
      }

      @Override
      public String getStartDate() {
        return row.getCellText(7);
      }

      @Override
      public String getArrivalPort() {
        return row.getCellText(8);
      }

      @Override
      public String getEndDate() {
        return row.getCellText(9);
      }

      @Override
      public String getSeaArea() {
        return row.getCellText(10);
      }

      @Override
      public String getProjectName() {
        return row.getCellText(11);
      }

      @Override
      public String getCruiseTitle() {
        return row.getCellText(12);
      }

      @Override
      public String getCruisePurpose() {
        return row.getCellText(13);
      }

      @Override
      public List<String> getCTDInstruments() {
        return getDelimitedList(row, 14);
      }

      @Override
      public List<String> getMBESInstruments() {
        return getDelimitedList(row, 15);
      }

      @Override
      public List<String> getSBESInstruments() {
        return getDelimitedList(row, 16);
      }

      @Override
      public List<String> getWaterColumnInstruments() {
        return getDelimitedList(row, 17);
      }

      @Override
      public List<String> getADCPInstruments() {
        return getDelimitedList(row, 18);
      }

      @Override
      public String getComments() {
        return row.getCellText(19);
      }
    };
  }

}
