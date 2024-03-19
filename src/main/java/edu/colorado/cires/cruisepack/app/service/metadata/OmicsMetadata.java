package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(builder = OmicsMetadata.Builder.class)
public class OmicsMetadata extends Omics {
  
  public static Builder builder() {
    return new Builder();
  }

  private OmicsMetadata(String ncbiAccession, List<String> samplingTypes, List<String> analysesTypes, String omicsComment,
      OmicsPoc omicsPoc) {
    super(ncbiAccession, samplingTypes, analysesTypes, omicsComment, omicsPoc);
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
    
    public OmicsMetadata build() {
      return new OmicsMetadata(ncbiAccession, samplingTypes, analysesTypes, omicsComment, omicsPoc);
    }
  }
}
