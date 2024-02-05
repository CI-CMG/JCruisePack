package edu.colorado.cires.cruisepack.prototype.dataset;

import edu.colorado.cires.cruisepack.prototype.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.prototype.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.prototype.ui.view.DatasetPanel;

public class MultibeamBathymetryDataWidget extends DatasetPanel {

  public MultibeamBathymetryDataWidget(
      DefaultController controller,
      DataWidgetModel model) {
    super("Multibeam Bathymetry", model, controller);
  }
}
