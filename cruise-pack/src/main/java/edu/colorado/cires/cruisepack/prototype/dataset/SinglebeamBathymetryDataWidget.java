package edu.colorado.cires.cruisepack.prototype.dataset;

import edu.colorado.cires.cruisepack.prototype.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.prototype.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.prototype.ui.view.DatasetPanel;

public class SinglebeamBathymetryDataWidget extends DatasetPanel {

  public SinglebeamBathymetryDataWidget(
      DefaultController controller,
      DataWidgetModel model) {
    super("Singlebeam Bathymetry", model, controller);
  }
}
