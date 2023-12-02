package edu.colorado.cires.cruisepack.ui.view;

import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

class MultiComboBoxPanel extends JPanel {

  MultiComboBoxPanel() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBackground(Color.WHITE);
  }

  void addChooser() {
    JComboBox projectList = new JComboBox(new String[] { "Test Project" });
    projectList.setSelectedIndex(0);
    add(projectList);
  }
}
