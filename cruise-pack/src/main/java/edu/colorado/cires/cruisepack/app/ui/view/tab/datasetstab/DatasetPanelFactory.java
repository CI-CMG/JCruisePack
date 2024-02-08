package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

public interface DatasetPanelFactory {

  String getInstrumentGroupShortCode();

  DatasetPanel createPanel(DropDownItem dataType);

}
