package edu.colorado.cires.cruisepack.dataset;

import edu.colorado.cires.cruisepack.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.ui.view.DatasetPanel;
import java.util.List;

public class SinglebeamBathymetryDataWidget extends DatasetPanel {

  public SinglebeamBathymetryDataWidget(
      DefaultController controller,
      DataWidgetModel model) {
    super("Singlebeam Bathymetry", model, controller);
  }
}
