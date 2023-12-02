package edu.colorado.cires.cruisepack.ui.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class CruiseInformationPanel extends JPanel implements BaseViewPanel {
  CruiseInformationPanel() {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridy = 0;
    add(new JLabel("Cruise Title"), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridy = 1;
    add(new JTextField(), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridy = 2;
    add(new JLabel("Cruise Purpose"), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridy = 3;
    c.ipady = 200;
    add(new JTextArea(), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridy = 4;
    add(new JLabel("Cruise Description (overview of cruise-level metadata)"), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridy = 5;
    c.ipady = 200;
    add(new JTextArea(), c);

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridy = 6;
    add(new DocumentsPanel(), c);
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {

  }

  private class DocumentsPanel extends JPanel {
    private DocumentsPanel() {
      setLayout(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      add(new JLabel("Documents Path"), c);

      c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 1;
      add(new JTextField(), c);

      c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 2;
      add(new JButton("Select Directory"), c);
    }
  }
}
