package edu.colorado.cires.cruisepack.prototype.components.model;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class InstrumentDetail {


  private final InstrumentStatus status;
  private final String instrument;
  private final String shortName;
  private final Set<String> extensions;
  private final Path dataPath;

  private String dirName;
  private String bagName;


  public InstrumentDetail(InstrumentStatus status, String instrument, String shortName, Set<String> extensions, Path dataPath) {
    this.status = status;
    this.instrument = instrument;
    this.shortName = shortName;
    this.extensions = Collections.unmodifiableSet(new HashSet<>(extensions));
    this.dataPath = dataPath;
  }

  public Path getDataPath() {
    return dataPath;
  }

  public Set<String> getExtensions() {
    return extensions;
  }

  public InstrumentStatus getStatus() {
    return status;
  }

  public String getInstrument() {
    return instrument;
  }

  public void setDirName(String dirName) {
    this.dirName = dirName;
  }

  public String getDirName() {
    return dirName;
  }

  public String getShortName() {
    return shortName;
  }

  public void setBagName(String bagName) {
    this.bagName = bagName;
  }

  public String getBagName() {
    return bagName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InstrumentDetail detail = (InstrumentDetail) o;
    return status == detail.status && Objects.equals(instrument, detail.instrument) && Objects.equals(shortName, detail.shortName)
        && Objects.equals(extensions, detail.extensions) && Objects.equals(dataPath, detail.dataPath) && Objects.equals(
        dirName, detail.dirName) && Objects.equals(bagName, detail.bagName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, instrument, shortName, extensions, dataPath, dirName, bagName);
  }

  @Override
  public String toString() {
    return "InstrumentDetail{" +
        "status=" + status +
        ", instrument='" + instrument + '\'' +
        ", shortName='" + shortName + '\'' +
        ", extensions=" + extensions +
        ", dataPath=" + dataPath +
        ", dirName='" + dirName + '\'' +
        ", bagName='" + bagName + '\'' +
        '}';
  }
}
