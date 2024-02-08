package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public abstract class DatasetPanel extends JPanel {


  private final String dataType;
  private final DatasetConfigurationPanel parent;
  private final DatasetContentPanel content = new DatasetContentPanel();

  protected DatasetPanel(String dataType, DatasetConfigurationPanel parent) {
    this.dataType = dataType;
    this.parent = parent;
  }

  public void init() {
    setLayout(new BorderLayout());
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createEtchedBorder());
    add(content, BorderLayout.NORTH);
  }
}
