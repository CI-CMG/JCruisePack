package edu.colorado.cires.cruisepack.app.service;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class PackJob {

  private Instant startTime;
  private String packageId;
  private String masterBagName;
  private Path bagPath;
  private Map<String, List<InstrumentDetail>> instruments;
  private Path docsDir;
  private Path omicsFile;
  private Path cruisePackDataDir;
  private CruiseMetadata cruiseMetadata;


  public Instant getStartTime() {
    return startTime;
  }

  public void setStartTime(Instant startTime) {
    this.startTime = startTime;
  }

  public String getPackageId() {
    return packageId;
  }

  public void setPackageId(String packageId) {
    this.packageId = packageId;
  }

  public String getMasterBagName() {
    return masterBagName;
  }

  public void setMasterBagName(String masterBagName) {
    this.masterBagName = masterBagName;
  }

  public Path getBagPath() {
    return bagPath;
  }

  public void setBagPath(Path bagPath) {
    this.bagPath = bagPath;
  }

  public Map<String, List<InstrumentDetail>> getInstruments() {
    return instruments;
  }

  public void setInstruments(Map<String, List<InstrumentDetail>> instruments) {
    this.instruments = instruments;
  }

  public Path getDocsDir() {
    return docsDir;
  }

  public void setDocsDir(Path docsDir) {
    this.docsDir = docsDir;
  }

  public Path getOmicsFile() {
    return omicsFile;
  }

  public void setOmicsFile(Path omicsFile) {
    this.omicsFile = omicsFile;
  }

  public Path getCruisePackDataDir() {
    return cruisePackDataDir;
  }

  public void setCruisePackDataDir(Path cruisePackDataDir) {
    this.cruisePackDataDir = cruisePackDataDir;
  }

  public CruiseMetadata getCruiseMetadata() {
    return cruiseMetadata;
  }

  public void setCruiseMetadata(CruiseMetadata cruiseMetadata) {
    this.cruiseMetadata = cruiseMetadata;
  }
}
