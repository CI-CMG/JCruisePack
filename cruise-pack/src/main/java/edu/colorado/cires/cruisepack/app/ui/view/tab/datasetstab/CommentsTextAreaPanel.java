package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CommentsTextAreaPanel extends JPanel {

  private static final String COMMENTS_LABEL = "Comments";

  private final JTextArea commentsField = new JTextArea();

  public CommentsTextAreaPanel() {
    this(COMMENTS_LABEL);
  }

  public CommentsTextAreaPanel(String title) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createTitledBorder(title));
    add(commentsField);
  }
}
