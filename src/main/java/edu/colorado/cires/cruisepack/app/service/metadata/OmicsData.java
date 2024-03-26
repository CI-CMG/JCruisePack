package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.nio.file.Path;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OmicsData(
    @JsonProperty("NCBI_accession") String ncbiAccession, List<String> samplingTypes, List<String> analysesTypes, String omicsComment, OmicsPoc omicsPoc,
    Path trackingPath
) implements Omics {
  
  public boolean samplingConducted() {
    return ncbiAccession() != null ||
        !samplingTypes().isEmpty() ||
        !analysesTypes().isEmpty() ||
        omicsComment() != null ||
        omicsPoc() != null ||
        trackingPath() != null;
  }
  
  public static OmicsData create(OmicsData src) {
    return new OmicsData(
        src.ncbiAccession(), 
        src.samplingTypes(),
        src.analysesTypes(),
        src.omicsComment(),
        src.omicsPoc(),
        src.trackingPath()
    );
  }
  
}
