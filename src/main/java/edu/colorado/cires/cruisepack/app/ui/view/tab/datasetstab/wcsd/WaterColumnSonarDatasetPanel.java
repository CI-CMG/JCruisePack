package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.wcsd;

import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_DATA_PATH;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_DATA_PATH_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_DATE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_DATE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_REPORT_PATH;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_REPORT_PATH_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_STATE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel.UPDATE_CALIBRATION_STATE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateDatePicker;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updatePathField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.dataset.WaterColumnSonarDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.SimpleDocumentListener;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.AdditionalFieldsPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledComboBoxPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledDatePickerPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledFilePathPanel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.apache.commons.lang3.StringUtils;

public class WaterColumnSonarDatasetPanel extends AdditionalFieldsPanel<WaterColumnAdditionalFieldsModel, WaterColumnSonarDatasetInstrumentController> {

  private static final String CALIBRATION_STATE_LABEL = "Calibration State";
  private static final String CALIBRATION_REPORT_PATH_LABEL = "Calibration Report Path";
  private static final String CALIBRATION_DATA_PATH_LABEL = "Calibration Data / Support Files Path";
  private static final String DOWNLOAD_BUTTON_LABEL = "Download Calibration Excel Template";
  private static final String CALIBRATION_DATE_LABEL = "Calibration Date";

  private final LabeledComboBoxPanel calibrationStatePanel = new LabeledComboBoxPanel(CALIBRATION_STATE_LABEL);
  private final LabeledFilePathPanel calibrationReportPathPanel = new LabeledFilePathPanel(CALIBRATION_REPORT_PATH_LABEL);
  private final LabeledFilePathPanel calibrationDataPathPanel = new LabeledFilePathPanel(CALIBRATION_DATA_PATH_LABEL);
  private final JButton calibrationTemplateDownloadButton = new JButton(DOWNLOAD_BUTTON_LABEL);
  private final LabeledDatePickerPanel calibrationDatePanel = new LabeledDatePickerPanel(CALIBRATION_DATE_LABEL);
  private final List<DropDownItem> calibrationStateDropDownOptions;

  protected WaterColumnSonarDatasetPanel(WaterColumnAdditionalFieldsModel model, WaterColumnSonarDatasetInstrumentController controller,
      List<DropDownItem> calibrationStateDropDownOptions) {
    super(model, controller);
    this.calibrationStateDropDownOptions = calibrationStateDropDownOptions;
  }

  @Override
  protected void initializeFields() {
    calibrationStatePanel.getInstrumentField().setModel(new DefaultComboBoxModel<>(
        calibrationStateDropDownOptions.toArray(new DropDownItem[0])
    ));
    calibrationStatePanel.getInstrumentField().setSelectedItem(model.getCalibrationState());
    calibrationReportPathPanel.getTextField().setText(model.getCalibrationReportPath() == null ? null : model.getCalibrationReportPath().toAbsolutePath().normalize().toString());
    calibrationDataPathPanel.getTextField().setText(model.getCalibrationDataPath() == null ? null : model.getCalibrationDataPath().toAbsolutePath().normalize().toString());
    calibrationDatePanel.getDatePicker().setDate(model.getCalibrationDate());
  }

  @Override
  protected void setupLayout() {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);

    JPanel row2 = new JPanel();
    row2.setLayout(new GridBagLayout());
    row2.add(calibrationStatePanel, configureLayout(0, 0, c -> c.weightx = 3));
    row2.add(calibrationDatePanel, configureLayout(1, 0, c -> c.weightx = 1));
    add(row2, configureLayout(0, 0));

    JPanel row3 = new JPanel();
    row3.setLayout(new GridBagLayout());
    row3.add(calibrationTemplateDownloadButton, configureLayout(0, 0, c -> { c.weightx = 0; c.fill = GridBagConstraints.HORIZONTAL; }));
    row3.add(calibrationReportPathPanel, configureLayout(1, 0));
    row3.add(calibrationDataPathPanel, configureLayout(2, 0));
    add(row3, configureLayout(0, 1));
  }

  @Override
  protected void setupMvc() {
    calibrationStatePanel.getInstrumentField().addItemListener((evt) -> controller.setCalibrationState((DropDownItem) evt.getItem()));
    calibrationReportPathPanel.getTextField().getDocument().addDocumentListener((SimpleDocumentListener)(evt) -> handleCalibrationReportPath(calibrationReportPathPanel.getTextField().getText()));
    calibrationDataPathPanel.getTextField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> handleCalibrationDataPath(calibrationDataPathPanel.getTextField().getText()));
    calibrationDatePanel.getDatePicker().addDateChangeListener((evt) -> controller.setCalibrationDate(calibrationDatePanel.getDatePicker().getDate()));
    calibrationTemplateDownloadButton.addActionListener((evt) -> handleDirectorySelect(controller::saveTemplate));
  }

  private void handleDirectorySelect(Consumer<Path> consumer) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
      consumer.accept(fileChooser.getSelectedFile().toPath().toAbsolutePath().normalize());
    }
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
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
}
