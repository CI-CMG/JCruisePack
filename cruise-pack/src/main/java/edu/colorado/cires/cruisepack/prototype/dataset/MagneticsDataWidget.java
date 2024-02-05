package edu.colorado.cires.cruisepack.prototype.dataset;

import edu.colorado.cires.cruisepack.prototype.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.prototype.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.prototype.ui.view.DatasetPanel;

public class MagneticsDataWidget extends DatasetPanel {

  public MagneticsDataWidget(
      DefaultController controller,
      DataWidgetModel model) {
    super("Magnetics", model, controller);
  }
}
