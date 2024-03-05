package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.singebeam;

import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel.UPDATE_COMMENTS;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel.UPDATE_COMMENTS_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel.UPDATE_INSTRUMENT;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel.UPDATE_INSTRUMENT_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel.UPDATE_OBS_RATE;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel.UPDATE_OBS_RATE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel.UPDATE_SOUND_VELOCITY;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel.UPDATE_SOUND_VELOCITY_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel.UPDATE_VERTICAL_DATUM;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel.UPDATE_VERTICAL_DATUM_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.setSelectedButton;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateRadioButtonGroup;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.SinglebeamDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.SinglebeamDatasetInstrumentModel;
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

public class SinglebeamDatasetPanel extends DatasetPanel<SinglebeamDatasetInstrumentModel, SinglebeamDatasetInstrumentController> {

  public static final String INSTRUMENT_SHORT_CODE = "SB-BATHY";

  private static final String VERTICAL_DATUM_LABEL = "Vertical Datum";
  private static final String OBS_RATE_LABEL = "Observation Rate (obs/min)";
  private static final String SOUND_VELOCITY_LABEL = "Sound Velocity (m/s)";

  private final LabeledComboBoxPanel instrumentPanel = new LabeledComboBoxPanel();
  private final ProcessingLevelRadioPanel buttonPanel = new ProcessingLevelRadioPanel();
  private final CommentsTextAreaPanel commentsPanel = new CommentsTextAreaPanel();
  private final LabeledComboBoxPanel verticalDatumPanel = new LabeledComboBoxPanel(VERTICAL_DATUM_LABEL);
  private final LabeledTextFieldPanel obsRatePanel = new LabeledTextFieldPanel(OBS_RATE_LABEL);
  private final LabeledTextFieldPanel soundVelocityPanel = new LabeledTextFieldPanel(SOUND_VELOCITY_LABEL);
  private final List<DropDownItem> verticalDatumOptions;

  public SinglebeamDatasetPanel(SinglebeamDatasetInstrumentModel model, SinglebeamDatasetInstrumentController controller, InstrumentDatastore instrumentDatastore, List<DropDownItem> verticalDatumOptions) {
    super(model, controller, instrumentDatastore);
    this.verticalDatumOptions = verticalDatumOptions;
  }

  @Override
  public void init() {
    super.init();
    instrumentPanel.getInstrumentField().setModel(new DefaultComboBoxModel<>(instrumentDatastore.getInstrumentDropDownsForDatasetType(INSTRUMENT_SHORT_CODE).toArray(new DropDownItem[0])));
    instrumentPanel.getInstrumentField().setSelectedItem(model.getInstrument());
    setSelectedButton(buttonPanel.getProcessingLevelGroup(), model.getProcessingLevel());
    commentsPanel.getCommentsField().setText(model.getComments());
    verticalDatumPanel.getInstrumentField().setModel(new DefaultComboBoxModel<>(
      verticalDatumOptions.toArray(new DropDownItem[0])
    ));
    verticalDatumPanel.getInstrumentField().setSelectedItem(model.getVerticalDatum());
    obsRatePanel.getField().setText(model.getObsRate());
    soundVelocityPanel.getField().setText(model.getSoundVelocity());

    instrumentPanel.getInstrumentField().addItemListener((evt) -> controller.setInstrument((DropDownItem) evt.getItem()));
    buttonPanel.addActionListener((evt) -> controller.setProcessingLevel(buttonPanel.getSelectedButtonText()));
    commentsPanel.getCommentsField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setComments(commentsPanel.getCommentsField().getText()));
    verticalDatumPanel.getInstrumentField().addItemListener((evt) -> controller.setVerticalDatum((DropDownItem) evt.getItem()));
    obsRatePanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setObsRate(obsRatePanel.getField().getText()));
    soundVelocityPanel.getField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setSoundVelocity(soundVelocityPanel.getField().getText()));

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
    row2.add(verticalDatumPanel, configureLayout(0, 0));
    row2.add(obsRatePanel, configureLayout(1, 0));
    row2.add(soundVelocityPanel, configureLayout(2, 0));
    panel.add(row2, configureLayout(0, 1));

    panel.add(commentsPanel, configureLayout(0, 2));
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
      case UPDATE_VERTICAL_DATUM:
        updateComboBox(verticalDatumPanel.getInstrumentField(), evt);
        break;
      case UPDATE_OBS_RATE:
        updateTextField(obsRatePanel.getField(), evt);
        break;
      case UPDATE_SOUND_VELOCITY:
        updateTextField(soundVelocityPanel.getField(), evt);
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
