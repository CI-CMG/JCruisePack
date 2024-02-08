package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ancillary;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import javax.swing.JPanel;

class AncillaryDatasetPanel extends DatasetPanel {

  public AncillaryDatasetPanel(DropDownItem dataType) {
    super(dataType);
  }

  @Override
  protected JPanel createAndInitializeContentPanel() {
    AncillaryDatasetContentPanel panel = new AncillaryDatasetContentPanel();
    panel.init();
    return panel;
  }
}
