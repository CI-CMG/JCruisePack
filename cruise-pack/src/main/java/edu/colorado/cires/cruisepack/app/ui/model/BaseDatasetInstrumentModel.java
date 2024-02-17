package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.service.AdditionalFiles;
import edu.colorado.cires.cruisepack.app.service.InstrumentDetailPackageKey;
import edu.colorado.cires.cruisepack.app.service.InstrumentNameHolder;
import edu.colorado.cires.cruisepack.app.service.InstrumentStatus;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class BaseDatasetInstrumentModel extends PropertyChangeModel {

  public static final String UPDATE_PUBLIC_RELEASE_DATE = "UPDATE_PUBLIC_RELEASE_DATE";
  public static final String UPDATE_DATA_PATH = "UPDATE_DATA_PATH";

  private LocalDate publicReleaseDate;
  private Path dataPath;
  protected final String instrumentGroupShortCode;

  protected BaseDatasetInstrumentModel(String instrumentGroupShortCode) {
    this.instrumentGroupShortCode = instrumentGroupShortCode;
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
    return getSelectedInstrument().map(dd -> new InstrumentDetailPackageKey(instrumentGroupShortCode, dd.getId()));
  }

  public Optional<InstrumentNameHolder> getInstrumentNameHolder() {
    return getSelectedInstrument().map(dd -> new InstrumentNameHolder(dd.getValue(), dd.getId(), getSelectedInstrumentProcessingLevel(), dataPath,
        getAdditionalFiles()));
  }

  protected List<AdditionalFiles> getAdditionalFiles() {
    return Collections.emptyList();
  }
}
