package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.gravity;

import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_ARRIVAL_TIE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_ARRIVAL_TIE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_COMMENTS;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_COMMENTS_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_CORRECTION_MODEL;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_CORRECTION_MODEL_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_DEPARTURE_TIE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_DEPARTURE_TIE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_DRIFT_PER_DAY;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_DRIFT_PER_DAY_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_INSTRUMENT;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_INSTRUMENT_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_OBSERVATION_RATE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_OBSERVATION_RATE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.setSelectedButton;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateRadioButtonGroup;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.GravityDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityDatasetInstrumentModel;
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
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

public class GravityDatasetPanel extends DatasetPanel<GravityDatasetInstrumentModel, GravityDatasetInstrumentController> {

  public static final String INSTRUMENT_SHORT_CODE = "GRAV";

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
  private final List<DropDownItem> correctionModelOptions;

  public GravityDatasetPanel(GravityDatasetInstrumentModel model, GravityDatasetInstrumentController controller,
      InstrumentDatastore instrumentDatastore, List<DropDownItem> correctionModelOptions) {
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
    correctionModelPanel.getInstrumentField().setSelectedItem(model.getCorrectionModel());
    obsRatePanel.getField().setText(model.getObservationRate());
    departureTiePanel.getField().setText(model.getDepartureTie());
    arrivalTiePanel.getField().setText(model.getArrivalTie());
    driftPanel.getField().setText(model.getDriftPerDay());

    instrumentPanel.getInstrumentField().addItemListener((evt) -> controller.setInstrument((DropDownItem) evt.getItem()));
    buttonPanel.addActionListener((evt) -> controller.setProcessingLevel(buttonPanel.getSelectedButtonText()));
    commentsPanel.getCommentsField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setComments(commentsPanel.getCommentsField().getText()));
    correctionModelPanel.getInstrumentField().addItemListener((evt) -> controller.setCorrectionModel((DropDownItem) evt.getItem()));
    obsRatePanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setObservationRate(obsRatePanel.getField().getText()));
    departureTiePanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setDepartureTie(departureTiePanel.getField().getText()));
    arrivalTiePanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setArrivalTie(arrivalTiePanel.getField().getText()));
    driftPanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setDriftPerDay(driftPanel.getField().getText()));

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
    row2.add(obsRatePanel, configureLayout(1, 0, c -> c.weightx = 1));
    panel.add(row2, configureLayout(0, 1));

    JPanel row3 = new JPanel();
    row3.setLayout(new GridBagLayout());
    row3.add(departureTiePanel, configureLayout(0, 0));
    row3.add(arrivalTiePanel, configureLayout(1, 0));
    row3.add(driftPanel, configureLayout(2, 0));
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
      case UPDATE_DEPARTURE_TIE:
        updateTextField(departureTiePanel.getField(), evt);
        break;
      case UPDATE_ARRIVAL_TIE:
        updateTextField(arrivalTiePanel.getField(), evt);
        break;
      case UPDATE_OBSERVATION_RATE:
        updateTextField(obsRatePanel.getField(), evt);
        break;
      case UPDATE_DRIFT_PER_DAY:
        updateTextField(driftPanel.getField(), evt);
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
      case UPDATE_OBSERVATION_RATE_ERROR:
        updateLabelText(obsRatePanel.getErrorLabel(), evt);
        break;
      case UPDATE_DEPARTURE_TIE_ERROR:
        updateLabelText(departureTiePanel.getErrorLabel(), evt);
        break;
      case UPDATE_ARRIVAL_TIE_ERROR:
        updateLabelText(arrivalTiePanel.getErrorLabel(), evt);
        break;
      case UPDATE_DRIFT_PER_DAY_ERROR:
        updateLabelText(driftPanel.getErrorLabel(), evt);
        break;
      default:
        break;
    }
  }
}
