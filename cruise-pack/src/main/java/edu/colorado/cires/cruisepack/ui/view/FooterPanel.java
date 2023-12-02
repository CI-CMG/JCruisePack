package edu.colorado.cires.cruisepack.ui.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class FooterPanel extends JPanel {

  FooterPanel() {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    add(new ButtonPanel(), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 1;
    add(new JLabel("Information Goes Here"), c);
  }

  private class ButtonPanel extends JPanel {
    ButtonPanel() {
      setLayout(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      add(new JButton("Help"), c);

      c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 1;
      add(new JButton("Hide Records"), c);

      c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 2;
      add(new JButton("Clear Form"), c);

      c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 3;
      add(new JButton("Stop Packaging"), c);

      c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 4;
      add(new JButton("Save For Later"), c);

      c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 5;
      add(new JButton("Package Data"), c);
    }
  }

}
