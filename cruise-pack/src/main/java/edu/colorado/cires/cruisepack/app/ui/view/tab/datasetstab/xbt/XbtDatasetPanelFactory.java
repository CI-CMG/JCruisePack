package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.xbt;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.stereotype.Component;

@Component
public class XbtDatasetPanelFactory implements DatasetPanelFactory {

  @Override
  public String getInstrumentGroupShortCode() {
    return "XBT";
  }

  @Override
  public DatasetPanel createPanel(DropDownItem dataType) {
    return new XbtDatasetPanel(dataType);
  }
}
