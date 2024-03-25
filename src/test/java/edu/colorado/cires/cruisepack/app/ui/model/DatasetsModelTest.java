package edu.colorado.cires.cruisepack.app.ui.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.service.metadata.InstrumentData;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactoryResolver;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DefaultDatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.InstrumentGroupName;
import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class DatasetsModelTest extends PropertyChangeModelTest<DatasetsModel> {
  
  private static final InstrumentDatastore instrumentDatastore = mock(InstrumentDatastore.class);
  private static final DefaultDatasetPanelFactory datasetPanelFactory = new DefaultDatasetPanelFactory(instrumentDatastore);

  @Override
  protected DatasetsModel createModel() {
    return new DatasetsModel(new DatasetPanelFactoryResolver(List.of(
        datasetPanelFactory
    )));
  }

  @Test
  void restoreDefaults() {
    DatasetPanel<?, ?> datasetPanel = datasetPanelFactory.createPanel(InstrumentGroupName.XBT);
    
    model.setDatasetsError("datasets error");
    model.addDataset(datasetPanel);
    Path docsPath = Paths.get("documents path");
    model.setDocumentsPath(docsPath);
    model.setDocumentsPathError("documents path error");
    
    List<? extends BaseDatasetInstrumentModel<? extends AdditionalFieldsModel>> datasets = model.getDatasets();
    
    clearEvents();
    model.restoreDefaults();
    
    assertChangeEvent(Events.UPDATE_DATASETS_ERROR, "datasets error", null);
    assertNull(model.getDatasetsError());
    assertChangeEvent(Events.CLEAR_DATASET_LIST, datasets, Collections.emptyList());
    assertEquals(Collections.emptyList(), model.getDatasets());
    assertChangeEvent(Events.UPDATE_DOCS_DIRECTORY, docsPath, null);
    assertNull(model.getDocumentsPath());
    assertChangeEvent(Events.UPDATE_DOCS_DIRECTORY_ERROR, "documents path error", null);
    assertNull(model.getDocumentsPathError());
  }

  @Test
  void updateFormState() {
    CruiseData cruiseData = CruiseData.builder()
        .withInstruments(List.of(
            InstrumentData.builder()
                .withType("XBT")
                .withDataPath(Paths.get("xbt-path"))
                .withAncillaryDataPath(Paths.get("xbt-ancillary-path"))
                .build()
        )).withDocumentsPath("documents path")
        .build();
    
    model.updateFormState(cruiseData);
    
    assertChangeEvent(Events.UPDATE_DOCS_DIRECTORY, null, Paths.get("documents path"));
    assertEquals(cruiseData.getDocumentsPath(), model.getDocumentsPath().toString());
    
    List<PropertyChangeEvent> events = getEventMap().get(Events.ADD_DATASET);
    assertEquals(1, events.size());
    assertEquals(InstrumentGroupName.XBT.getShortName(), ((DatasetPanel<?, ?>) events.get(0).getNewValue()).getModel().getInstrumentGroupShortCode());
    assertNull(events.get(0).getOldValue());
    assertEquals(1, model.getDatasets().size());
    assertEquals(InstrumentGroupName.XBT.getShortName(), model.getDatasets().get(0).getInstrumentGroupShortCode());
  }

  @Test
  void addRemoveDatasets() {
    DatasetPanel<? extends AdditionalFieldsModel, ?> datasetPanel1 = datasetPanelFactory.createPanel(InstrumentGroupName.XBT);
    DatasetPanel<? extends AdditionalFieldsModel, ?> datasetPanel2 = datasetPanelFactory.createPanel(InstrumentGroupName.CTD);
    
    model.addDataset(datasetPanel1);
    assertChangeEvent(Events.ADD_DATASET, null, datasetPanel1);
    
    clearEvents();
    
    model.addDataset(datasetPanel2);
    assertChangeEvent(Events.ADD_DATASET, null, datasetPanel2);
    
    List<BaseDatasetInstrumentModel<? extends AdditionalFieldsModel>> expectedModels = List.of(
        datasetPanel1.getModel(), datasetPanel2.getModel()
    );
    
    assertEquals(expectedModels, model.getDatasets());
    
    model.removeDataset(datasetPanel1);
    assertChangeEvent(Events.REMOVE_DATASET, datasetPanel1, null);
    
    clearEvents();
    
    model.removeDataset(datasetPanel2);
    assertChangeEvent(Events.REMOVE_DATASET, datasetPanel2, null);
  }

  @Test
  void setDatasetsError() {
    assertPropertyChange(Events.UPDATE_DATASETS_ERROR, model::getDatasetsError, model::setDatasetsError, "value1", "value2", null);
  }

  @Test
  void setDocumentsPath() {
    assertPropertyChange(Events.UPDATE_DOCS_DIRECTORY, model::getDocumentsPath, model::setDocumentsPath, Paths.get("path1"), Paths.get("path2"), null);
  }

  @Test
  void setDocumentsPathError() {
    assertPropertyChange(Events.UPDATE_DOCS_DIRECTORY_ERROR, model::getDocumentsPathError, model::setDocumentsPathError, "value1", "value2", null);
  }
}