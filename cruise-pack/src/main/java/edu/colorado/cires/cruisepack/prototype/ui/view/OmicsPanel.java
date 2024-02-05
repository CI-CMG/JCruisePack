package edu.colorado.cires.cruisepack.prototype.ui.view;

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
