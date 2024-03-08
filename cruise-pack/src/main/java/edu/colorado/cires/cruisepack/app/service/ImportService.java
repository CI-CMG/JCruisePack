package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.ImportModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportService {
  
  private final Validator validator;
  private final CruiseDataDatastore datastore;
  private final MetadataService metadataService;

  @Autowired
  public ImportService(Validator validator, CruiseDataDatastore datastore, MetadataService metadataService) {
    this.validator = validator;
    this.datastore = datastore;
    this.metadataService = metadataService;
  }

  public void importCruises(ImportModel model) {
    validateModel(model)
        .ifPresent(this::importFile);
  }
  
  private void importFile(ImportModel model) {
    try (
        InputStream inputStream = new FileInputStream(model.getImportPath().toFile());
        ReadableWorkbook workbook = new ReadableWorkbook(inputStream)
    ) {
      importSheet(workbook.getFirstSheet(), model);
    } catch (IOException e) {
      throw new IllegalStateException("Could not read spreadsheet", e);
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
  
  private void importSheet(Sheet sheet, ImportModel model) {
    try (Stream<Row> stream = sheet.openStream()) {
      stream.map(this::fromRow)
          .map(r -> metadataService.createMetadata(r, model))
          .forEach(datastore::saveCruise);
    } catch (IOException e) {
      throw new IllegalStateException("Could not read sheet", e);
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
