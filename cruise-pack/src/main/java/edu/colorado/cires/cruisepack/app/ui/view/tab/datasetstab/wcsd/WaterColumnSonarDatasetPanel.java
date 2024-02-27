package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.wcsd;

import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel.UPDATE_CALIBRATION_DATA_PATH;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel.UPDATE_CALIBRATION_DATA_PATH_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel.UPDATE_CALIBRATION_DATE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel.UPDATE_CALIBRATION_DATE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel.UPDATE_CALIBRATION_REPORT_PATH;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel.UPDATE_CALIBRATION_REPORT_PATH_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel.UPDATE_CALIBRATION_STATE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel.UPDATE_CALIBRATION_STATE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel.UPDATE_COMMENTS;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel.UPDATE_COMMENTS_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel.UPDATE_INSTRUMENT;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel.UPDATE_INSTRUMENT_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.setSelectedButton;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateDatePicker;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updatePathField;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateRadioButtonGroup;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.WaterColumnSonarDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnSonarDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.SimpleDocumentListener;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.CommentsTextAreaPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledComboBoxPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledDatePickerPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledFilePathPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ProcessingLevelRadioPanel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.apache.commons.lang3.StringUtils;

public class WaterColumnSonarDatasetPanel extends DatasetPanel<WaterColumnSonarDatasetInstrumentModel, WaterColumnSonarDatasetInstrumentController> {

  public static final String INSTRUMENT_SHORT_CODE = "WCSD";

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
  private final List<DropDownItem> calibrationStateDropDownOptions;

  public WaterColumnSonarDatasetPanel(WaterColumnSonarDatasetInstrumentModel model, WaterColumnSonarDatasetInstrumentController controller, InstrumentDatastore instrumentDatastore, List<DropDownItem> calibrationStateDropDownOptions) {
    super(model, controller, instrumentDatastore);
    this.calibrationStateDropDownOptions = calibrationStateDropDownOptions;
  }

  @Override
  public void init() {
    super.init();
    instrumentPanel.getInstrumentField().setSelectedItem(model.getInstrument());
    instrumentPanel.getInstrumentField().setModel(new DefaultComboBoxModel<>(instrumentDatastore.getInstrumentDropDownsForDatasetType(INSTRUMENT_SHORT_CODE).toArray(new DropDownItem[0])));
    setSelectedButton(buttonPanel.getProcessingLevelGroup(), model.getProcessingLevel());
    commentsPanel.getCommentsField().setText(model.getComments());
    calibrationStatePanel.getInstrumentField().setSelectedItem(model.getCalibrationState());
    calibrationStatePanel.getInstrumentField().setModel(new DefaultComboBoxModel<>(
      calibrationStateDropDownOptions.toArray(new DropDownItem[0])
    ));
    calibrationReportPathPanel.getTextField().setText(model.getCalibrationReportPath() == null ? null : model.getCalibrationReportPath().toAbsolutePath().normalize().toString());
    calibrationDataPathPanel.getTextField().setText(model.getCalibrationDataPath() == null ? null : model.getCalibrationDataPath().toAbsolutePath().normalize().toString());
    calibrationDatePanel.getDatePicker().setDate(model.getCalibrationDate());

    instrumentPanel.getInstrumentField().addItemListener((evt) -> controller.setInstrument((DropDownItem) evt.getItem()));
    buttonPanel.addActionListener((evt) -> controller.setProcessingLevel(buttonPanel.getSelectedButtonText()));
    commentsPanel.getCommentsField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setComments(commentsPanel.getCommentsField().getText()));
    calibrationStatePanel.getInstrumentField().addItemListener((evt) -> controller.setCalibrationState((DropDownItem) evt.getItem()));
    calibrationReportPathPanel.getTextField().getDocument().addDocumentListener((SimpleDocumentListener)(evt) -> handleCalibrationReportPath(calibrationReportPathPanel.getTextField().getText()));
    calibrationDataPathPanel.getTextField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> handleCalibrationDataPath(calibrationDataPathPanel.getTextField().getText()));
    calibrationDatePanel.getDatePicker().addDateChangeListener((evt) -> controller.setCalibrationDate(calibrationDatePanel.getDatePicker().getDate()));
  }

  private void handleCalibrationReportPath(String value) {
    if (StringUtils.isBlank(value)) {
      controller.setCalibrationReportPath(null);
    } else {
      Path path = Paths.get(value);
      controller.setCalibrationReportPath(path.toAbsolutePath().normalize());
    }
  }

  private void handleCalibrationDataPath(String value) {
    if (StringUtils.isBlank(value)) {
      controller.setCalibrationDataPath(null);
    } else {
      Path path = Paths.get(value);
      controller.setCalibrationDataPath(path.toAbsolutePath().normalize());
    }
  }

  @Override
  protected String getInstrumentShortCode() {
    return INSTRUMENT_SHORT_CODE;
  }

  @Override
  protected JPanel createAndInitializeContentPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    panel.setBackground(Color.WHITE);

    JPanel row1 = new JPanel();
    row1.setLayout(new GridBagLayout());
    row1.setBackground(Color.WHITE);
    row1.add(instrumentPanel, configureLayout(0, 0));
    row1.add(buttonPanel, configureLayout(1, 0, c -> c.weightx = 0));
    panel.add(row1, configureLayout(0, 0));

    JPanel row2 = new JPanel();
    row2.setLayout(new GridBagLayout());
    row2.add(calibrationStatePanel, configureLayout(0, 0, c -> c.weightx = 3));
    row2.add(calibrationDatePanel, configureLayout(1, 0, c -> c.weightx = 1));
    panel.add(row2, configureLayout(0, 1));

    JPanel row3 = new JPanel();
    row3.setLayout(new GridBagLayout());
    row3.add(calibrationTemplateDownloadButton, configureLayout(0, 0, c -> { c.weightx = 0; c.fill = GridBagConstraints.HORIZONTAL; }));
    row3.add(calibrationReportPathPanel, configureLayout(1, 0));
    row3.add(calibrationDataPathPanel, configureLayout(2, 0));
    panel.add(row3, configureLayout(0, 2));

    panel.add(commentsPanel, configureLayout(0, 3));
    return panel;
  }

  @Override
  protected void customOnChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case UPDATE_INSTRUMENT:
        updateComboBox(instrumentPanel.getInstrumentField(), evt);
        break;
      case UPDATE_PROCESSING_LEVEL:
        updateRadioButtonGroup(buttonPanel.getProcessingLevelGroup(), evt);
        break;
      case UPDATE_COMMENTS:
        updateTextField(commentsPanel.getCommentsField(), evt);
        break;
      case UPDATE_CALIBRATION_STATE:
        updateComboBox(calibrationStatePanel.getInstrumentField(), evt);
        break;
      case UPDATE_CALIBRATION_REPORT_PATH:
        updatePathField(calibrationReportPathPanel.getTextField(), evt);
        break;
      case UPDATE_CALIBRATION_DATA_PATH:
        updatePathField(calibrationDataPathPanel.getTextField(), evt);
        break;
      case UPDATE_CALIBRATION_DATE:
        updateDatePicker(calibrationDatePanel.getDatePicker(), evt);
        break;
      case UPDATE_INSTRUMENT_ERROR:
        updateLabelText(instrumentPanel.getErrorLabel(), evt);
        break;
      case UPDATE_PROCESSING_LEVEL_ERROR:
        updateLabelText(buttonPanel.getErrorLabel(), evt);
        break;
      case UPDATE_COMMENTS_ERROR:
        updateLabelText(commentsPanel.getErrorLabel(), evt);
        break;
      case UPDATE_CALIBRATION_STATE_ERROR:
        updateLabelText(calibrationStatePanel.getErrorLabel(), evt);
        break;
      case UPDATE_CALIBRATION_REPORT_PATH_ERROR:
        updateLabelText(calibrationReportPathPanel.getErrorLabel(), evt);
        break;
      case UPDATE_CALIBRATION_DATA_PATH_ERROR:
        updateLabelText(calibrationDataPathPanel.getErrorLabel(), evt);
        break;
      case UPDATE_CALIBRATION_DATE_ERROR:
        updateLabelText(calibrationDatePanel.getErrorLabel(), evt);
        break;
      default:
        break;
    }
  }
}
