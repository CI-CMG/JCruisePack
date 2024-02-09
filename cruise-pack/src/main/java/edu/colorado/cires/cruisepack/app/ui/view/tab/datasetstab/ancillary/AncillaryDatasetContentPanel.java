package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ancillary;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.CommentsTextAreaPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledComboBoxPanel;
import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

class AncillaryDatasetContentPanel extends JPanel {

  private static final String INSTRUMENT_TITLE = "Ancillary Data Type";
  private static final String COMMENTS_TITLE = "Ancillary Data Details";

  private final LabeledComboBoxPanel instrumentPanel = new LabeledComboBoxPanel(INSTRUMENT_TITLE);
  private final CommentsTextAreaPanel commentsPanel = new CommentsTextAreaPanel(COMMENTS_TITLE);

  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void initializeFields() {

  }

  private void setupLayout() {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);
    add(instrumentPanel, configureLayout(0, 0));
    add(commentsPanel, configureLayout(0, 1));
  }

  private void setupMvc() {

  }
}
