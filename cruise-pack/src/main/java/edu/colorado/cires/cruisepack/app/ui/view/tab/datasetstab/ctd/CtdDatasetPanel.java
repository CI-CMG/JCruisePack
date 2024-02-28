package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ctd;

import static edu.colorado.cires.cruisepack.app.ui.model.dataset.CtdDatasetInstrumentModel.UPDATE_COMMENTS;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.CtdDatasetInstrumentModel.UPDATE_COMMENTS_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.CtdDatasetInstrumentModel.UPDATE_INSTRUMENT;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.CtdDatasetInstrumentModel.UPDATE_INSTRUMENT_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.CtdDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.CtdDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.setSelectedButton;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateRadioButtonGroup;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.CtdDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.CtdDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.SimpleDocumentListener;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.CommentsTextAreaPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledComboBoxPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ProcessingLevelRadioPanel;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

public class CtdDatasetPanel extends DatasetPanel<CtdDatasetInstrumentModel, CtdDatasetInstrumentController> {

  public static final String INSTRUMENT_SHORT_CODE = "CTD";

  private final LabeledComboBoxPanel instrumentPanel = new LabeledComboBoxPanel();
  private final ProcessingLevelRadioPanel buttonPanel = new ProcessingLevelRadioPanel();
  private final CommentsTextAreaPanel commentsPanel = new CommentsTextAreaPanel();

  public CtdDatasetPanel(CtdDatasetInstrumentModel model, CtdDatasetInstrumentController controller, InstrumentDatastore instrumentDatastore) {
    super(model, controller, instrumentDatastore);
  }

  @Override
  public void init() {
    super.init();
    instrumentPanel.getInstrumentField().setModel(new DefaultComboBoxModel<>(instrumentDatastore.getInstrumentDropDownsForDatasetType(INSTRUMENT_SHORT_CODE).toArray(new DropDownItem[0])));
    instrumentPanel.getInstrumentField().setSelectedItem(model.getInstrument());
    setSelectedButton(buttonPanel.getProcessingLevelGroup(), model.getProcessingLevel());
    commentsPanel.getCommentsField().setText(model.getComments());

    instrumentPanel.getInstrumentField().addItemListener((evt) -> controller.setInstrument((DropDownItem) evt.getItem()));
    buttonPanel.addActionListener((evt) -> controller.setProcessingLevel(buttonPanel.getSelectedButtonText()));
    commentsPanel.getCommentsField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setComments(commentsPanel.getCommentsField().getText()));
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
    panel.add(commentsPanel, configureLayout(0, 1));
    return panel;
  }

  @Override
  protected void customOnChange(PropertyChangeEvent evt) {
    if (UPDATE_INSTRUMENT.equals(evt.getPropertyName())) {
      updateComboBox(instrumentPanel.getInstrumentField(), evt);
    } else if (UPDATE_PROCESSING_LEVEL.equals(evt.getPropertyName())) {
      updateRadioButtonGroup(buttonPanel.getProcessingLevelGroup(), evt);
    } else if (UPDATE_COMMENTS.equals(evt.getPropertyName())) {
      updateTextField(commentsPanel.getCommentsField(), evt);
    } else if (UPDATE_INSTRUMENT_ERROR.equals(evt.getPropertyName())) {
      updateLabelText(instrumentPanel.getErrorLabel(), evt);
    } else if (UPDATE_COMMENTS_ERROR.equals(evt.getPropertyName())) {
      updateLabelText(commentsPanel.getErrorLabel(), evt);
    } else if (UPDATE_PROCESSING_LEVEL_ERROR.equals(evt.getPropertyName())) {
      updateLabelText(buttonPanel.getErrorLabel(), evt);
    }
  }

}
