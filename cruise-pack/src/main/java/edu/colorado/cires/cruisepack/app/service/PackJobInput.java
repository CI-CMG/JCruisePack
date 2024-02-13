package edu.colorado.cires.cruisepack.app.service;

import jakarta.validation.constraints.NotBlank;
import java.nio.file.Path;

//TODO validate this object
public class PackJobInput {

  @NotBlank
//  @NoUnderscores TODO implement this
//  @NoBlanks TODO implement this
  private String cruiseId;

  private String segmentId;

  private Path destinationPath;

  public String getCruiseId() {
    return cruiseId;
  }

  public void setCruiseId(String cruiseId) {
    this.cruiseId = cruiseId;
  }

  public String getSegmentId() {
    return segmentId;
  }

  public void setSegmentId(String segmentId) {
    this.segmentId = segmentId;
  }

  public Path getDestinationPath() {
    return destinationPath;
  }

  public void setDestinationPath(Path destinationPath) {
    this.destinationPath = destinationPath;
  }
}
