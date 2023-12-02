package edu.colorado.cires.cruisepack.dataset;

import edu.colorado.cires.cruisepack.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.ui.view.DatasetPanel;
import java.util.List;

public class WaterColumnSonarDataWidget extends DatasetPanel {

  public WaterColumnSonarDataWidget(
      DefaultController controller,
      DataWidgetModel model) {
    super("Water ColumnSonar Data", model, controller);
  }
}
