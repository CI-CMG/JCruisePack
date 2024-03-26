package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OmicsMetadata(
    @JsonProperty("NCBI_accession") String ncbiAccession, List<String> samplingTypes, List<String> analysesTypes, String omicsComment, OmicsPoc omicsPoc
) implements Omics {}
