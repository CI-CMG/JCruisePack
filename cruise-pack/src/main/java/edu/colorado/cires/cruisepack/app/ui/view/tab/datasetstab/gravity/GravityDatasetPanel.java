package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.gravity;

import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_INSTRUMENT;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel.UPDATE_ARRIVAL_TIE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel.UPDATE_ARRIVAL_TIE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel.UPDATE_CORRECTION_MODEL;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel.UPDATE_CORRECTION_MODEL_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel.UPDATE_DEPARTURE_TIE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel.UPDATE_DEPARTURE_TIE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel.UPDATE_DRIFT_PER_DAY;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel.UPDATE_DRIFT_PER_DAY_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel.UPDATE_OBSERVATION_RATE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel.UPDATE_OBSERVATION_RATE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.dataset.GravityDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.GravityAdditionalFieldsModel;
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

public class GravityDatasetPanel extends AdditionalFieldsPanel<GravityAdditionalFieldsModel, GravityDatasetInstrumentController> {

  private static final String OBS_RATE_LABEL = "Observation Rate (obs/min)";
  private static final String DEPARTURE_TIE_LABEL = "Departure Tie (milligal)";
  private static final String ARRIVAL_TIE_LABEL = "Arrival Tie (milligal)";
  private static final String DRIFT_LABEL = "Drift Rate Per Day";
  private static final String CORRECTION_MODEL_LABEL = "Correction Model";
  private final LabeledComboBoxPanel correctionModelPanel = new LabeledComboBoxPanel(CORRECTION_MODEL_LABEL);
  private final LabeledTextFieldPanel obsRatePanel = new LabeledTextFieldPanel(OBS_RATE_LABEL);
  private final LabeledTextFieldPanel departureTiePanel = new LabeledTextFieldPanel(DEPARTURE_TIE_LABEL);
  private final LabeledTextFieldPanel arrivalTiePanel = new LabeledTextFieldPanel(ARRIVAL_TIE_LABEL);
  private final LabeledTextFieldPanel driftPanel = new LabeledTextFieldPanel(DRIFT_LABEL);
  private final List<DropDownItem> correctionModelOptions;

  protected GravityDatasetPanel(GravityAdditionalFieldsModel model, GravityDatasetInstrumentController controller,
      List<DropDownItem> correctionModelOptions) {
    super(model, controller);
    this.correctionModelOptions = correctionModelOptions;
  }


  @Override
  protected void initializeFields() {
    correctionModelPanel.getInstrumentField().setModel(new DefaultComboBoxModel<>(
        correctionModelOptions.toArray(new DropDownItem[0])
    ));
    correctionModelPanel.getInstrumentField().setSelectedItem(model.getCorrectionModel());
    obsRatePanel.getField().setText(model.getObservationRate());
    departureTiePanel.getField().setText(model.getDepartureTie());
    arrivalTiePanel.getField().setText(model.getArrivalTie());
    driftPanel.getField().setText(model.getDriftPerDay());
  }

  @Override
  protected void setupLayout() {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);

    JPanel row2 = new JPanel();
    row2.setLayout(new GridBagLayout());
    row2.add(correctionModelPanel, configureLayout(0, 0, c -> c.weightx = 3));
    row2.add(obsRatePanel, configureLayout(1, 0, c -> c.weightx = 1));
    add(row2, configureLayout(0, 0));

    JPanel row3 = new JPanel();
    row3.setLayout(new GridBagLayout());
    row3.add(departureTiePanel, configureLayout(0, 0));
    row3.add(arrivalTiePanel, configureLayout(1, 0));
    row3.add(driftPanel, configureLayout(2, 0));
    add(row3, configureLayout(0, 1));
  }

  @Override
  protected void setupMvc() {
    correctionModelPanel.getInstrumentField().addItemListener((evt) -> controller.setCorrectionModel((DropDownItem) evt.getItem()));
    obsRatePanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setObservationRate(obsRatePanel.getField().getText()));
    departureTiePanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setDepartureTie(departureTiePanel.getField().getText()));
    arrivalTiePanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setArrivalTie(arrivalTiePanel.getField().getText()));
    driftPanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setDriftPerDay(driftPanel.getField().getText()));
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case UPDATE_INSTRUMENT:
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
