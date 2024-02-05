package edu.colorado.cires.cruisepack.prototype.datastore;

import edu.colorado.cires.cruisepack.prototype.dataset.Dataset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class DatasetDatastore {

  private final Map<String, Dataset> datasets;

  public DatasetDatastore() {
    Map<String, Dataset> datasets = new HashMap<>();
    for (Dataset dataset : ServiceLoader.load(Dataset.class)) {
      datasets.put(dataset.getDataType(), dataset);
    }
    this.datasets = Collections.unmodifiableMap(datasets);
  }

  public List<String> getDatasetTypes() {
    return new ArrayList<>(datasets.keySet());
  }

  public Dataset getDatasetFactory(String type) {
    return datasets.get(type);
  }

}
