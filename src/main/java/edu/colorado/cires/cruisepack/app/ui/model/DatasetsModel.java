package edu.colorado.cires.cruisepack.app.ui.model;

import edu.colorado.cires.cruisepack.app.service.metadata.Cruise;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.service.metadata.InstrumentData;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.model.validation.DocumentsUnderMaxAllowed;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathExists;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsDirectory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactoryResolver;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatasetsModel extends PropertyChangeModel {
  
  private  final DatasetPanelFactoryResolver factoryResolver;

  @NotEmpty
  private List<@Valid ? extends BaseDatasetInstrumentModel<@Valid ? extends AdditionalFieldsModel>> datasets = new ArrayList<>();
  private String datasetsError = null;

  @PathExists
  @PathIsDirectory
  @DocumentsUnderMaxAllowed
  private Path documentsPath;
  private String documentsPathError;

  @Autowired
  public DatasetsModel(DatasetPanelFactoryResolver factoryResolver) {
    this.factoryResolver = factoryResolver;
  }

  public void restoreDefaults() {
    setDatasetsError(null);
    clearDatasets();
    setDocumentsPath(null);
    setDocumentsPathError(null);
  }
  
  public void updateFormState(Cruise metadata) {
    clearDatasets();
    
    for (Instrument instrument : metadata.getInstruments().stream().filter(i -> !i.getType().contains("Ancillary Data")).toList()) {
      DatasetPanel<? extends AdditionalFieldsModel, ?> panel = factoryResolver.createDatasetPanel(instrument);
      Optional<Instrument> maybeAncillaryInstrument = metadata.getInstruments().stream()
          .filter(i -> i.getType().equals("Ancillary Data"))
          .filter(i -> i.getInstrument().equals(String.format("%s Ancillary", instrument.getType())))
          .filter(i -> i.getStatus().equals(instrument.getStatus()))
          .filter(i -> i.getUuid().equals(instrument.getUuid()))
          .findFirst();
      if (maybeAncillaryInstrument.isPresent()) {
        Instrument ancillaryInstrument = maybeAncillaryInstrument.get();
        if (ancillaryInstrument instanceof InstrumentData) {
          panel.getModel().setAncillaryPath(((InstrumentData) ancillaryInstrument).getDataPath());
        }
        panel.getModel().setAncillaryDetails(ancillaryInstrument.getDataComment());
      }
      addDataset(panel);
    }

    if (metadata instanceof CruiseData) {
      String docPath = ((CruiseData) metadata).getDocumentsPath();
      setDocumentsPath(docPath == null ? null : Paths.get(docPath));
    }
    setDocumentsPathError(null);
  }

  public List<? extends BaseDatasetInstrumentModel<? extends AdditionalFieldsModel>> getDatasets() {
    return datasets;
  }

  private void clearDatasets() {
    List<BaseDatasetInstrumentModel<? extends AdditionalFieldsModel>> oldDatasets = new ArrayList<>(datasets);
    datasets = new ArrayList<>(0);
    fireChangeEvent(Events.CLEAR_DATASET_LIST, oldDatasets, datasets);
  }

  public void addDataset(DatasetPanel<? extends AdditionalFieldsModel, ?> row) {
    List<BaseDatasetInstrumentModel<?>> oldDatasets = new ArrayList<>(datasets);
    BaseDatasetInstrumentModel<? extends AdditionalFieldsModel> dataset = row.getModel();
    if (!oldDatasets.contains(dataset)) {
      List<BaseDatasetInstrumentModel<? extends AdditionalFieldsModel>> newDatasets = new ArrayList<>(datasets);
      newDatasets.add(dataset);
      datasets = newDatasets;
      fireChangeEvent(Events.ADD_DATASET, null, row);
    }
  }

  public void removeDataset(DatasetPanel<? extends AdditionalFieldsModel, ?> row) {
    List<BaseDatasetInstrumentModel<?>> oldDatasets = new ArrayList<>(datasets);
    BaseDatasetInstrumentModel<? extends AdditionalFieldsModel> dataset = row.getModel();
    if (oldDatasets.contains(dataset)) {
      List<BaseDatasetInstrumentModel<? extends AdditionalFieldsModel>> newDatasets = new ArrayList<>(datasets);
      newDatasets.remove(dataset);
      datasets = newDatasets;
      fireChangeEvent(Events.REMOVE_DATASET, row, null);
    }
  }

  public void setDatasetsError(String datasetsError) {
      setIfChanged(Events.UPDATE_DATASETS_ERROR, datasetsError, () -> this.datasetsError, (e) -> this.datasetsError = e);
  }

  public Path getDocumentsPath() {
    return documentsPath;
  }

  public void setDocumentsPath(Path documentsPath) {
    setIfChanged(Events.UPDATE_DOCS_DIRECTORY, documentsPath, () -> this.documentsPath, (nv) -> this.documentsPath = nv);
  }

  public String getDocumentsPathError() {
    return documentsPathError;
  }

  public void setDocumentsPathError(String documentsPathError) {
    setIfChanged(Events.UPDATE_DOCS_DIRECTORY_ERROR, documentsPathError, () -> this.documentsPathError, (nv) -> this.documentsPathError = nv);
  }

  public String getDatasetsError() {
    return datasetsError;
  }
}
