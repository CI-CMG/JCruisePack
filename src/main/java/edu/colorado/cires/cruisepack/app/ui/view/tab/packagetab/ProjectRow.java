package edu.colorado.cires.cruisepack.app.ui.view.tab.packagetab;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class ProjectRow extends JPanel {

  private final ProjectChooserPanel parent;

  private JButton removeButton = new JButton("Remove");
  private JComboBox<String> projectsList = new JComboBox<>();

  public ProjectRow(ProjectChooserPanel parent) {
    this.parent = parent;
  }

  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void initializeFields() {
    projectsList.setEditable(true);
  }

  private void setupLayout() {
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createEtchedBorder());
    add(removeButton);
    add(projectsList);
  }

  private void setupMvc() {
    removeButton.addActionListener((evt) -> parent.removeRow(this));
  }

}
