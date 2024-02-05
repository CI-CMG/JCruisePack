package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class DatasetContentCommentRowPanel extends JPanel {

  private static final String ADD_COMMENT_LABEL = "Add Data Comment";

  private final JTextArea commentField = new JTextArea();
  private final JButton addCommentButton = new JButton(ADD_COMMENT_LABEL);

  public void init() {
    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    add(addCommentButton);
    add(commentField);
  }
}
