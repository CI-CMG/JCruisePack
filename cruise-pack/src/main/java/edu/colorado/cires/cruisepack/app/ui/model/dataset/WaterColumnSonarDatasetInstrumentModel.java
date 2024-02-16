package edu.colorado.cires.cruisepack.app.ui.model.dataset;

import edu.colorado.cires.cruisepack.app.service.InstrumentStatus;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class WaterColumnSonarDatasetInstrumentModel extends BaseDatasetInstrumentModel {

  public static final String UPDATE_INSTRUMENT = "UPDATE_INSTRUMENT";
  public static final String UPDATE_PROCESSING_LEVEL = "UPDATE_PROCESSING";
  public static final String UPDATE_COMMENTS = "UPDATE_COMMENTS";
  public static final String UPDATE_CALIBRATION_STATE = "UPDATE_CALIBRATION_STATE";
  public static final String UPDATE_CALIBRATION_REPORT_PATH = "UPDATE_CALIBRATION_REPORT_PATH";
  public static final String UPDATE_CALIBRATION_DATA_PATH = "UPDATE_CALIBRATION_DATA_PATH";
  public static final String UPDATE_CALIBRATION_DATE = "UPDATE_CALIBRATION_DATE";

  // TODO move this to datasource
  private DropDownItem instrument;
  private String processingLevel = "Raw";
  private String comments;
  private DropDownItem calibrationState;
  @NotNull
  private Path calibrationReportPath;
  private String calibrationReportPathError;
  private Path calibrationDataPath;
  private LocalDate calibrationDate;

  public WaterColumnSonarDatasetInstrumentModel(String instrumentGroupShortCode) {
    super(instrumentGroupShortCode);
  }

  public DropDownItem getInstrument() {
    return instrument;
  }

  public void setInstrument(DropDownItem instrument) {
    setIfChanged(UPDATE_INSTRUMENT, instrument, () -> this.instrument, (nv) -> this.instrument = nv);
  }

  public String getProcessingLevel() {
    return processingLevel;
  }

  public void setProcessingLevel(String processingLevel) {
    setIfChanged(UPDATE_PROCESSING_LEVEL, processingLevel, () -> this.processingLevel, (nv) -> this.processingLevel = nv);
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    setIfChanged(UPDATE_COMMENTS, comments, () -> this.comments, (nv) -> this.comments = nv);
  }

  public DropDownItem getCalibrationState() {
    return calibrationState;
  }

  public void setCalibrationState(DropDownItem calibrationState) {
    setIfChanged(UPDATE_CALIBRATION_STATE, calibrationState, () -> this.calibrationState, (nv) -> this.calibrationState = nv);
  }

  public Path getCalibrationReportPath() {
    return calibrationReportPath;
  }

  public void setCalibrationReportPath(Path calibrationReportPath) {
    setIfChanged(UPDATE_CALIBRATION_REPORT_PATH, calibrationReportPath, () -> this.calibrationReportPath, (nv) -> this.calibrationReportPath = nv);
  }

  public Path getCalibrationDataPath() {
    return calibrationDataPath;
  }

  public void setCalibrationDataPath(Path calibrationDataPath) {
    setIfChanged(UPDATE_CALIBRATION_DATA_PATH, calibrationDataPath, () -> this.calibrationDataPath, (nv) -> this.calibrationDataPath = nv);
  }

  public LocalDate getCalibrationDate() {
    return calibrationDate;
  }

  public void setCalibrationDate(LocalDate calibrationDate) {
    setIfChanged(UPDATE_CALIBRATION_DATE, calibrationDate, () -> this.calibrationDate, (nv) -> this.calibrationDate = nv);
  }

  @Override
  public Optional<DropDownItem> getSelectedInstrument() {
    if (instrument == null || StringUtils.isBlank(instrument.getId())) {
      return Optional.empty();
    }
    return Optional.of(instrument);
  }

  @Override
  protected InstrumentStatus getSelectedInstrumentProcessingLevel() {
    return InstrumentStatus.forValue(processingLevel);
  }

}
