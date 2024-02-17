package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder = Instrument.Builder.class)
public class Instrument {

  public Builder builder() {
    return new Builder();
  }

  public Builder builder(Instrument src) {
    return new Builder(src);
  }

  private final String uuid;
  private final String type;
  private final String instrument;
  private final String shortName;
  private final String releaseDate;
  private final String dataPath;
  private final String status;
  private final String dataComment;
  private final List<AdditionalData> additionalData;
  private final Map<String, Object> otherFields;

  private Instrument(String uuid, String type, String instrument, String shortName, String releaseDate, String dataPath, String status,
      String dataComment, List<AdditionalData> additionalData, Map<String, Object> otherFields) {
    this.uuid = uuid;
    this.type = type;
    this.instrument = instrument;
    this.shortName = shortName;
    this.releaseDate = releaseDate;
    this.dataPath = dataPath;
    this.status = status;
    this.dataComment = dataComment;
    this.additionalData = additionalData;
    this.otherFields = otherFields;
  }

  @JsonAnyGetter
  public Map<String, Object> getOtherFields() {
    return otherFields;
  }

  public String getUuid() {
    return uuid;
  }

  public String getType() {
    return type;
  }

  public String getInstrument() {
    return instrument;
  }

  public String getShortName() {
    return shortName;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public String getDataPath() {
    return dataPath;
  }

  public String getStatus() {
    return status;
  }

  public String getDataComment() {
    return dataComment;
  }

  public List<AdditionalData> getAdditionalData() {
    return additionalData;
  }

  public static class Builder {

    private String uuid;
    private String type;
    private String instrument;
    private String shortName;
    private String releaseDate;
    private String dataPath;
    private String status;
    private String dataComment;
    private List<AdditionalData> additionalData = Collections.emptyList();
    private Map<String, Object> otherFields = new LinkedHashMap<>();

    private Builder() {

    }

    private Builder(Instrument src) {
      uuid = src.uuid;
      type = src.type;
      instrument = src.instrument;
      shortName = src.shortName;
      releaseDate = src.releaseDate;
      dataPath = src.dataPath;
      status = src.status;
      dataComment = src.dataComment;
      additionalData = src.additionalData;
      otherFields = src.otherFields;
    }

    public Builder withUuid(String uuid) {
      this.uuid = uuid;
      return this;
    }

    public Builder withType(String type) {
      this.type = type;
      return this;
    }

    public Builder withInstrument(String instrument) {
      this.instrument = instrument;
      return this;
    }

    public Builder withShortName(String shortName) {
      this.shortName = shortName;
      return this;
    }

    public Builder withReleaseDate(String releaseDate) {
      this.releaseDate = releaseDate;
      return this;
    }

    public Builder withDataPath(String dataPath) {
      this.dataPath = dataPath;
      return this;
    }

    public Builder withStatus(String status) {
      this.status = status;
      return this;
    }

    public Builder withDataComment(String dataComment) {
      this.dataComment = dataComment;
      return this;
    }

    public Builder withAdditionalData(List<AdditionalData> additionalData) {
      if (additionalData == null) {
        additionalData = Collections.emptyList();
      }
      this.additionalData = Collections.unmodifiableList(new ArrayList<>(additionalData));
      return this;
    }

    @JsonAnySetter
    private Builder withOtherField(String name, Object value) {
      this.otherFields.put(name, value);
      return this;
    }

    public Instrument build() {
      return new Instrument(uuid, type, instrument, shortName, releaseDate, dataPath, status,
          dataComment, additionalData, Collections.unmodifiableMap(otherFields));
    }
  }


}
