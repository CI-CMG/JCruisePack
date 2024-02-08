package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.other;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanelFactory;
import org.springframework.stereotype.Component;

@Component
public class OtherDatasetPanelFactory implements DatasetPanelFactory {

  @Override
  public String getInstrumentGroupShortCode() {
    return "OTHER";
  }

  @Override
  public DatasetPanel createPanel(DropDownItem dataType) {
    return new OtherDatasetPanel(dataType);
  }
}
