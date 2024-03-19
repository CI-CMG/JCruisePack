package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import java.util.Objects;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder = Omics.Builder.class)
public class Omics {
  public static Builder builder() {
    return new Builder();
  }
  
  private final String ncbiAccession;
  private final List<String> samplingTypes;
  private final List<String> analysesTypes;
  private final String omicsComment;
  private final OmicsPoc omicsPoc;

  private Omics(@JsonProperty("NBI_accession") String ncbiAccession, List<String> samplingTypes, List<String> analysesTypes, String omicsComment, OmicsPoc omicsPoc) {
    this.ncbiAccession = ncbiAccession;
    this.samplingTypes = samplingTypes;
    this.analysesTypes = analysesTypes;
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

  public String getOmicsComment() {
    return omicsComment;
  }

  public OmicsPoc getOmicsPoc() {
    return omicsPoc;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Omics omics)) {
      return false;
    }
    return Objects.equals(ncbiAccession, omics.ncbiAccession) && Objects.equals(samplingTypes, omics.samplingTypes)
        && Objects.equals(analysesTypes, omics.analysesTypes) && Objects.equals(omicsComment, omics.omicsComment)
        && Objects.equals(omicsPoc, omics.omicsPoc);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ncbiAccession, samplingTypes, analysesTypes, omicsComment, omicsPoc);
  }

  @Override
  public String toString() {
    return "Omics{" +
        "ncbiAccession='" + ncbiAccession + '\'' +
        ", samplingTypes=" + samplingTypes +
        ", analysesTypes=" + analysesTypes +
        ", omicsComment='" + omicsComment + '\'' +
        ", omicsPoc='" + omicsPoc + '\'' +
        '}';
  }
  
  public static class Builder {
    private String ncbiAccession;
    private List<String> samplingTypes;
    private List<String> analysesTypes;
    private String omicsComment;
    private OmicsPoc omicsPoc;
    
    private Builder() {}
    
    public Builder withNCBIAccession(String ncbiAccession) {
      this.ncbiAccession = ncbiAccession;
      return this;
    }
    
    public Builder withSamplingTypes(List<String> samplingTypes) {
      this.samplingTypes = samplingTypes;
      return this;
    }
    
    public Builder withAnalysesTypes(List<String> analysesTypes) {
      this.analysesTypes = analysesTypes;
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
      return new Omics(ncbiAccession, samplingTypes, analysesTypes, omicsComment, omicsPoc);
    }
  }
}
