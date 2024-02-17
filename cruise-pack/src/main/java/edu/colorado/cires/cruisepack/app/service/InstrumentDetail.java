package edu.colorado.cires.cruisepack.app.service;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class InstrumentDetail {

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(InstrumentDetail src) {
    return new Builder(src);
  }

  private final InstrumentStatus status;
  private final String instrument;
  private final String shortName;
  private final Set<String> extensions;
  private final Path dataPath;
  private final boolean flatten;
  private final String dirName;
  private final String bagName;
  private final List<AdditionalFiles> additionalFiles;
  //  private final Consumer<CustomInstrumentProcessingContext> customHandler;

  private InstrumentDetail(InstrumentStatus status, String instrument, String shortName, Set<String> extensions, Path dataPath, boolean flatten,
      String dirName, String bagName, List<AdditionalFiles> additionalFiles) {
    this.status = status;
    this.instrument = instrument;
    this.shortName = shortName;
    this.extensions = extensions;
    this.dataPath = dataPath;
    this.flatten = flatten;
    this.dirName = dirName;
    this.bagName = bagName;
    this.additionalFiles = additionalFiles;
  }

  public InstrumentStatus getStatus() {
    return status;
  }

  public String getInstrument() {
    return instrument;
  }

  public String getShortName() {
    return shortName;
  }

  public Set<String> getExtensions() {
    return extensions;
  }

  public Path getDataPath() {
    return dataPath;
  }

  public boolean isFlatten() {
    return flatten;
  }

  public String getDirName() {
    return dirName;
  }

  public String getBagName() {
    return bagName;
  }

  public List<AdditionalFiles> getAdditionalFiles() {
    return additionalFiles;
  }

  public static class Builder {

    private InstrumentStatus status;
    private String instrument;
    private String shortName;
    private Set<String> extensions = Collections.emptySet();
    private Path dataPath;
    private boolean flatten;
    private String dirName;
    private String bagName;
    private List<AdditionalFiles> additionalFiles = Collections.emptyList();

    private Builder() {

    }

    private Builder(InstrumentDetail src) {
      status = src.status;
      instrument = src.instrument;
      shortName = src.shortName;
      extensions = src.extensions;
      dataPath = src.dataPath;
      flatten = src.flatten;
      dirName = src.dirName;
      bagName = src.bagName;
      additionalFiles = src.additionalFiles;
    }

    public Builder setStatus(InstrumentStatus status) {
      this.status = status;
      return this;
    }

    public Builder setInstrument(String instrument) {
      this.instrument = instrument;
      return this;
    }

    public Builder setShortName(String shortName) {
      this.shortName = shortName;
      return this;
    }

    public Builder setExtensions(Set<String> extensions) {
      if (extensions == null) {
        extensions = new HashSet<>();
      }
      this.extensions = Collections.unmodifiableSet(new LinkedHashSet<>(extensions));
      return this;
    }

    public Builder setDataPath(Path dataPath) {
      this.dataPath = dataPath;
      return this;
    }

    public Builder setFlatten(boolean flatten) {
      this.flatten = flatten;
      return this;
    }

    public Builder setDirName(String dirName) {
      this.dirName = dirName;
      return this;
    }

    public Builder setBagName(String bagName) {
      this.bagName = bagName;
      return this;
    }

    public Builder setAdditionalFiles(List<AdditionalFiles> additionalFiles) {
      this.additionalFiles = additionalFiles;
      return this;
    }

    public InstrumentDetail build() {
      return new InstrumentDetail(status, instrument, shortName, extensions, dataPath, flatten,
          dirName, bagName, additionalFiles);
    }
  }
}
