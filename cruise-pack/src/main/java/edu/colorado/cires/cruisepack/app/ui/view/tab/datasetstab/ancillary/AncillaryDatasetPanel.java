package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ancillary;

import static edu.colorado.cires.cruisepack.app.ui.model.dataset.AncillaryDatasetInstrumentModel.UPDATE_COMMENTS;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.AncillaryDatasetInstrumentModel.UPDATE_COMMENTS_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.AncillaryDatasetInstrumentModel.UPDATE_INSTRUMENT;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.AncillaryDatasetInstrumentModel.UPDATE_INSTRUMENT_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.AncillaryDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.AncillaryDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.SimpleDocumentListener;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.CommentsTextAreaPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledComboBoxPanel;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

public class AncillaryDatasetPanel extends DatasetPanel<AncillaryDatasetInstrumentModel, AncillaryDatasetInstrumentController> {

  public static final String INSTRUMENT_SHORT_CODE = "ANCILLARY";

  private static final String INSTRUMENT_TITLE = "Ancillary Data Type";
  private static final String COMMENTS_TITLE = "Ancillary Data Details";

  private final LabeledComboBoxPanel instrumentPanel = new LabeledComboBoxPanel(INSTRUMENT_TITLE);
  private final CommentsTextAreaPanel commentsPanel = new CommentsTextAreaPanel(COMMENTS_TITLE);

  public AncillaryDatasetPanel(AncillaryDatasetInstrumentModel model, AncillaryDatasetInstrumentController controller,
      InstrumentDatastore instrumentDatastore) {
    super(model, controller, instrumentDatastore);
  }

  @Override
  public void init() {
    super.init();
    instrumentPanel.getInstrumentField().setModel(new DefaultComboBoxModel<>(instrumentDatastore.getInstrumentDropDownsForDatasetType(INSTRUMENT_SHORT_CODE).toArray(new DropDownItem[0])));
    instrumentPanel.getInstrumentField().setSelectedItem(model.getInstrument());
    commentsPanel.getCommentsField().setText(model.getComments());

    instrumentPanel.getInstrumentField().addItemListener((evt) -> controller.setInstrument((DropDownItem) evt.getItem()));
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
    panel.add(instrumentPanel, configureLayout(0, 0));
    panel.add(commentsPanel, configureLayout(0, 1));
    return panel;
  }

  @Override
  protected void customOnChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case UPDATE_INSTRUMENT:
        updateComboBox(instrumentPanel.getInstrumentField(), evt);
        break;
      case UPDATE_COMMENTS:
        updateTextField(commentsPanel.getCommentsField(), evt);
        break;
      case UPDATE_INSTRUMENT_ERROR:
        updateLabelText(instrumentPanel.getErrorLabel(), evt);
        break;
      case UPDATE_COMMENTS_ERROR:
        updateLabelText(commentsPanel.getErrorLabel(), evt);
      default:
        break;
    }
  }
}
