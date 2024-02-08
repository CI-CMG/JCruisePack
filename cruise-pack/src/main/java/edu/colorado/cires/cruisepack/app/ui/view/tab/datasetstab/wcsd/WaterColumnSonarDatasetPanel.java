package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.wcsd;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import javax.swing.JPanel;

class WaterColumnSonarDatasetPanel extends DatasetPanel {

  public WaterColumnSonarDatasetPanel(DropDownItem dataType) {
    super(dataType);
  }

  @Override
  protected JPanel createAndInitializeContentPanel() {
    WaterColumnSonarDatasetContentPanel panel = new WaterColumnSonarDatasetContentPanel();
    panel.init();
    return panel;
  }
}
