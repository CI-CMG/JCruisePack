package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;

public interface DatasetListener {
    void handle(DatasetPanel<?, ?> model);
}
