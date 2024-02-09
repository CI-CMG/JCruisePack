package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.gravity;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.CommentsTextAreaPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledComboBoxPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledTextFieldPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ProcessingLevelRadioPanel;
import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

class GravityDatasetContentPanel extends JPanel {

  private static final String OBS_RATE_LABEL = "Observation Rate (obs/min)";
  private static final String DEPARTURE_TIE_LABEL = "Departure Tie (milligal)";
  private static final String ARRIVAL_TIE_LABEL = "Arrival Tie (milligal)";
  private static final String DRIFT_LABEL = "Drift Rate Per Day";
  private static final String CORRECTION_MODEL_LABEL = "Correction Model";

  private final LabeledComboBoxPanel instrumentPanel = new LabeledComboBoxPanel();
  private final ProcessingLevelRadioPanel buttonPanel = new ProcessingLevelRadioPanel();
  private final CommentsTextAreaPanel commentsPanel = new CommentsTextAreaPanel();
  private final LabeledComboBoxPanel correctionModelPanel = new LabeledComboBoxPanel(CORRECTION_MODEL_LABEL);
  private final LabeledTextFieldPanel obsRatePanel = new LabeledTextFieldPanel(OBS_RATE_LABEL);
  private final LabeledTextFieldPanel departureTiePanel = new LabeledTextFieldPanel(DEPARTURE_TIE_LABEL);
  private final LabeledTextFieldPanel arrivalTiePanel = new LabeledTextFieldPanel(ARRIVAL_TIE_LABEL);
  private final LabeledTextFieldPanel driftPanel = new LabeledTextFieldPanel(DRIFT_LABEL);


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

    JPanel row2 = new JPanel();
    row2.setLayout(new GridBagLayout());
    row2.add(correctionModelPanel, configureLayout(0, 0, c -> c.weightx = 3));
    row2.add(obsRatePanel, configureLayout(1, 0, c -> c.weightx = 1));
    add(row2, configureLayout(0, 1));

    JPanel row3 = new JPanel();
    row3.setLayout(new GridBagLayout());
    row3.add(departureTiePanel, configureLayout(0, 0));
    row3.add(arrivalTiePanel, configureLayout(1, 0));
    row3.add(driftPanel, configureLayout(2, 0));
    add(row3, configureLayout(0, 2));

    add(commentsPanel, configureLayout(0, 3));

  }

  private void setupMvc() {

  }
}
