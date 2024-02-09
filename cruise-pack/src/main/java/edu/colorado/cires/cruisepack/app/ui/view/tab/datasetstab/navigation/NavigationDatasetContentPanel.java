package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.navigation;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.CommentsTextAreaPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledComboBoxPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ProcessingLevelRadioPanel;
import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

class NavigationDatasetContentPanel extends JPanel {

  private static final String NAV_DATUM_LABEL = "Navigation Datum";

  private final LabeledComboBoxPanel instrumentPanel = new LabeledComboBoxPanel();
  private final ProcessingLevelRadioPanel buttonPanel = new ProcessingLevelRadioPanel();
  private final CommentsTextAreaPanel commentsPanel = new CommentsTextAreaPanel();
  private final LabeledComboBoxPanel navDatumPanel = new LabeledComboBoxPanel(NAV_DATUM_LABEL);

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

    JPanel row1 = new JPanel();
    row1.setLayout(new GridBagLayout());
    row1.setBackground(Color.WHITE);
    row1.add(instrumentPanel, configureLayout(0, 0));
    row1.add(buttonPanel, configureLayout(1, 0, c -> c.weightx = 0));

    add(row1, configureLayout(0, 0));
    add(navDatumPanel, configureLayout(0, 1));
    add(commentsPanel, configureLayout(0, 2));

  }

  private void setupMvc() {

  }
}
