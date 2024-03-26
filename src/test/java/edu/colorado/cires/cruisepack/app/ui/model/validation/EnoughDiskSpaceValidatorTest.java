package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils;
import edu.colorado.cires.cruisepack.app.service.InstrumentDetail;
import edu.colorado.cires.cruisepack.app.service.InstrumentDetailPackageKey;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.ui.model.validation.EnoughDiskSpace.EnoughDiskSpaceValidator;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class EnoughDiskSpaceValidatorTest extends ConstraintValidatorTest<PackJob, EnoughDiskSpaceValidator> {
  
  private static final int USABLE_SPACE = 10;
  private static final Path PACKAGE_DIRECTORY = Paths.get("target").resolve("target-directory");
  private static final Path DOCUMENTS_DIRECTORY = Paths.get("target").resolve("documents-directory");
  private static final Path OMICS_TRACKING_SHEET_PATH = Paths.get("target").resolve("omics-tracking-sheet.txt");
  private static final Path DATA_PATH = Paths.get("target").resolve("data");
  private static final Path ANCILLARY_PATH = Paths.get("target").resolve("ancillary");
  private static final Path CALIBRATION_REPORT_PATH = Paths.get("target/calibration-report-path.pdf");
  private static final String CALIBRATION_DATA_PATH = "target/calibration-data-path";
  private static final PackJob PACK_JOB = PackJob.builder()
      .setCruiseId("CRUISE-ID")
        .setPackageDirectory(PACKAGE_DIRECTORY)
        .setDocumentsPath(DOCUMENTS_DIRECTORY)
        .setOmicsSampleTrackingSheetPath(OMICS_TRACKING_SHEET_PATH)
        .setInstruments(Map.of(
          new InstrumentDetailPackageKey(InstrumentGroupName.WATER_COLUMN.getShortName(), "test-water-column-instrument"),
          Collections.singletonList(InstrumentDetail.builder()
            .setShortName(InstrumentGroupName.WATER_COLUMN.getShortName())
            .setDataPath(DATA_PATH.toString())
            .setAncillaryDataPath(ANCILLARY_PATH.toString())
            .setAdditionalField(Map.entry("calibration_report_path", CALIBRATION_REPORT_PATH))
            .setAdditionalField(Map.entry("calibration_data_path", CALIBRATION_DATA_PATH))
          .build())
        ))
      .build();

  @Override
  protected EnoughDiskSpaceValidator createValidator() {
    return new EnoughDiskSpaceValidator();
  }
  
  @Test
  void testValidatorPass() {
    try (MockedStatic<CruisePackFileUtils> cruisePackFileUtils = mockStatic(CruisePackFileUtils.class)) {
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getUsableSpace(PACKAGE_DIRECTORY)).thenReturn(
          BigInteger.valueOf(USABLE_SPACE)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DOCUMENTS_DIRECTORY)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(OMICS_TRACKING_SHEET_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DATA_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(ANCILLARY_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(CALIBRATION_REPORT_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(Paths.get(CALIBRATION_DATA_PATH))).thenReturn(
          BigInteger.valueOf(1)
      );
      
      assertTrue(validator.isValid(PACK_JOB, VALIDATOR_CONTEXT));
    }
  }

  @Test
  void testValidatorDocsFail() {
    try (MockedStatic<CruisePackFileUtils> cruisePackFileUtils = mockStatic(CruisePackFileUtils.class)) {
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getUsableSpace(PACKAGE_DIRECTORY)).thenReturn(
          BigInteger.valueOf(USABLE_SPACE)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DOCUMENTS_DIRECTORY)).thenReturn(
          BigInteger.valueOf(USABLE_SPACE)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(OMICS_TRACKING_SHEET_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DATA_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(ANCILLARY_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(CALIBRATION_REPORT_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(Paths.get(CALIBRATION_DATA_PATH))).thenReturn(
          BigInteger.valueOf(1)
      );

      assertFalse(validator.isValid(PACK_JOB, VALIDATOR_CONTEXT));
    }
  }

  @Test
  void testValidatorOmicsTrackingSheetFail() {
    try (MockedStatic<CruisePackFileUtils> cruisePackFileUtils = mockStatic(CruisePackFileUtils.class)) {
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getUsableSpace(PACKAGE_DIRECTORY)).thenReturn(
          BigInteger.valueOf(USABLE_SPACE)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DOCUMENTS_DIRECTORY)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(OMICS_TRACKING_SHEET_PATH)).thenReturn(
          BigInteger.valueOf(USABLE_SPACE)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DATA_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(ANCILLARY_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(CALIBRATION_REPORT_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(Paths.get(CALIBRATION_DATA_PATH))).thenReturn(
          BigInteger.valueOf(1)
      );

      assertFalse(validator.isValid(PACK_JOB, VALIDATOR_CONTEXT));
    }
  }

  @Test
  void testValidatorDataPathFail() {
    try (MockedStatic<CruisePackFileUtils> cruisePackFileUtils = mockStatic(CruisePackFileUtils.class)) {
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getUsableSpace(PACKAGE_DIRECTORY)).thenReturn(
          BigInteger.valueOf(USABLE_SPACE)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DOCUMENTS_DIRECTORY)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(OMICS_TRACKING_SHEET_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DATA_PATH)).thenReturn(
          BigInteger.valueOf(USABLE_SPACE)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(ANCILLARY_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(CALIBRATION_REPORT_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(Paths.get(CALIBRATION_DATA_PATH))).thenReturn(
          BigInteger.valueOf(1)
      );

      assertFalse(validator.isValid(PACK_JOB, VALIDATOR_CONTEXT));
    }
  }

  @Test
  void testValidatorAncillaryPathFail() {
    try (MockedStatic<CruisePackFileUtils> cruisePackFileUtils = mockStatic(CruisePackFileUtils.class)) {
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getUsableSpace(PACKAGE_DIRECTORY)).thenReturn(
          BigInteger.valueOf(USABLE_SPACE)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DOCUMENTS_DIRECTORY)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(OMICS_TRACKING_SHEET_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DATA_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(ANCILLARY_PATH)).thenReturn(
          BigInteger.valueOf(USABLE_SPACE)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(CALIBRATION_REPORT_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(Paths.get(CALIBRATION_DATA_PATH))).thenReturn(
          BigInteger.valueOf(1)
      );

      assertFalse(validator.isValid(PACK_JOB, VALIDATOR_CONTEXT));
    }
  }

  @Test
  void testValidatorCalibrationReportPathFail() {
    try (MockedStatic<CruisePackFileUtils> cruisePackFileUtils = mockStatic(CruisePackFileUtils.class)) {
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getUsableSpace(PACKAGE_DIRECTORY)).thenReturn(
          BigInteger.valueOf(USABLE_SPACE)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DOCUMENTS_DIRECTORY)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(OMICS_TRACKING_SHEET_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DATA_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(ANCILLARY_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(CALIBRATION_REPORT_PATH)).thenReturn(
          BigInteger.valueOf(USABLE_SPACE)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(Paths.get(CALIBRATION_DATA_PATH))).thenReturn(
          BigInteger.valueOf(1)
      );

      assertFalse(validator.isValid(PACK_JOB, VALIDATOR_CONTEXT));
    }
  }

  @Test
  void testValidatorCalibrationDataPathFail() {
    try (MockedStatic<CruisePackFileUtils> cruisePackFileUtils = mockStatic(CruisePackFileUtils.class)) {
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getUsableSpace(PACKAGE_DIRECTORY)).thenReturn(
          BigInteger.valueOf(USABLE_SPACE)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DOCUMENTS_DIRECTORY)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(OMICS_TRACKING_SHEET_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DATA_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(ANCILLARY_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(CALIBRATION_REPORT_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(Paths.get(CALIBRATION_DATA_PATH))).thenReturn(
          BigInteger.valueOf(USABLE_SPACE)
      );

      assertFalse(validator.isValid(PACK_JOB, VALIDATOR_CONTEXT));
    }
  }
  
  @Test
  void testValidatorPathNotDefined() {
    PackJob packJob = PackJob.builder(PACK_JOB)
        .setDocumentsPath(null)
        .build();
    try (MockedStatic<CruisePackFileUtils> cruisePackFileUtils = mockStatic(CruisePackFileUtils.class)) {
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getUsableSpace(PACKAGE_DIRECTORY)).thenReturn(
          BigInteger.valueOf(USABLE_SPACE)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DOCUMENTS_DIRECTORY)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(OMICS_TRACKING_SHEET_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(DATA_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(ANCILLARY_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(CALIBRATION_REPORT_PATH)).thenReturn(
          BigInteger.valueOf(1)
      );
      cruisePackFileUtils.when(() -> CruisePackFileUtils.getSize(Paths.get(CALIBRATION_DATA_PATH))).thenReturn(
          BigInteger.valueOf(1)
      );

      assertTrue(validator.isValid(packJob, VALIDATOR_CONTEXT));
    }
  }
}
