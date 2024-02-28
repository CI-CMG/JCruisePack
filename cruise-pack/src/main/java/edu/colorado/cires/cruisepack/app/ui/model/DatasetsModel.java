package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactoryResolver;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatasetsModel extends PropertyChangeModel {
  
  private  final DatasetPanelFactoryResolver factoryResolver;

  @NotEmpty
  private List<@Valid ? extends BaseDatasetInstrumentModel> datasets = new ArrayList<>();
  private String datasetsError = null;

  @Autowired
  public DatasetsModel(DatasetPanelFactoryResolver factoryResolver) {
    this.factoryResolver = factoryResolver;
  }

  public void restoreDefaults() {
    setDatasetsError(null);
    clearDatasets();
  }
  
  public void updateFormState(CruiseMetadata metadata) {
    clearDatasets();
    
    for (Instrument instrument : metadata.getInstruments()) {
      DatasetPanel<?, ?> panel = factoryResolver.createDatasetPanel(instrument);
      addDataset(panel);
    }
  }

  public List<? extends BaseDatasetInstrumentModel> getDatasets() {
    return datasets;
  }

  private void clearDatasets() {
    List<BaseDatasetInstrumentModel> oldDatasets = new ArrayList<>(datasets);
    datasets = new ArrayList<>(0);
    fireChangeEvent(Events.CLEAR_DATASET_LIST, oldDatasets, datasets);
  }

  public void addDataset(DatasetPanel<?, ?> row) {
    List<BaseDatasetInstrumentModel> oldDatasets = new ArrayList<>(datasets);
    BaseDatasetInstrumentModel dataset = row.getModel();
    if (!oldDatasets.contains(dataset)) {
      List<BaseDatasetInstrumentModel> newDatasets = new ArrayList<>(datasets);
      newDatasets.add(dataset);
      datasets = newDatasets;
      fireChangeEvent(Events.ADD_DATASET, null, row);
    }
  }

  public void removeDataset(DatasetPanel<?, ?> row) {
    List<BaseDatasetInstrumentModel> oldDatasets = new ArrayList<>(datasets);
    BaseDatasetInstrumentModel dataset = row.getModel();
    if (oldDatasets.contains(dataset)) {
      List<BaseDatasetInstrumentModel> newDatasets = new ArrayList<>(datasets);
      newDatasets.remove(dataset);
      datasets = newDatasets;
      fireChangeEvent(Events.REMOVE_DATASET, row, null);
    }
  }

  public String getDatasetsError() {
      return datasetsError;
  }

  public void setDatasetsError(String datasetsError) {
      setIfChanged(Events.UPDATE_DATASETS_ERROR, datasetsError, () -> this.datasetsError, (e) -> this.datasetsError = e);
  }

  

}
