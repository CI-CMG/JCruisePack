package edu.colorado.cires.cruisepack.app.service.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import java.util.Objects;

public interface Omics {

  String ncbiAccession();

  List<String> samplingTypes();

  List<String> analysesTypes();

  String omicsComment();

  OmicsPoc omicsPoc();
}
