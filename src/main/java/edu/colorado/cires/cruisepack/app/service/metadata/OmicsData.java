package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.nio.file.Path;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder = OmicsData.Builder.class)
public class OmicsData extends Omics {

  public static Builder builder() {
    return new Builder();
  }
  
  private final Path sampleTrackingSheet;
  private final boolean samplingConducted;
  
  private OmicsData(@JsonProperty("NBI_accession") String ncbiAccession, List<String> samplingTypes, List<String> analysesTypes, String omicsComment, OmicsPoc omicsPoc,
      Path sampleTrackingSheet, boolean samplingConducted) {
    super(ncbiAccession, samplingTypes, analysesTypes, omicsComment, omicsPoc);
    this.sampleTrackingSheet = sampleTrackingSheet;
    this.samplingConducted = samplingConducted;
  }

  public Path getSampleTrackingSheet() {
    return sampleTrackingSheet;
  }

  public boolean isSamplingConducted() {
    return samplingConducted;
  }

  public static class Builder {
    private String ncbiAccession;
    private List<String> samplingTypes;
    private List<String> analysesTypes;
    private String omicsComment;
    private OmicsPoc omicsPoc;
    private Path sampleTrackingSheet;
    private boolean samplingConducted;
    
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
    
    public Builder withSampleTrackingSheet(Path sampleTrackingSheet) {
      this.sampleTrackingSheet = sampleTrackingSheet;
      return this;
    }
    
    public Builder withSamplingConducted(boolean samplingConducted) {
      this.samplingConducted = samplingConducted;
      return this;
    }
    
    public OmicsData build() {
      return new OmicsData(ncbiAccession, samplingTypes, analysesTypes, omicsComment, omicsPoc, sampleTrackingSheet, samplingConducted);
    }
  }
}
