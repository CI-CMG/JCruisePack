package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ctd;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.stereotype.Component;

@Component
public class CtdDatasetPanelFactory implements DatasetPanelFactory {

  @Override
  public String getInstrumentGroupShortCode() {
    return "CTD";
  }

  @Override
  public DatasetPanel createPanel(DropDownItem dataType) {
    return new CtdDatasetPanel(dataType);
  }
}
