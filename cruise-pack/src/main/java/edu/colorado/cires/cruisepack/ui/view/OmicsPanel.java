package edu.colorado.cires.cruisepack.ui.view;

import edu.colorado.cires.cruisepack.ui.controller.DefaultController;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.JPanel;

class OmicsPanel extends JPanel implements BaseViewPanel {

  OmicsPanel() {
    setLayout(new GridBagLayout());
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {

  }

}
