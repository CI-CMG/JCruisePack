package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
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
