package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.magnetics;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.CommentsTextAreaPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledComboBoxPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledTextFieldPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ProcessingLevelRadioPanel;
import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

class MagneticsDatasetContentPanel extends JPanel {

  private static final String SAMPLE_RATE_LABEL = "Sample Rate (obs/min)";
  private static final String TOW_DISTANCE_LABEL = "Tow Distance (m)";
  private static final String SENSOR_DEPTH = "Sensor Depth (m)";
  private static final String CORRECTION_MODEL_LABEL = "Correction Model";


  private final LabeledComboBoxPanel instrumentPanel = new LabeledComboBoxPanel();
  private final ProcessingLevelRadioPanel buttonPanel = new ProcessingLevelRadioPanel();
  private final CommentsTextAreaPanel commentsPanel = new CommentsTextAreaPanel();
  private final LabeledComboBoxPanel correctionModelPanel = new LabeledComboBoxPanel(CORRECTION_MODEL_LABEL);
  private final LabeledTextFieldPanel sampleRatePanel = new LabeledTextFieldPanel(SAMPLE_RATE_LABEL);
  private final LabeledTextFieldPanel towDistancePanel = new LabeledTextFieldPanel(TOW_DISTANCE_LABEL);
  private final LabeledTextFieldPanel sensorDepthPanel = new LabeledTextFieldPanel(SENSOR_DEPTH);



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
    row2.add(sampleRatePanel, configureLayout(1, 0, c -> c.weightx = 1));
    add(row2, configureLayout(0, 1));

    JPanel row3 = new JPanel();
    row3.setLayout(new GridBagLayout());
    row3.add(towDistancePanel, configureLayout(0, 0));
    row3.add(sensorDepthPanel, configureLayout(1, 0));
    add(row3, configureLayout(0, 2));

    add(commentsPanel, configureLayout(0, 3));

  }

  private void setupMvc() {

  }
}
