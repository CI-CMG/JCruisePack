package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import java.util.Objects;

public abstract class Omics {
  
  private final String ncbiAccession;
  private final List<String> samplingTypes;
  private final List<String> analysesTypes;
  private final String omicsComment;
  private final OmicsPoc omicsPoc;

  protected Omics(@JsonProperty("NBI_accession") String ncbiAccession, List<String> samplingTypes, List<String> analysesTypes, String omicsComment, OmicsPoc omicsPoc) {
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
}
