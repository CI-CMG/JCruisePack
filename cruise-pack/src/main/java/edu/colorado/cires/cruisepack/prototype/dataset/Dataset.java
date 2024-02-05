package edu.colorado.cires.cruisepack.prototype.dataset;

import edu.colorado.cires.cruisepack.prototype.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.prototype.ui.model.DataWidgetModel;
import edu.colorado.cires.cruisepack.prototype.ui.view.DatasetPanel;
import java.util.Optional;
import javax.swing.JPanel;

public interface Dataset {

  String getDataType();

  DatasetPanel initialize(DataWidgetModel model, DefaultController controller);

  default Optional<JPanel> getCustomGui() {
    return Optional.empty();
  }

}
