package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.DatasetsController;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.tab.cruisetab.CruiseDocumentsPanel;
import jakarta.annotation.PostConstruct;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class DatasetsPanel extends JPanel implements ReactiveView {

  private static final String ADD_DATASET_LABEL = "Add Dataset";

  private final JButton addDatasetButton = new JButton(ADD_DATASET_LABEL);

  private final ReactiveViewRegistry reactiveViewRegistry;
  private final DatasetConfigurationPanel datasetConfigurationPanel;
  private final DatasetsController datasetsController;
  private final CruiseDocumentsPanel cruiseDocumentsPanel;

  @Autowired
  public DatasetsPanel(
      ReactiveViewRegistry reactiveViewRegistry,
      DatasetConfigurationPanel datasetConfigurationPanel,
      DatasetsController datasetsController, CruiseDocumentsPanel cruiseDocumentsPanel
  ) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.datasetConfigurationPanel = datasetConfigurationPanel;
    this.datasetsController = datasetsController;
    this.cruiseDocumentsPanel = cruiseDocumentsPanel;
  }

  @PostConstruct
  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void initializeFields() {

  }

  private void setupLayout() {
    setLayout(new GridBagLayout());
    add(addDatasetButton, configureLayout(0, 0));
    add(new JScrollPane(datasetConfigurationPanel), configureLayout(0, 1, c -> {
      c.fill = GridBagConstraints.BOTH;
      c.weighty = 1;
    }));
    add(cruiseDocumentsPanel, configureLayout(0, 2, c -> c.weighty = 0));
  }

  private void setupMvc() {
    reactiveViewRegistry.register(this);
    addDatasetButton.addActionListener((evt) -> datasetConfigurationPanel.addRow());
    datasetConfigurationPanel.addDatasetCreatedListener(datasetsController::addDataset);
    datasetConfigurationPanel.addDatasetRemovedListener(datasetsController::removeDataset);
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.ADD_DATASET -> datasetConfigurationPanel.createRow((DatasetPanel<? extends AdditionalFieldsModel, ?>) evt.getNewValue());
      case Events.REMOVE_DATASET -> datasetConfigurationPanel.removeRow((DatasetPanel<? extends AdditionalFieldsModel, ?>) evt.getOldValue());
      case Events.CLEAR_DATASET_LIST -> datasetConfigurationPanel.removeAllRows();
    }
  }
}
