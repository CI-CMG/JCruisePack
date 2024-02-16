package edu.colorado.cires.cruisepack.app.service;

import java.nio.file.Path;

public class InstrumentNameHolder {

  private String dirName;
  private String bagName;
  private final String instrument;
  private final String shortName;
  private final InstrumentStatus status;
  private final Path dataPath;


  public InstrumentNameHolder(String instrument, String shortName, InstrumentStatus status, Path dataPath) {
    this.instrument = instrument;
    this.shortName = shortName;
    this.status = status;
    this.dataPath = dataPath;
  }

  public String getDirName() {
    return dirName;
  }

  public void setDirName(String dirName) {
    this.dirName = dirName;
  }

  public String getBagName() {
    return bagName;
  }

  public void setBagName(String bagName) {
    this.bagName = bagName;
  }

  public String getInstrument() {
    return instrument;
  }

  public String getShortName() {
    return shortName;
  }

  public InstrumentStatus getStatus() {
    return status;
  }

  public Path getDataPath() {
    return dataPath;
  }
}
