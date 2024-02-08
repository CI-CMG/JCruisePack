package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.singebeam;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.stereotype.Component;

@Component
public class SinglebeamDatasetPanelFactory implements DatasetPanelFactory {

  @Override
  public String getInstrumentGroupShortCode() {
    return "SB-BATHY";
  }

  @Override
  public DatasetPanel createPanel(DropDownItem dataType) {
    return new SinglebeamDatasetPanel(dataType);
  }
}
