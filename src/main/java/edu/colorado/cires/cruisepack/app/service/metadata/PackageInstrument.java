package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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


  private PackageInstrument(Instrument instrument, String typeName, boolean flatten, List<String> extensions) {
    this.instrument = instrument;
    this.typeName = typeName;
    this.flatten = flatten;
    this.extensions = extensions;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PackageInstrument that = (PackageInstrument) o;
    return flatten == that.flatten && Objects.equals(instrument, that.instrument) && Objects.equals(typeName, that.typeName)
        && Objects.equals(extensions, that.extensions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instrument, typeName, flatten, extensions);
  }

  @Override
  public String toString() {
    return "PackageInstrument{" +
        "instrument=" + instrument +
        ", typeName='" + typeName + '\'' +
        ", flatten=" + flatten +
        ", extensions=" + extensions +
        '}';
  }

  public static class Builder {
    private Instrument instrument;
    private String typeName;
    private boolean flatten;
    private List<String> extensions = Collections.emptyList();

    private Builder() {

    }

    private Builder(PackageInstrument src) {
      instrument = src.instrument;
      typeName = src.typeName;
      flatten = src.flatten;
      extensions = src.extensions;
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


    public PackageInstrument build() {
      return new PackageInstrument(instrument, typeName, flatten, extensions);
    }
  }
}
