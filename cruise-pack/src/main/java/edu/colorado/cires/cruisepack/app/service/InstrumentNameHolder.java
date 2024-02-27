package edu.colorado.cires.cruisepack.app.service;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class InstrumentNameHolder {

  private final String uuid;
  private String dirName;
  private String bagName;
  private final String instrument;
  private final String shortName;
  private final InstrumentStatus status;
  private final Path dataPath;
  private final LocalDate releaseDate;
  private final String dataComment;
  private final List<AdditionalFiles> additionalFiles;


  public InstrumentNameHolder(String uuid, String instrument, String shortName, InstrumentStatus status, Path dataPath, List<AdditionalFiles> additionalFiles, LocalDate releaseDate, String dataComment) {
    this.uuid = uuid;
    this.instrument = instrument;
    this.shortName = shortName;
    this.status = status;
    this.dataPath = dataPath;
    this.additionalFiles = additionalFiles;
    this.releaseDate = releaseDate;
    this.dataComment = dataComment;
  }

  public String getUuid() {
    return uuid;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
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

  public List<AdditionalFiles> getAdditionalFiles() {
    return additionalFiles;
  }

  public String getDataComment() {
    return dataComment;
  }
}
