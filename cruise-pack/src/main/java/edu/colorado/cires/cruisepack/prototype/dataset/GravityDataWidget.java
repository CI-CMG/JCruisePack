package edu.colorado.cires.cruisepack.prototype.dataset;

import edu.colorado.cires.cruisepack.prototype.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.prototype.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.prototype.ui.view.DatasetPanel;

public class GravityDataWidget extends DatasetPanel {

  public GravityDataWidget(
      DataWidgetModel model,
      DefaultController controller) {
    super("Gravity", model, controller);
  }
}
