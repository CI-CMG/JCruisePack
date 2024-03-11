package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder = InstrumentData.Builder.class)
public class InstrumentData extends Instrument {
  public static Builder builder() {
    return new Builder();
  }
  
  public static Builder builder(Instrument instrument) {
    return new Builder(instrument);
  }
  
  private final Path dataPath;
  private final Path ancillaryDataPath;

  protected InstrumentData(String uuid, String type, String instrument, String shortName, String releaseDate, String status, String dataComment,
      String dirName, String bagName, Map<String, Object> otherFields, String ancillaryDataDetails, Path dataPath, Path ancillaryDataPath) {
    super(uuid, type, instrument, shortName, releaseDate, status, dataComment, dirName, bagName, otherFields, ancillaryDataDetails);
    this.dataPath = dataPath;
    this.ancillaryDataPath = ancillaryDataPath;
  }

  public Path getDataPath() {
    return dataPath;
  }

  public Path getAncillaryDataPath() {
    return ancillaryDataPath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof InstrumentData that)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    return Objects.equals(dataPath, that.dataPath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), dataPath);
  }
  
  public static class Builder {
    private String uuid;
    private String type;
    private String instrument;
    private String shortName;
    private String releaseDate;
    private String status;
    private String dataComment;
    private String dirName;
    private String bagName;
    private Map<String, Object> otherFields = new LinkedHashMap<>();
    private String ancillaryDataDetails;
    private Path dataPath;
    private Path ancillaryDataPath;

    private Builder() {

    }

    private Builder(Instrument src) {
      uuid = src.getUuid();
      type = src.getType();
      instrument = src.getInstrument();
      shortName = src.getShortName();
      releaseDate = src.getReleaseDate();
      status = src.getStatus();
      dataComment = src.getDataComment();
      dirName = src.getDirName();
      bagName = src.getBagName();
      otherFields = src.getOtherFields();
      ancillaryDataDetails = src.getAncillaryDataDetails();
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

    public Builder withStatus(String status) {
      this.status = status;
      return this;
    }

    public Builder withDataComment(String dataComment) {
      this.dataComment = dataComment;
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

    @JsonAnySetter
    public Builder withOtherField(String name, Object value) {
      this.otherFields.put(name, value);
      return this;
    }

    public Builder withAncillaryDataDetails(String ancillaryDataDetails) {
      this.ancillaryDataDetails = ancillaryDataDetails;
      return this;
    }
    
    public Builder withDataPath(Path dataPath) {
      this.dataPath = dataPath;
      return this;
    }
    
    public Builder withAncillaryDataPath(Path ancillaryDataPath) {
      this.ancillaryDataPath = ancillaryDataPath;
      return this;
    }

    public InstrumentData build() {
      return new InstrumentData(uuid, type, instrument, shortName, releaseDate, status,
          dataComment, dirName, bagName, otherFields, ancillaryDataDetails, dataPath, ancillaryDataPath);
    }
  }
}
