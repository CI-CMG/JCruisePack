package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.singebeam;

import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamAdditionalFieldsModel.UPDATE_OBS_RATE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamAdditionalFieldsModel.UPDATE_OBS_RATE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamAdditionalFieldsModel.UPDATE_SOUND_VELOCITY;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamAdditionalFieldsModel.UPDATE_SOUND_VELOCITY_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamAdditionalFieldsModel.UPDATE_VERTICAL_DATUM;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamAdditionalFieldsModel.UPDATE_VERTICAL_DATUM_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.dataset.SinglebeamDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamAdditionalFieldsModel;
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

public class SinglebeamDatasetPanel extends AdditionalFieldsPanel<SinglebeamAdditionalFieldsModel, SinglebeamDatasetInstrumentController> {

  private static final String VERTICAL_DATUM_LABEL = "Vertical Datum";
  private static final String OBS_RATE_LABEL = "Observation Rate (obs/min)";
  private static final String SOUND_VELOCITY_LABEL = "Sound Velocity (m/s)";

  private final LabeledComboBoxPanel verticalDatumPanel = new LabeledComboBoxPanel(VERTICAL_DATUM_LABEL);
  private final LabeledTextFieldPanel obsRatePanel = new LabeledTextFieldPanel(OBS_RATE_LABEL);
  private final LabeledTextFieldPanel soundVelocityPanel = new LabeledTextFieldPanel(SOUND_VELOCITY_LABEL);
  private final List<DropDownItem> verticalDatumOptions;

  protected SinglebeamDatasetPanel(SinglebeamAdditionalFieldsModel model, SinglebeamDatasetInstrumentController controller,
      List<DropDownItem> verticalDatumOptions) {
    super(model, controller);
    this.verticalDatumOptions = verticalDatumOptions;
  }

  @Override
  protected void initializeFields() {
    verticalDatumPanel.getInstrumentField().setModel(new DefaultComboBoxModel<>(
        verticalDatumOptions.toArray(new DropDownItem[0])
    ));
    verticalDatumPanel.getInstrumentField().setSelectedItem(model.getVerticalDatum());
    obsRatePanel.getField().setText(model.getObsRate());
    soundVelocityPanel.getField().setText(model.getSoundVelocity());
  }

  @Override
  protected void setupLayout() {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);

    add(verticalDatumPanel, configureLayout(0, 0));
    add(obsRatePanel, configureLayout(1, 0));
    add(soundVelocityPanel, configureLayout(2, 0));
  }

  @Override
  protected void setupMvc() {
    verticalDatumPanel.getInstrumentField().addItemListener((evt) -> controller.setVerticalDatum((DropDownItem) evt.getItem()));
    obsRatePanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setObsRate(obsRatePanel.getField().getText()));
    soundVelocityPanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setSoundVelocity(soundVelocityPanel.getField().getText()));
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case UPDATE_VERTICAL_DATUM:
        updateComboBox(verticalDatumPanel.getInstrumentField(), evt);
        break;
      case UPDATE_OBS_RATE:
        updateTextField(obsRatePanel.getField(), evt);
        break;
      case UPDATE_SOUND_VELOCITY:
        updateTextField(soundVelocityPanel.getField(), evt);
        break;
      case UPDATE_VERTICAL_DATUM_ERROR:
        updateLabelText(verticalDatumPanel.getErrorLabel(), evt);
        break;
      case UPDATE_OBS_RATE_ERROR:
        updateLabelText(obsRatePanel.getErrorLabel(), evt);
        break;
      case UPDATE_SOUND_VELOCITY_ERROR:
        updateLabelText(soundVelocityPanel.getErrorLabel(), evt);
        break;
      default:
        break;
    }
  }
}
