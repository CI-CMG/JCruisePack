package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.navigation;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.stereotype.Component;

@Component
public class NavigationDatasetPanelFactory implements DatasetPanelFactory {

  @Override
  public String getInstrumentGroupShortCode() {
    return "NAV";
  }

  @Override
  public DatasetPanel createPanel(DropDownItem dataType) {
    return new NavigationDatasetPanel(dataType);
  }
}
