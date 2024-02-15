package edu.colorado.cires.cruisepack.app.ui.model;

import java.nio.file.Path;
import java.time.LocalDate;

public abstract class BaseDatasetInstrumentModel extends PropertyChangeModel {

  public static final String UPDATE_PUBLIC_RELEASE_DATE = "UPDATE_PUBLIC_RELEASE_DATE";
  public static final String UPDATE_DATA_PATH = "UPDATE_DATA_PATH";

  private LocalDate publicReleaseDate;
  private Path dataPath;


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

}
