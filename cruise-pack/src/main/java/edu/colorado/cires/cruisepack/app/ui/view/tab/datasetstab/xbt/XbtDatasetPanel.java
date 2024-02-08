package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.xbt;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import javax.swing.JPanel;

class XbtDatasetPanel extends DatasetPanel {

  public XbtDatasetPanel(DropDownItem dataType) {
    super(dataType);
  }

  @Override
  protected JPanel createAndInitializeContentPanel() {
    XbtDatasetContentPanel panel = new XbtDatasetContentPanel();
    panel.init();
    return panel;
  }
}
