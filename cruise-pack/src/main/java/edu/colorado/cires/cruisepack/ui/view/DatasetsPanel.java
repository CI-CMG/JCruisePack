package edu.colorado.cires.cruisepack.ui.view;

import edu.colorado.cires.cruisepack.dataset.OtherDataWidget;
import edu.colorado.cires.cruisepack.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.ui.controller.Events;
import edu.colorado.cires.cruisepack.ui.model.DataWidgetModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

class DatasetsPanel extends JPanel implements BaseViewPanel {

  private final DefaultController controller;
  private final DatasetConfigurationPanel datasetConfigurationPanel = new DatasetConfigurationPanel();
  private final List<DatasetPanel> datasets = new ArrayList<>();
  private final JButton addDatasetButton = new JButton("Add Additional Dataset");

  DatasetsPanel(DefaultController controller) {
    this.controller = controller;
    setLayout(new GridBagLayout());

    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    add(addDatasetButton, c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = 1;
    c.weighty = 100;
    c.weightx = 100;
    add(new JScrollPane(datasetConfigurationPanel), c);

    addDatasetButton.addActionListener((evt) -> controller.addDataset());
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.ADD_DATASET:
        addDataset((DatasetPanel) evt.getNewValue());
        break;
      case Events.REMOVE_DATASET:
        removeDataset((String) evt.getNewValue());
        break;
        case Events.CHANGE_DATASET:
          changeDataset((String) evt.getOldValue(), (DatasetPanel) evt.getNewValue());
        break;
      default:
        break;
    }
  }

  private void addDataset(DatasetPanel datasetPanel) {
    datasets.add(datasetPanel);
    datasetConfigurationPanel.addDatasetPanel(datasetPanel);
    controller.addView(datasetPanel);
    datasetConfigurationPanel.validate();
    datasetConfigurationPanel.repaint();
  }

  private void removeDataset(String id) {
    Iterator<DatasetPanel> it = datasets.iterator();
    while (it.hasNext()) {
      DatasetPanel datasetPanel = it.next();
      if (datasetPanel.getId().equals(id)) {
        controller.removeView(datasetPanel);
        datasetConfigurationPanel.removeDatasetPanel(datasetPanel);
        it.remove();
      }
    }
    datasetConfigurationPanel.validate();
    datasetConfigurationPanel.repaint();
  }

  private void changeDataset(String id, DatasetPanel datasetPanel) {
    Iterator<DatasetPanel> it = datasets.iterator();
    int found = 0;
    DatasetPanel existing = null;
    int i = 0;
    while (it.hasNext()) {
      existing = it.next();
      if (existing.getId().equals(id)) {
        found = i;
        break;
      }
      i++;
    }
    if (found >=0 ){
      controller.removeView(existing);
      datasetConfigurationPanel.remove(found);
      datasets.remove(existing);

      datasets.add(datasetPanel);
      datasetConfigurationPanel.addDatasetPanel(datasetPanel, found);
      controller.addView(datasetPanel);
    }
    datasetConfigurationPanel.validate();
    datasetConfigurationPanel.repaint();
  }

  private static class DatasetConfigurationPanel extends JPanel {
    DatasetConfigurationPanel() {
      setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
      setBackground(Color.WHITE);
//      Box.Filler glue = (Box.Filler) Box.createVerticalGlue();
//      glue.changeShape(glue.getMinimumSize(),
//          new Dimension(0, Short.MAX_VALUE),   // prefer it big
//          glue.getMaximumSize());
//      add(glue);
    }

    void addDatasetPanel(DatasetPanel datasetPanel) {
//      add(datasetPanel, getComponents().length - 2);
      add(datasetPanel);
    }

    void addDatasetPanel(DatasetPanel datasetPanel, int index) {
      add(datasetPanel, index);
    }

    void removeDatasetPanel(DatasetPanel datasetPanel) {
      remove(datasetPanel);
    }
  }
}
