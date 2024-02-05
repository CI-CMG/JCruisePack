package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import jakarta.annotation.PostConstruct;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatasetsPanel extends JPanel {

  private static final String ADD_DATASET_LABEL = "Add Additional Dataset";

  private final JButton addDatasetButton = new JButton(ADD_DATASET_LABEL);

  private final DatasetConfigurationPanel datasetConfigurationPanel;

  @Autowired
  public DatasetsPanel(DatasetConfigurationPanel datasetConfigurationPanel) {
    this.datasetConfigurationPanel = datasetConfigurationPanel;
  }

  @PostConstruct
  public void init() {
    setLayout(new GridBagLayout());
    add(addDatasetButton, configureLayout(0, 0));
    add(new JScrollPane(datasetConfigurationPanel), configureLayout(0, 1, c -> {
      c.fill = GridBagConstraints.BOTH;
      c.weightx = 100;
      c.weighty = 100;
    }));
  }
}
