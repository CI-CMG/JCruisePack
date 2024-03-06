package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.AdditionalFiles;
import edu.colorado.cires.cruisepack.app.service.InstrumentDetailPackageKey;
import edu.colorado.cires.cruisepack.app.service.InstrumentNameHolder;
import edu.colorado.cires.cruisepack.app.service.InstrumentStatus;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathExists;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsDirectory;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidInstrumentDropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.instrument.Instrument;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseDatasetInstrumentModel<T extends AdditionalFieldsModel> extends PropertyChangeModel {

  public static final String UPDATE_PUBLIC_RELEASE_DATE = "UPDATE_PUBLIC_RELEASE_DATE";
  public static final String UPDATE_DATA_PATH = "UPDATE_DATA_PATH";
  public static final String UPDATE_DATASET_PUBLIC_RELEASE_DATE_ERROR = "UPDATE_DATASET_PUBLIC_RELEASE_DATE_ERROR";
  public static final String UPDATE_DATASET_DATA_PATH_ERROR = "UPDATE_DATASET_DATA_PATH_ERROR";
  public static final String UPDATE_INSTRUMENT = "UPDATE_INSTRUMENT";
  public static final String UPDATE_PROCESSING_LEVEL = "UPDATE_PROCESSING";
  public static final String UPDATE_COMMENTS = "UPDATE_COMMENTS";
  public static final String UPDATE_COMMENTS_ERROR = "UPDATE_COMMENTS_ERROR";
  public static final String UPDATE_PROCESSING_LEVEL_ERROR = "UPDATE_PROCESSING_LEVEL_ERROR";
  public static final String UPDATE_INSTRUMENT_ERROR = "UPDATE_INSTRUMENT_ERROR";
  public static final String UPDATE_ANCILLARY_PATH = "UPDATE_ANCILLARY_PATH";
  public static final String UPDATE_ANCILLARY_PATH_ERROR = "UPDATE_ANCILLARY_PATH_ERROR";
  public static final String UPDATE_ANCILLARY_DETAILS = "UPDATE_ANCILLARY_DETAILS";
  public static final String UPDATE_ANCILLARY_DETAILS_ERROR = "UPDATE_ANCILLARY_DETAILS_ERROR";

  @NotNull(message = "must not be blank")
  private LocalDate publicReleaseDate;
  private String publicReleaseDateError = null;
  @NotNull(message = "must not be blank")
  @PathExists
  @PathIsDirectory
  private Path dataPath;
  private String dataPathError = null;
  @NotBlank
  protected final String instrumentGroupShortCode;
  @ValidInstrumentDropDownItem
  private DropDownItem instrument = InstrumentDatastore.UNSELECTED_INSTRUMENT;
  private String instrumentError = null;
  @NotBlank
  private String processingLevel = "Raw";
  private String processingLevelError = null;
  @NotBlank
  private String comments;
  private String commentsError = null;
  @NotNull(message = "must not be blank")
  private Path ancillaryPath;
  private String ancillaryPathError = null;
  
  @NotBlank
  private String ancillaryDetails;
  private String ancillaryDetailsError = null;
  
  private T additionalFieldsModel = null;

  protected BaseDatasetInstrumentModel(String instrumentGroupShortCode) {
    this.instrumentGroupShortCode = instrumentGroupShortCode;
  }

  public Optional<DropDownItem> getSelectedInstrument() {
    if (instrument == null || StringUtils.isBlank(instrument.getId())) {
      return Optional.empty();
    }
    return Optional.of(instrument);
  }

  protected InstrumentStatus getSelectedInstrumentProcessingLevel() {
    return InstrumentStatus.forValue(processingLevel);
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

  protected void setCommentsError(String message) {
    setIfChanged(UPDATE_COMMENTS_ERROR, message, () -> this.commentsError, (e) -> this.commentsError = e);
  }
  protected void setProcessingLevelError(String message) {
    setIfChanged(UPDATE_PROCESSING_LEVEL_ERROR, message, () -> this.processingLevelError, (e) -> this.processingLevelError = e);
  }

  protected void setInstrumentError(String message) {
    setIfChanged(UPDATE_INSTRUMENT_ERROR, message, () -> this.instrumentError, (e) -> this.instrumentError = e);
  }

  protected void setErrors(String propertyPath, String message) {
    if (propertyPath.endsWith("instrument")) {
      setInstrumentError(message);
    } else if (propertyPath.endsWith("processingLevel")) {
      setProcessingLevelError(message);
    } else if (propertyPath.endsWith("comments")) {
      setCommentsError(message);
    }
  }

  public void clearErrors() {
    setDataPathError(null);
    setPublicReleaseDateError(null);
    setInstrumentError(null);
    setProcessingLevelError(null);
    setCommentsError(null);
    setAncillaryPathError(null);
    setAncillaryDetailsError(null);
  }

  public String getInstrumentGroupShortCode() {
    return instrumentGroupShortCode;
  }

  public LocalDate getPublicReleaseDate() {
    return publicReleaseDate;
  }

  public void setPublicReleaseDate(LocalDate publicReleaseDate) {
    setIfChanged(UPDATE_PUBLIC_RELEASE_DATE, publicReleaseDate, () -> this.publicReleaseDate, (nv) -> this.publicReleaseDate = nv);
  }

  public Path getDataPath() {
    return dataPath;
  }

  public void setDataPath(Path dataPath) {
    setIfChanged(UPDATE_DATA_PATH, dataPath, () -> this.dataPath, (nv) -> this.dataPath = nv);
  }

  public Optional<InstrumentDetailPackageKey> getPackageKey() {
    return getSelectedInstrument().map(dd -> new InstrumentDetailPackageKey(instrumentGroupShortCode, dd.getValue()));
  }

  public Optional<InstrumentNameHolder> getInstrumentNameHolder(Instrument referenceInstrument) {
    Optional<DropDownItem> maybeInstrument = getSelectedInstrument();
    if (maybeInstrument.isEmpty()) {
      return Optional.empty();
    }
    DropDownItem selectedInstrument = maybeInstrument.get();
    if (selectedInstrument.getId().equals(referenceInstrument.getUuid()) && selectedInstrument.getValue().equals(referenceInstrument.getShortName())) {
      return Optional.of(
          new InstrumentNameHolder(
              referenceInstrument.getUuid(),
              referenceInstrument.getName(),
              referenceInstrument.getShortName(),
              getSelectedInstrumentProcessingLevel(),
              dataPath,
              getAdditionalFiles(),
              getPublicReleaseDate(),
              getComments(),
              additionalFieldsModel == null ? null : additionalFieldsModel.transform()
          )
      );
    }
    
    return Optional.empty();
  }

  protected List<AdditionalFiles> getAdditionalFiles() {
    return Collections.emptyList();
  }

  protected void setPublicReleaseDateError(String publicReleaseDateError) {
    setIfChanged(UPDATE_DATASET_PUBLIC_RELEASE_DATE_ERROR, publicReleaseDateError, () -> this.publicReleaseDateError, (e) -> this.publicReleaseDateError = e);
  }

  protected void setDataPathError(String dataPathError) {
    setIfChanged(UPDATE_DATASET_DATA_PATH_ERROR, dataPathError, () -> this.dataPathError, (e) -> this.dataPathError = e);
  }

  public Path getAncillaryPath() {
    return ancillaryPath;
  }

  public void setAncillaryPath(Path ancillaryPath) {
    setIfChanged(UPDATE_ANCILLARY_PATH, ancillaryPath, () -> this.ancillaryPath, (p) -> this.ancillaryPath = p);
  }

  public String getAncillaryPathError() {
    return ancillaryPathError;
  }

  public void setAncillaryPathError(String ancillaryPathError) {
    setIfChanged(UPDATE_ANCILLARY_PATH_ERROR, ancillaryPathError, () -> this.ancillaryPathError, (e) -> this.ancillaryPathError = e);
  }

  public String getAncillaryDetails() {
    return ancillaryDetails;
  }

  public void setAncillaryDetails(String ancillaryDetails) {
    setIfChanged(UPDATE_ANCILLARY_DETAILS, ancillaryDetails, () -> this.ancillaryDetails, (d) -> this.ancillaryDetails = d);
  }

  public String getAncillaryDetailsError() {
    return ancillaryDetailsError;
  }

  public void setAncillaryDetailsError(String ancillaryDetailsError) {
    setIfChanged(UPDATE_ANCILLARY_DETAILS_ERROR, ancillaryDetailsError, () -> this.ancillaryDetailsError, (e) -> this.ancillaryDetailsError = e);
  }

  public void setModelErrors(String propertyPath, String message) {
    if (propertyPath.endsWith("publicReleaseDate")) {
      setPublicReleaseDateError(message);
    } else if (propertyPath.endsWith("dataPath")) {
      setDataPathError(message);
    } else if (propertyPath.endsWith("ancillaryPath")) {
      setAncillaryPathError(message);
    } else if (propertyPath.endsWith("ancillaryDetails")) {
      setAncillaryDetailsError(message);
    }

    setErrors(propertyPath, message);
  }

  public T getAdditionalFieldsModel() {
    return additionalFieldsModel;
  }

  public void setAdditionalFieldsModel(T additionalFieldsModel) {
    this.additionalFieldsModel = additionalFieldsModel;
  }
}
