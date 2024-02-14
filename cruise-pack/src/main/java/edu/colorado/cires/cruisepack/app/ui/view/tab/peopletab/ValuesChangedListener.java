package edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab;

import java.util.List;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

public interface ValuesChangedListener {

    void handleValuesChanged(List<DropDownItem> items);
    
}
