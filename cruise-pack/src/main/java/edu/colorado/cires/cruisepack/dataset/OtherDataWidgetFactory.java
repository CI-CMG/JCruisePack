package edu.colorado.cires.cruisepack.dataset;

import edu.colorado.cires.cruisepack.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.ui.view.DatasetPanel;

public class OtherDataWidgetFactory implements Dataset{

  @Override
  public String getDataType() {
    return "Other";
  }

  @Override
  public DatasetPanel initialize(DataWidgetModel model, DefaultController controller) {
    return new OtherDataWidget(controller, model);
  }
}
