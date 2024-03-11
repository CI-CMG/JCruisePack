package edu.colorado.cires.cruisepack.app.service;

import java.nio.file.Path;

public class AdditionalFiles {

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(AdditionalFiles src) {
    return new Builder(src);
  }

  private final Path relativeDestinationDirectory;
  private final Path sourceFileOrDirectory;

  private AdditionalFiles(Path relativeDestinationDirectory, Path sourceFileOrDirectory) {
    this.relativeDestinationDirectory = relativeDestinationDirectory;
    this.sourceFileOrDirectory = sourceFileOrDirectory;
  }

  public Path getRelativeDestinationDirectory() {
    return relativeDestinationDirectory;
  }

  public Path getSourceFileOrDirectory() {
    return sourceFileOrDirectory;
  }

  public static class Builder {

    private Path relativeDestinationDirectory;
    private Path sourceFileOrDirectory;

    private Builder() {

    }

    private Builder(AdditionalFiles src) {
      relativeDestinationDirectory =  src.relativeDestinationDirectory;
      sourceFileOrDirectory = src.sourceFileOrDirectory;
    }

    public Builder setRelativeDestinationDirectory(Path relativeDestinationDirectory) {
      this.relativeDestinationDirectory = relativeDestinationDirectory;
      return this;
    }

    public Builder setSourceFileOrDirectory(Path sourceFileOrDirectory) {
      this.sourceFileOrDirectory = sourceFileOrDirectory;
      return this;
    }

    public AdditionalFiles build() {
      return new AdditionalFiles(relativeDestinationDirectory, sourceFileOrDirectory);
    }
  }
}
