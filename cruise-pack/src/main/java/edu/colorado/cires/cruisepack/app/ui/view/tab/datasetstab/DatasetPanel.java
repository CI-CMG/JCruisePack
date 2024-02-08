package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class DatasetPanel extends JPanel {

  private final DatasetPanelHeader header;

  protected DatasetPanel(DropDownItem dataType) {
    header = new DatasetPanelHeader(dataType.getValue());
  }

  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void initializeFields() {
    header.init();
  }

  private void setupLayout() {
    setLayout(new BorderLayout());
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createEtchedBorder());
    add(header, BorderLayout.NORTH);
  }

  private void setupMvc() {

  }
}
