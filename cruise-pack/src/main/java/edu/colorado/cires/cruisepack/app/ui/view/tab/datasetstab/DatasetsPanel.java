package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import jakarta.annotation.PostConstruct;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatasetsPanel extends JPanel implements ReactiveView {

  private static final String ADD_DATASET_LABEL = "Add Additional Dataset";

  private final JButton addDatasetButton = new JButton(ADD_DATASET_LABEL);

  private final ReactiveViewRegistry reactiveViewRegistry;
  private final DatasetConfigurationPanel datasetConfigurationPanel;

  @Autowired
  public DatasetsPanel(
      ReactiveViewRegistry reactiveViewRegistry,
      DatasetConfigurationPanel datasetConfigurationPanel) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.datasetConfigurationPanel = datasetConfigurationPanel;
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
  }

  private void setupMvc() {
    reactiveViewRegistry.register(this);
    addDatasetButton.addActionListener((evt) -> datasetConfigurationPanel.addRow());
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {

  }
}
