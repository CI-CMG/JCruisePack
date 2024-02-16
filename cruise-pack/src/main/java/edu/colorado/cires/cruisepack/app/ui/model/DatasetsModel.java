package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DatasetsModel extends PropertyChangeModel {

  private List<@Valid ? extends BaseDatasetInstrumentModel> datasets = new ArrayList<>();

  public List<? extends BaseDatasetInstrumentModel> getDatasets() {
    return datasets;
  }

  public void addDataset(BaseDatasetInstrumentModel dataset) {
    if (!datasets.contains(dataset)) {
      List<BaseDatasetInstrumentModel> newDatasets = new ArrayList<>(datasets);
      newDatasets.add(dataset);
      fireChangeEvent(Events.UPDATE_DATASET_LIST, datasets, newDatasets);
    }
  }

  public void removeDataset(BaseDatasetInstrumentModel dataset) {
    if (datasets.contains(dataset)) {
      List<BaseDatasetInstrumentModel> newDatasets = new ArrayList<>(datasets);
      newDatasets.remove(dataset);
      fireChangeEvent(Events.UPDATE_DATASET_LIST, datasets, newDatasets);
    }
  }

}
