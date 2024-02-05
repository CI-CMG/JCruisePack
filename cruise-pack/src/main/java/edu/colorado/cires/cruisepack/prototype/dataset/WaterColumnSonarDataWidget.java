package edu.colorado.cires.cruisepack.prototype.dataset;

import edu.colorado.cires.cruisepack.prototype.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.prototype.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.prototype.ui.view.DatasetPanel;

public class WaterColumnSonarDataWidget extends DatasetPanel {

  public WaterColumnSonarDataWidget(
      DefaultController controller,
      DataWidgetModel model) {
    super("Water ColumnSonar Data", model, controller);
  }
}
