package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import java.util.Objects;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Omics (@JsonProperty("NCBI_accession") String ncbiAccession, List<String> samplingTypes, List<String> analysesTypes, String omicsComment, OmicsPoc omicsPoc) {

  @Override
  public String ncbiAccession() {
    return ncbiAccession;
  }

  @Override
  public List<String> samplingTypes() {
    return samplingTypes;
  }

  @Override
  public List<String> analysesTypes() {
    return analysesTypes;
  }

  @Override
  public String omicsComment() {
    return omicsComment;
  }

  @Override
  public OmicsPoc omicsPoc() {
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
        ", omicsPoc=" + omicsPoc +
        '}';
  }
}
