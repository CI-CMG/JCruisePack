package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import javax.swing.JPanel;

public class OtherDatasetPanel extends DatasetPanel {

  public OtherDatasetPanel(DropDownItem dataType) {
    super(dataType);
  }

  @Override
  protected JPanel createAndInitializeContentPanel() {
    OtherDatasetContentPanel panel = new OtherDatasetContentPanel();
    panel.init();
    return panel;
  }
}
