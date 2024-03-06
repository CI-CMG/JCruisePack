package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.navigation;

import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_INSTRUMENT;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationAdditionalFieldsModel.UPDATE_NAV_DATUM;
import static edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationAdditionalFieldsModel.UPDATE_NAV_DATUM_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.dataset.NavigationDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.dataset.NavigationAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.AdditionalFieldsPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.LabeledComboBoxPanel;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class NavigationDatasetPanel extends AdditionalFieldsPanel<NavigationAdditionalFieldsModel, NavigationDatasetInstrumentController> {

  private static final String NAV_DATUM_LABEL = "Navigation Datum";
  private final LabeledComboBoxPanel navDatumPanel = new LabeledComboBoxPanel(NAV_DATUM_LABEL);
  private final List<DropDownItem> navigationDatumOptions;


  public NavigationDatasetPanel(NavigationAdditionalFieldsModel model, NavigationDatasetInstrumentController controller, List<DropDownItem> navigationDatumOptions) {
    super(model, controller);
    this.navigationDatumOptions = navigationDatumOptions;
  }
  
  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }
  
  @Override
  protected void initializeFields() {
    navDatumPanel.getInstrumentField().setModel(new DefaultComboBoxModel<>(
        navigationDatumOptions.toArray(new DropDownItem[0])
    ));
    navDatumPanel.getInstrumentField().setSelectedItem(model.getNavDatum());
  }
  
  @Override
  protected void setupLayout() {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);
    add(navDatumPanel, configureLayout(0, 0));
  }
  
  @Override
  protected void setupMvc() {
    navDatumPanel.getInstrumentField().addItemListener((evt) -> controller.setNavDatum((DropDownItem) evt.getItem()));
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case UPDATE_INSTRUMENT:
      case UPDATE_NAV_DATUM:
        updateComboBox(navDatumPanel.getInstrumentField(), evt);
        break;
      case UPDATE_NAV_DATUM_ERROR:
        updateLabelText(navDatumPanel.getErrorLabel(), evt);
        break;
      default:
        break;
    }
  }
}
