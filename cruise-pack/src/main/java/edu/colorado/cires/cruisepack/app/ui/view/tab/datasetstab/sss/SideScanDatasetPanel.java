package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.sss;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import javax.swing.JPanel;

class SideScanDatasetPanel extends DatasetPanel {

  public SideScanDatasetPanel(DropDownItem dataType) {
    super(dataType);
  }

  @Override
  protected JPanel createAndInitializeContentPanel() {
    SideScanSonarDatasetContentPanel panel = new SideScanSonarDatasetContentPanel();
    panel.init();
    return panel;
  }
}
