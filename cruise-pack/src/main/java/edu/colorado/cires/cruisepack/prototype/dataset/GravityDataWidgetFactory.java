package edu.colorado.cires.cruisepack.prototype.dataset;

import edu.colorado.cires.cruisepack.prototype.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.prototype.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.prototype.ui.view.DatasetPanel;

public class GravityDataWidgetFactory implements Dataset {

  @Override
  public String getDataType() {
    return "Gravity";
  }

  @Override
  public DatasetPanel initialize(DataWidgetModel model, DefaultController controller) {
    return new GravityDataWidget(model, controller);
  }
}
