package edu.colorado.cires.cruisepack.prototype.dataset;

import edu.colorado.cires.cruisepack.prototype.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.prototype.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.prototype.ui.view.DatasetPanel;

public class NavigationDataWidget extends DatasetPanel {

  public NavigationDataWidget(
      DefaultController controller,
      DataWidgetModel model) {
    super("Navigation", model, controller);
  }
}
