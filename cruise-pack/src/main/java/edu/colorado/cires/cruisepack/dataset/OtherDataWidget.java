package edu.colorado.cires.cruisepack.dataset;

import edu.colorado.cires.cruisepack.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.ui.view.DatasetPanel;
import java.util.List;

public class OtherDataWidget extends DatasetPanel {

  public OtherDataWidget(
      DefaultController controller,
      DataWidgetModel model) {
    super("Other", model, controller);
  }
}
