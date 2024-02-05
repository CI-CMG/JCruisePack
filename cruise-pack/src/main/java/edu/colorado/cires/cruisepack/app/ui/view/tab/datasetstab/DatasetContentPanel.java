package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class DatasetContentPanel extends JPanel {

  private final DatasetContentTypeRowPanel row1 = new DatasetContentTypeRowPanel();
  private final DatasetContentButtonRowPanel row2 = new DatasetContentButtonRowPanel();
  private final DatasetContentCommentRowPanel row3 = new DatasetContentCommentRowPanel();

  public void init() {
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    add(row1);
    add(row2);
    add(row3);
  }
}
