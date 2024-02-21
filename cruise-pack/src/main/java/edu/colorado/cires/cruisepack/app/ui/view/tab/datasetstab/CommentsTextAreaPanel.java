package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CommentsTextAreaPanel extends JPanel {

  private static final String COMMENTS_LABEL = "Comments";

  private final JTextArea commentsField = new JTextArea();
  private final JLabel errorLabel = createErrorLabel();

  public CommentsTextAreaPanel() {
    this(COMMENTS_LABEL);
  }

  public CommentsTextAreaPanel(String title) {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createTitledBorder(title));
    add(errorLabel, configureLayout(0, 0));
    add(commentsField, configureLayout(0, 1));
  }

  public JLabel getErrorLabel() {
    return errorLabel;
  }

  public JTextArea getCommentsField() {
    return commentsField;
  }
}
