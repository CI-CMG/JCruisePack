package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.magnetics;

import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_COMMENTS;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_COMMENTS_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_INSTRUMENT;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_INSTRUMENT_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_CORRECTION_MODEL;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_CORRECTION_MODEL_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_SAMPLE_RATE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_SAMPLE_RATE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_SENSOR_DEPTH;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_SENSOR_DEPTH_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_TOW_DISTANCE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel.UPDATE_TOW_DISTANCE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.setSelectedButton;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateRadioButtonGroup;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.MagneticsDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.MagneticsAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.SimpleDocumentListener;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.CommentsTextAreaPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledComboBoxPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledTextFieldPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ProcessingLevelRadioPanel;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;


public class MagneticsDatasetPanel extends DatasetPanel<BaseDatasetInstrumentModel<MagneticsAdditionalFieldsModel>, MagneticsDatasetInstrumentController> {

  public static final String INSTRUMENT_SHORT_CODE = "MAG";

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
  private final List<DropDownItem> correctionModelOptions;


  public MagneticsDatasetPanel(BaseDatasetInstrumentModel<MagneticsAdditionalFieldsModel> model, MagneticsDatasetInstrumentController controller, InstrumentDatastore instrumentDatastore, List<DropDownItem> correctionModelOptions) {
    super(model, controller, instrumentDatastore);
    this.correctionModelOptions = correctionModelOptions;
  }

  @Override
  public void init() {
    super.init();
    instrumentPanel.getInstrumentField().setModel(new DefaultComboBoxModel<>(instrumentDatastore.getInstrumentDropDownsForDatasetType(INSTRUMENT_SHORT_CODE).toArray(new DropDownItem[0])));
    instrumentPanel.getInstrumentField().setSelectedItem(model.getInstrument());
    setSelectedButton(buttonPanel.getProcessingLevelGroup(), model.getProcessingLevel());
    commentsPanel.getCommentsField().setText(model.getComments());
    correctionModelPanel.getInstrumentField().setModel(new DefaultComboBoxModel<>(
      correctionModelOptions.toArray(new DropDownItem[0])
    ));
    correctionModelPanel.getInstrumentField().setSelectedItem(model.getAdditionalFieldsModel().getCorrectionModel());
    sampleRatePanel.getField().setText(model.getAdditionalFieldsModel().getSampleRate());
    towDistancePanel.getField().setText(model.getAdditionalFieldsModel().getTowDistance());
    sensorDepthPanel.getField().setText(model.getAdditionalFieldsModel().getSensorDepth());

    instrumentPanel.getInstrumentField().addItemListener((evt) -> controller.setInstrument((DropDownItem) evt.getItem()));
    buttonPanel.addActionListener((evt) -> controller.setProcessingLevel(buttonPanel.getSelectedButtonText()));
    commentsPanel.getCommentsField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setComments(commentsPanel.getCommentsField().getText()));
    correctionModelPanel.getInstrumentField().addItemListener((evt) -> controller.setCorrectionModel((DropDownItem) evt.getItem()));
    sampleRatePanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setSampleRate(sampleRatePanel.getField().getText()));
    towDistancePanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setTowDistance(towDistancePanel.getField().getText()));
    sensorDepthPanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setSensorDepth(sensorDepthPanel.getField().getText()));
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
    row2.add(correctionModelPanel, configureLayout(0, 0, c -> c.weightx = 3));
    row2.add(sampleRatePanel, configureLayout(1, 0, c -> c.weightx = 1));
    panel.add(row2, configureLayout(0, 1));

    JPanel row3 = new JPanel();
    row3.setLayout(new GridBagLayout());
    row3.add(towDistancePanel, configureLayout(0, 0));
    row3.add(sensorDepthPanel, configureLayout(1, 0));
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
      case UPDATE_INSTRUMENT_ERROR:
        updateLabelText(instrumentPanel.getErrorLabel(), evt);
        break;
      case UPDATE_PROCESSING_LEVEL_ERROR:
        updateLabelText(buttonPanel.getErrorLabel(), evt);
        break;
      case UPDATE_COMMENTS_ERROR:
        updateLabelText(commentsPanel.getErrorLabel(), evt);
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
