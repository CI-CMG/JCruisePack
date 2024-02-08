package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.sss;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.stereotype.Component;

@Component
public class SideScanDatasetPanelFactory implements DatasetPanelFactory {

  @Override
  public String getInstrumentGroupShortCode() {
    return "SIDE-SCAN";
  }

  @Override
  public DatasetPanel createPanel(DropDownItem dataType) {
    return new SideScanDatasetPanel(dataType);
  }
}
