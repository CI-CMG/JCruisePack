package edu.colorado.cires.cruisepack.dataset;

import edu.colorado.cires.cruisepack.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.ui.view.DatasetPanel;
import java.util.List;

public class MultibeamBathymetryDataWidget extends DatasetPanel {

  public MultibeamBathymetryDataWidget(
      DefaultController controller,
      DataWidgetModel model) {
    super("Multibeam Bathymetry", model, controller);
  }
}
