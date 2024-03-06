package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.magnetics;

import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_CORRECTION_MODEL;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_CORRECTION_MODEL_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_SAMPLE_RATE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_SAMPLE_RATE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_SENSOR_DEPTH;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_SENSOR_DEPTH_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_TOW_DISTANCE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_TOW_DISTANCE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.dataset.MagneticsDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.SimpleDocumentListener;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.AdditionalFieldsPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledComboBoxPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledTextFieldPanel;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;


public class MagneticsDatasetPanel extends AdditionalFieldsPanel<MagneticsAdditionalFieldsModel, MagneticsDatasetInstrumentController> {

  private static final String SAMPLE_RATE_LABEL = "Sample Rate (obs/min)";
  private static final String TOW_DISTANCE_LABEL = "Tow Distance (m)";
  private static final String SENSOR_DEPTH = "Sensor Depth (m)";
  private static final String CORRECTION_MODEL_LABEL = "Correction Model";
  
  private final LabeledComboBoxPanel correctionModelPanel = new LabeledComboBoxPanel(CORRECTION_MODEL_LABEL);
  private final LabeledTextFieldPanel sampleRatePanel = new LabeledTextFieldPanel(SAMPLE_RATE_LABEL);
  private final LabeledTextFieldPanel towDistancePanel = new LabeledTextFieldPanel(TOW_DISTANCE_LABEL);
  private final LabeledTextFieldPanel sensorDepthPanel = new LabeledTextFieldPanel(SENSOR_DEPTH);
  private final List<DropDownItem> correctionModelOptions;

  protected MagneticsDatasetPanel(MagneticsAdditionalFieldsModel model, MagneticsDatasetInstrumentController controller,
      List<DropDownItem> correctionModelOptions) {
    super(model, controller);
    this.correctionModelOptions = correctionModelOptions;
  }

  @Override
  public void init() {
    super.init();
  }

  @Override
  protected void initializeFields() {
    correctionModelPanel.getInstrumentField().setModel(new DefaultComboBoxModel<>(
        correctionModelOptions.toArray(new DropDownItem[0])
    ));
    correctionModelPanel.getInstrumentField().setSelectedItem(model.getCorrectionModel());
    sampleRatePanel.getField().setText(model.getSampleRate());
    towDistancePanel.getField().setText(model.getTowDistance());
    sensorDepthPanel.getField().setText(model.getSensorDepth());
  }

  @Override
  protected void setupLayout() {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);

    JPanel row1 = new JPanel();
    row1.setLayout(new GridBagLayout());
    row1.add(correctionModelPanel, configureLayout(0, 0, c -> c.weightx = 3));
    row1.add(sampleRatePanel, configureLayout(1, 0, c -> c.weightx = 1));
    add(row1, configureLayout(0, 0));

    JPanel row2 = new JPanel();
    row2.setLayout(new GridBagLayout());
    row2.add(towDistancePanel, configureLayout(0, 0));
    row2.add(sensorDepthPanel, configureLayout(1, 0));
    add(row2, configureLayout(0, 1));
  }

  @Override
  protected void setupMvc() {
    correctionModelPanel.getInstrumentField().addItemListener((evt) -> controller.setCorrectionModel((DropDownItem) evt.getItem()));
    sampleRatePanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setSampleRate(sampleRatePanel.getField().getText()));
    towDistancePanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setTowDistance(towDistancePanel.getField().getText()));
    sensorDepthPanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setSensorDepth(sensorDepthPanel.getField().getText()));
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case UPDATE_CORRECTION_MODEL:
        updateComboBox(correctionModelPanel.getInstrumentField(), evt);
        break;
      case UPDATE_SAMPLE_RATE:
        updateTextField(sampleRatePanel.getField(), evt);
        break;
      case UPDATE_TOW_DISTANCE:
        updateTextField(towDistancePanel.getField(), evt);
        break;
      case UPDATE_SENSOR_DEPTH:
        updateTextField(sensorDepthPanel.getField(), evt);
        break;
      case UPDATE_CORRECTION_MODEL_ERROR:
        updateLabelText(correctionModelPanel.getErrorLabel(), evt);
        break;
      case UPDATE_SAMPLE_RATE_ERROR:
        updateLabelText(sampleRatePanel.getErrorLabel(), evt);
        break;
      case UPDATE_TOW_DISTANCE_ERROR:
        updateLabelText(towDistancePanel.getErrorLabel(), evt);
        break;
      case UPDATE_SENSOR_DEPTH_ERROR:
        updateLabelText(sensorDepthPanel.getErrorLabel(), evt);
        break;
      default:
        break;
    }
  }
}
