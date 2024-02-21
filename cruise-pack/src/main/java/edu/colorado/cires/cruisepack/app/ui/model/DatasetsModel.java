package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DatasetsModel extends PropertyChangeModel {

  @NotEmpty
  private List<@Valid ? extends BaseDatasetInstrumentModel> datasets = new ArrayList<>();
  private String datasetsError = null;

  public void restoreDefaults() {
    setDatasetsError(null);
    clearDatasets();
  }

  public List<? extends BaseDatasetInstrumentModel> getDatasets() {
    return datasets;
  }

  private void clearDatasets() {
    fireChangeEvent(Events.UPDATE_DATASET_LIST, datasets, Collections.emptyList());
  }

  public void addDataset(BaseDatasetInstrumentModel dataset) {
    List<BaseDatasetInstrumentModel> oldDatasets = new ArrayList<>(datasets);
    if (!oldDatasets.contains(dataset)) {
      List<BaseDatasetInstrumentModel> newDatasets = new ArrayList<>(datasets);
      newDatasets.add(dataset);
      datasets = newDatasets;
      fireChangeEvent(Events.UPDATE_DATASET_LIST, oldDatasets, datasets);
    }
  }

  public void removeDataset(BaseDatasetInstrumentModel dataset) {
    List<BaseDatasetInstrumentModel> oldDatasets = new ArrayList<>(datasets);
    if (oldDatasets.contains(dataset)) {
      List<BaseDatasetInstrumentModel> newDatasets = new ArrayList<>(datasets);
      newDatasets.remove(dataset);
      datasets = newDatasets;
      fireChangeEvent(Events.UPDATE_DATASET_LIST, oldDatasets, datasets);
    }
  }

  public String getDatasetsError() {
      return datasetsError;
  }

  public void setDatasetsError(String datasetsError) {
      setIfChanged(Events.UPDATE_DATASETS_ERROR, datasetsError, () -> this.datasetsError, (e) -> this.datasetsError = e);
  }

  

}
