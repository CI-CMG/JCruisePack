package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ctd;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import javax.swing.JPanel;

class CtdDatasetPanel extends DatasetPanel {

  public CtdDatasetPanel(DropDownItem dataType) {
    super(dataType);
  }

  @Override
  protected JPanel createAndInitializeContentPanel() {
    CtdDatasetContentPanel panel = new CtdDatasetContentPanel();
    panel.init();
    return panel;
  }
}
