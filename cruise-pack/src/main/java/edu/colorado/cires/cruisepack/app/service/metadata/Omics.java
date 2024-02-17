package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder = Omics.Builder.class)
public class Omics {

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(Omics src) {
    return new Builder(src);
  }

  @JsonProperty("NCBI_accession")
  private final String ncbiAccession;
  private final List<String> samplingTypes;
  private final List<String> analysesTypes;
  private final String trackingPath;
  private final String omicsComment;
  private final OmicsPoc omicsPoc;

  private Omics(String ncbiAccession, List<String> samplingTypes, List<String> analysesTypes, String trackingPath, String omicsComment,
      OmicsPoc omicsPoc) {
    this.ncbiAccession = ncbiAccession;
    this.samplingTypes = samplingTypes;
    this.analysesTypes = analysesTypes;
    this.trackingPath = trackingPath;
    this.omicsComment = omicsComment;
    this.omicsPoc = omicsPoc;
  }

  public String getNcbiAccession() {
    return ncbiAccession;
  }

  public List<String> getSamplingTypes() {
    return samplingTypes;
  }

  public List<String> getAnalysesTypes() {
    return analysesTypes;
  }

  public String getTrackingPath() {
    return trackingPath;
  }

  public String getOmicsComment() {
    return omicsComment;
  }

  public OmicsPoc getOmicsPoc() {
    return omicsPoc;
  }

  public static class Builder {
    private String ncbiAccession;
    private List<String> samplingTypes = Collections.emptyList();
    private List<String> analysesTypes = Collections.emptyList();
    private String trackingPath;
    private String omicsComment;
    private OmicsPoc omicsPoc;

    private Builder() {

    }

    private Builder(Omics src) {
      ncbiAccession = src.ncbiAccession;
      samplingTypes = src.samplingTypes;
      analysesTypes = src.analysesTypes;
      trackingPath = src.trackingPath;
      omicsComment = src.omicsComment;
      omicsPoc = src.omicsPoc;
    }

    public Builder withNcbiAccession(String ncbiAccession) {
      this.ncbiAccession = ncbiAccession;
      return this;
    }

    public Builder withSamplingTypes(List<String> samplingTypes) {
      if (samplingTypes == null) {
        samplingTypes = new ArrayList<>(0);
      }
      this.samplingTypes = Collections.unmodifiableList(new ArrayList<>(samplingTypes));
      return this;
    }

    public Builder withAnalysesTypes(List<String> analysesTypes) {
      if (analysesTypes == null) {
        analysesTypes = new ArrayList<>(0);
      }
      this.analysesTypes = Collections.unmodifiableList(new ArrayList<>(analysesTypes));
      return this;
    }

    public Builder withTrackingPath(String trackingPath) {
      this.trackingPath = trackingPath;
      return this;
    }

    public Builder withOmicsComment(String omicsComment) {
      this.omicsComment = omicsComment;
      return this;
    }

    public Builder withOmicsPoc(OmicsPoc omicsPoc) {
      this.omicsPoc = omicsPoc;
      return this;
    }

    public Omics build() {
      return new Omics(ncbiAccession, samplingTypes, analysesTypes, trackingPath, omicsComment, omicsPoc);
    }
  }
}
