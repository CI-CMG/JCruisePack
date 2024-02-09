package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.wcsd;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledDatePickerPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.CommentsTextAreaPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledComboBoxPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledFilePathPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ProcessingLevelRadioPanel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

class WaterColumnSonarDatasetContentPanel extends JPanel {

  private static final String CALIBRATION_STATE_LABEL = "Calibration State";
  private static final String CALIBRATION_REPORT_PATH_LABEL = "Calibration Report Path";
  private static final String CALIBRATION_DATA_PATH_LABEL = "Calibration Data / Support Files Path";
  private static final String DOWNLOAD_BUTTON_LABEL = "Download Calibration Excel Template";
  private static final String CALIBRATION_DATE_LABEL = "Calibration Date";


  private final LabeledComboBoxPanel instrumentPanel = new LabeledComboBoxPanel();
  private final ProcessingLevelRadioPanel buttonPanel = new ProcessingLevelRadioPanel();
  private final CommentsTextAreaPanel commentsPanel = new CommentsTextAreaPanel();
  private final LabeledComboBoxPanel calibrationStatePanel = new LabeledComboBoxPanel(CALIBRATION_STATE_LABEL);
  private final LabeledFilePathPanel calibrationReportPathPanel = new LabeledFilePathPanel(CALIBRATION_REPORT_PATH_LABEL);
  private final LabeledFilePathPanel calibrationDataPathPanel = new LabeledFilePathPanel(CALIBRATION_DATA_PATH_LABEL);
  private final JButton calibrationTemplateDownloadButton = new JButton(DOWNLOAD_BUTTON_LABEL);
  private final LabeledDatePickerPanel calibrationDatePanel = new LabeledDatePickerPanel(CALIBRATION_DATE_LABEL);


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
    row2.add(calibrationStatePanel, configureLayout(0, 0, c -> c.weightx = 3));
    row2.add(calibrationDatePanel, configureLayout(1, 0, c -> c.weightx = 1));
    add(row2, configureLayout(0, 1));

    JPanel row3 = new JPanel();
    row3.setLayout(new GridBagLayout());
    row3.add(calibrationTemplateDownloadButton, configureLayout(0, 0, c -> { c.weightx = 0; c.fill = GridBagConstraints.HORIZONTAL; }));
    row3.add(calibrationReportPathPanel, configureLayout(1, 0));
    row3.add(calibrationDataPathPanel, configureLayout(2, 0));
    add(row3, configureLayout(0, 2));

    add(commentsPanel, configureLayout(0, 3));

  }

  private void setupMvc() {

  }
}
