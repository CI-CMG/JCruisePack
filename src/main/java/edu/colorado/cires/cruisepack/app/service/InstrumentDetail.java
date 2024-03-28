package edu.colorado.cires.cruisepack.app.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class InstrumentDetail {

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(InstrumentDetail src) {
    return new Builder(src);
  }

  private final InstrumentStatus status;
  private final String uuid;
  private final LocalDate releaseDate;
  private final String instrument;
  private final String shortName;
  private final Set<String> extensions;
  private final String dataPath;
  private final boolean flatten;
  private final String dirName;
  private final String bagName;
  private final String dataComment;
  private final List<AdditionalFiles> additionalFiles;
  private final Map<String, Object> additionalFields;
  //  private final Consumer<CustomInstrumentProcessingContext> customHandler;

  private InstrumentDetail(InstrumentStatus status, String uuid, LocalDate releaseDate, String instrument, String shortName, Set<String> extensions, String dataPath, boolean flatten,
      String dirName, String bagName, String dataComment, List<AdditionalFiles> additionalFiles, Map<String, Object> additionalFields
  ) {
    this.status = status;
    this.uuid = uuid;
    this.releaseDate = releaseDate;
    this.instrument = instrument;
    this.shortName = shortName;
    this.extensions = extensions;
    this.dataPath = dataPath;
    this.flatten = flatten;
    this.dirName = dirName;
    this.bagName = bagName;
    this.dataComment = dataComment;
    this.additionalFiles = additionalFiles;
    this.additionalFields = additionalFields;
  }

  public InstrumentStatus getStatus() {
    return status;
  }

  public String getUuid() {
    return uuid;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
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

  public String getDataPath() {
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

  public String getDataComment() {
    return dataComment;
  }

  public List<AdditionalFiles> getAdditionalFiles() {
    return additionalFiles;
  }

  public Map<String, Object> getAdditionalFields() {
    return additionalFields;
  }

  public static class Builder {

    private InstrumentStatus status;
    private String uuid;
    private LocalDate releaseDate;
    private String instrument;
    private String shortName;
    private Set<String> extensions = Collections.emptySet();
    private String dataPath;
    private boolean flatten;
    private String dirName;
    private String bagName;
    private String dataComment;
    private List<AdditionalFiles> additionalFiles = Collections.emptyList();
    private Map<String, Object> additionalFields = new HashMap<>();

    private Builder() {

    }

    private Builder(InstrumentDetail src) {
      status = src.status;
      uuid = src.uuid;
      releaseDate = src.releaseDate;
      instrument = src.instrument;
      shortName = src.shortName;
      extensions = src.extensions;
      dataPath = src.dataPath;
      flatten = src.flatten;
      dirName = src.dirName;
      bagName = src.bagName;
      dataComment = src.dataComment;
      additionalFiles = src.additionalFiles;
      additionalFields = src.additionalFields;
    }

    public Builder setStatus(InstrumentStatus status) {
      this.status = status;
      return this;
    }

    public Builder setUuid(String uuid) {
      this.uuid = uuid;
      return this;
    }

    public Builder setReleaseDate(LocalDate releaseDate) {
      this.releaseDate = releaseDate;
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

    public Builder setDataPath(String dataPath) {
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

    public Builder setDataComment(String dataComment) {
      this.dataComment = dataComment;
      return this;
    }

    public Builder setAdditionalFiles(List<AdditionalFiles> additionalFiles) {
      this.additionalFiles = additionalFiles;
      return this;
    }
    
    public Builder setAdditionalField(Entry<String, Object> entry) {
      this.additionalFields.put(entry.getKey(), entry.getValue());
      return this;
    }

    public InstrumentDetail build() {
      return new InstrumentDetail(status, uuid, releaseDate, instrument, shortName, extensions, dataPath, flatten,
          dirName, bagName, dataComment, additionalFiles, additionalFields);
    }
  }
}
