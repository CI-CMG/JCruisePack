package edu.colorado.cires.cruisepack.ui.model;

import java.util.List;
import java.util.Map;

public class DropDownModel extends AbstractModel {

  private final Map<String, List<String>> instruments;
  private final List<String> datasetTypes;

  public DropDownModel(Map<String, List<String>> instruments, List<String> datasetTypes) {
    this.instruments = instruments;
    this.datasetTypes = datasetTypes;
  }

  public List<String> getInstruments(String dataType) {
    return instruments.get(dataType);
  }

  public List<String> getDatasetTypes() {
    return datasetTypes;
  }
}
