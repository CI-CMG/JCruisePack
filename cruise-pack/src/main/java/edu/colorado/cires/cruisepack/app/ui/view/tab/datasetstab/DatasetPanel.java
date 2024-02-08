package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public abstract class DatasetPanel extends JPanel {

  private final DatasetPanelHeader header;
  private final String dataTypeName;

  protected DatasetPanel(DropDownItem dataType) {
    dataTypeName = dataType.getValue();
    header = new DatasetPanelHeader(dataType.getValue());
  }

  protected abstract JPanel createAndInitializeContentPanel();

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
    setBorder(BorderFactory.createTitledBorder(dataTypeName));
    add(header, BorderLayout.NORTH);
    add(createAndInitializeContentPanel(), BorderLayout.CENTER);
  }

  private void setupMvc() {

  }
}
