package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.service.AdditionalFiles;
import edu.colorado.cires.cruisepack.app.service.InstrumentDetailPackageKey;
import edu.colorado.cires.cruisepack.app.service.InstrumentNameHolder;
import edu.colorado.cires.cruisepack.app.service.InstrumentStatus;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathExists;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsDirectory;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.instrument.Instrument;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public abstract class BaseDatasetInstrumentModel extends PropertyChangeModel {

  public static final String UPDATE_PUBLIC_RELEASE_DATE = "UPDATE_PUBLIC_RELEASE_DATE";
  public static final String UPDATE_DATA_PATH = "UPDATE_DATA_PATH";
  public static final String UPDATE_DATASET_PUBLIC_RELEASE_DATE_ERROR = "UPDATE_DATASET_PUBLIC_RELEASE_DATE_ERROR";
  public static final String UPDATE_DATASET_DATA_PATH_ERROR = "UPDATE_DATASET_DATA_PATH_ERROR";

  @NotNull(message = "must not be blank")
  private LocalDate publicReleaseDate;
  private String publicReleaseDateError = null;;
  @NotNull(message = "must not be blank")
  @PathExists
  @PathIsDirectory
  private Path dataPath;
  private String dataPathError = null;
  @NotBlank
  protected final String instrumentGroupShortCode;

  protected BaseDatasetInstrumentModel(String instrumentGroupShortCode) {
    this.instrumentGroupShortCode = instrumentGroupShortCode;
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

  protected abstract Optional<DropDownItem> getSelectedInstrument();
  protected abstract InstrumentStatus getSelectedInstrumentProcessingLevel();

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
              getAdditionalFields()
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

  public void setModelErrors(String propertyPath, String message) {
    if (propertyPath.endsWith("publicReleaseDate")) {
      setPublicReleaseDateError(message);
    } else if (propertyPath.endsWith("dataPath")) {
      setDataPathError(message);
    }

    setErrors(propertyPath, message);
  }
  
  public abstract void clearErrors();

  protected abstract void setErrors(String propertyPath, String message);
  public abstract String getComments();
  
  protected abstract HashMap<String, Object> getAdditionalFields();
  
}
