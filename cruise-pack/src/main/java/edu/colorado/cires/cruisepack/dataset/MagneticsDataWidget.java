package edu.colorado.cires.cruisepack.dataset;

import edu.colorado.cires.cruisepack.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.ui.view.DatasetPanel;
import java.util.List;

public class MagneticsDataWidget extends DatasetPanel {

  public MagneticsDataWidget(
      DefaultController controller,
      DataWidgetModel model) {
    super("Magnetics", model, controller);
  }
}
