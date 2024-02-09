package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.singebeam;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.CommentsTextAreaPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledComboBoxPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledTextFieldPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ProcessingLevelRadioPanel;
import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

class SinglebeamDatasetContentPanel extends JPanel {

  private static final String VERTICAL_DATUM_LABEL = "Vertical Datum";
  private static final String OBS_RATE_LABEL = "Observation Rate (obs/min)";
  private static final String SOUND_VELOCITY_LABEL = "Sound Velocity (m/s)";


  private final LabeledComboBoxPanel instrumentPanel = new LabeledComboBoxPanel();
  private final ProcessingLevelRadioPanel buttonPanel = new ProcessingLevelRadioPanel();
  private final CommentsTextAreaPanel commentsPanel = new CommentsTextAreaPanel();
  private final LabeledComboBoxPanel verticalDatumPanel = new LabeledComboBoxPanel(VERTICAL_DATUM_LABEL);
  private final LabeledTextFieldPanel obsRatePanel = new LabeledTextFieldPanel(OBS_RATE_LABEL);
  private final LabeledTextFieldPanel soundVelocityPanel = new LabeledTextFieldPanel(SOUND_VELOCITY_LABEL);


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
    row2.add(verticalDatumPanel, configureLayout(0, 0));
    row2.add(obsRatePanel, configureLayout(1, 0));
    row2.add(soundVelocityPanel, configureLayout(2, 0));
    add(row2, configureLayout(0, 1));

    add(commentsPanel, configureLayout(0, 2));

  }

  private void setupMvc() {

  }
}
