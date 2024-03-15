package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.util.Map;
import java.util.Objects;

@JsonTypeInfo(use = Id.NONE)
@JsonSubTypes({ @Type(InstrumentMetadata.class), @Type(InstrumentData.class ) })
public abstract class Instrument {

  private final String uuid;
  private final String type;
  private final String instrument;
  private final String shortName;
  private final String releaseDate;
  private final String status;
  private final String dataComment;
  private final String dirName;
  private final String bagName;
  private final Map<String, Object> otherFields;
  private final String ancillaryDataDetails;

  protected Instrument(String uuid, String type, String instrument, String shortName, String releaseDate, String status, String dataComment,
      String dirName, String bagName, Map<String, Object> otherFields, String ancillaryDataDetails) {
    this.uuid = uuid;
    this.type = type;
    this.instrument = instrument;
    this.shortName = shortName;
    this.releaseDate = releaseDate;
    this.status = status;
    this.dataComment = dataComment;
    this.dirName = dirName;
    this.bagName = bagName;
    this.otherFields = otherFields;
    this.ancillaryDataDetails = ancillaryDataDetails;
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

  public String getStatus() {
    return status;
  }

  public String getDataComment() {
    return dataComment;
  }

  public String getDirName() {
    return dirName;
  }

  public String getBagName() {
    return bagName;
  }

  @JsonAnyGetter
  public Map<String, Object> getOtherFields() {
    return otherFields;
  }

  public String getAncillaryDataDetails() {
    return ancillaryDataDetails;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Instrument that = (Instrument) o;
    return Objects.equals(uuid, that.uuid) && Objects.equals(type, that.type) && Objects.equals(instrument,
        that.instrument) && Objects.equals(shortName, that.shortName) && Objects.equals(releaseDate, that.releaseDate)
        && Objects.equals(status, that.status) && Objects.equals(dataComment, that.dataComment) && Objects.equals(dirName,
        that.dirName) && Objects.equals(bagName, that.bagName) && Objects.equals(otherFields, that.otherFields)
        && Objects.equals(ancillaryDataDetails, that.ancillaryDataDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, type, instrument, shortName, releaseDate, status, dataComment, dirName, bagName, otherFields, ancillaryDataDetails);
  }

  @Override
  public String toString() {
    return "Instrument{" +
        "uuid='" + uuid + '\'' +
        ", type='" + type + '\'' +
        ", instrument='" + instrument + '\'' +
        ", shortName='" + shortName + '\'' +
        ", releaseDate='" + releaseDate + '\'' +
        ", status='" + status + '\'' +
        ", dataComment='" + dataComment + '\'' +
        ", dirName='" + dirName + '\'' +
        ", bagName='" + bagName + '\'' +
        ", otherFields=" + otherFields +
        ", ancillaryDataDetails='" + ancillaryDataDetails + '\'' +
        '}';
  }
}
