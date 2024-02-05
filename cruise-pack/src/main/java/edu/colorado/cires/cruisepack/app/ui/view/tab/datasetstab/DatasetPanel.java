package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public abstract class DatasetPanel extends JPanel {


  private final String dataType;
  private final DatasetContentPanel content = new DatasetContentPanel();

  protected DatasetPanel(String dataType) {
    this.dataType = dataType;
  }

  public void init() {
    setLayout(new BorderLayout());
    add(content, BorderLayout.NORTH);
  }
}
