package edu.colorado.cires.cruisepack.dataset;

import edu.colorado.cires.cruisepack.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.ui.view.DatasetPanel;

public class GravityDataWidget extends DatasetPanel {

  public GravityDataWidget(
      DataWidgetModel model,
      DefaultController controller) {
    super("Gravity", model, controller);
  }
}
