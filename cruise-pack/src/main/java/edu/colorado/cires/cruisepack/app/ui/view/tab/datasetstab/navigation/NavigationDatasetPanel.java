package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.navigation;

import static edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationDatasetInstrumentModel.UPDATE_COMMENTS;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationDatasetInstrumentModel.UPDATE_COMMENTS_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationDatasetInstrumentModel.UPDATE_INSTRUMENT;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationDatasetInstrumentModel.UPDATE_INSTRUMENT_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationDatasetInstrumentModel.UPDATE_NAV_DATUM;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationDatasetInstrumentModel.UPDATE_NAV_DATUM_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.setSelectedButton;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateRadioButtonGroup;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.NavigationDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.SimpleDocumentListener;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.CommentsTextAreaPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledComboBoxPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ProcessingLevelRadioPanel;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.JPanel;

class NavigationDatasetPanel extends DatasetPanel<NavigationDatasetInstrumentModel, NavigationDatasetInstrumentController> {

  public static final String INSTRUMENT_SHORT_CODE = "NAV";

  private static final String NAV_DATUM_LABEL = "Navigation Datum";

  private final LabeledComboBoxPanel instrumentPanel = new LabeledComboBoxPanel();
  private final ProcessingLevelRadioPanel buttonPanel = new ProcessingLevelRadioPanel();
  private final CommentsTextAreaPanel commentsPanel = new CommentsTextAreaPanel();
  private final LabeledComboBoxPanel navDatumPanel = new LabeledComboBoxPanel(NAV_DATUM_LABEL);


  public NavigationDatasetPanel(NavigationDatasetInstrumentModel model, NavigationDatasetInstrumentController controller, InstrumentDatastore instrumentDatastore) {
    super(model, controller, instrumentDatastore);
  }

  @Override
  public void init() {
    super.init();
    instrumentPanel.getInstrumentField().setSelectedItem(model.getInstrument());
    setSelectedButton(buttonPanel.getProcessingLevelGroup(), model.getProcessingLevel());
    commentsPanel.getCommentsField().setText(model.getComments());
    navDatumPanel.getInstrumentField().setSelectedItem(model.getNavDatum());

    instrumentPanel.getInstrumentField().addItemListener((evt) -> controller.setInstrument((DropDownItem) evt.getItem()));
    buttonPanel.addActionListener((evt) -> controller.setProcessingLevel(buttonPanel.getSelectedButtonText()));
    commentsPanel.getCommentsField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setComments(commentsPanel.getCommentsField().getText()));
    navDatumPanel.getInstrumentField().addItemListener((evt) -> controller.setNavDatum((DropDownItem) evt.getItem()));

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
    panel.add(navDatumPanel, configureLayout(0, 1));
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
      case UPDATE_NAV_DATUM:
        updateComboBox(navDatumPanel.getInstrumentField(), evt);
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
      case UPDATE_NAV_DATUM_ERROR:
        updateLabelText(navDatumPanel.getErrorLabel(), evt);
        break;
      default:
        break;
    }
  }
}
