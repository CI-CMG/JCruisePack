package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder = PackageInstrument.Builder.class)
public class PackageInstrument {

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(PackageInstrument src) {
    return new Builder(src);
  }

  @JsonUnwrapped
  private final Instrument instrument;
  private final String typeName;
  private final boolean flatten;
  private final List<String> extensions;
  private final String dirName;
  private final String bagName;

  private PackageInstrument(Instrument instrument, String typeName, boolean flatten, List<String> extensions, String dirName, String bagName) {
    this.instrument = instrument;
    this.typeName = typeName;
    this.flatten = flatten;
    this.extensions = extensions;
    this.dirName = dirName;
    this.bagName = bagName;
  }

  public Instrument getInstrument() {
    return instrument;
  }

  public String getTypeName() {
    return typeName;
  }

  public boolean isFlatten() {
    return flatten;
  }

  public List<String> getExtensions() {
    return extensions;
  }

  public String getDirName() {
    return dirName;
  }

  public String getBagName() {
    return bagName;
  }

  public static class Builder {
    private Instrument instrument;
    private String typeName;
    private boolean flatten;
    private List<String> extensions = Collections.emptyList();
    private String dirName;
    private String bagName;

    private Builder() {

    }

    private Builder(PackageInstrument src) {
      instrument = src.instrument;
      typeName = src.typeName;
      flatten = src.flatten;
      extensions = src.extensions;
      dirName = src.dirName;
      bagName = src.bagName;
    }

    public Builder withInstrument(Instrument instrument) {
      this.instrument = instrument;
      return this;
    }

    public Builder withTypeName(String typeName) {
      this.typeName = typeName;
      return this;
    }

    public Builder withFlatten(boolean flatten) {
      this.flatten = flatten;
      return this;
    }

    public Builder withExtensions(List<String> extensions) {
      if (extensions == null) {
        extensions = new ArrayList<>(0);
      }
      this.extensions = Collections.unmodifiableList(new ArrayList<>(extensions));
      return this;
    }

    public Builder withDirName(String dirName) {
      this.dirName = dirName;
      return this;
    }

    public Builder withBagName(String bagName) {
      this.bagName = bagName;
      return this;
    }

    public PackageInstrument build() {
      return new PackageInstrument(instrument, typeName, flatten, extensions, dirName, bagName);
    }
  }
}
