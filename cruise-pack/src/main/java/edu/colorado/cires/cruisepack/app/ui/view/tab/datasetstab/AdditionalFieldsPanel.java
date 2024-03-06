package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import edu.colorado.cires.cruisepack.app.ui.controller.dataset.BaseDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModel;
import java.beans.PropertyChangeEvent;
import javax.swing.JPanel;

public abstract class AdditionalFieldsPanel<T extends AdditionalFieldsModel, C extends BaseDatasetInstrumentController<T>> extends JPanel {
  
  protected final T model;
  protected final C controller;

  protected AdditionalFieldsPanel(T model, C controller) {
    this.model = model;
    this.controller = controller;
  }
  
  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }
  
  protected abstract void initializeFields();
  protected abstract void setupLayout();
  protected abstract void setupMvc();
  public abstract void onChange(PropertyChangeEvent evt);
}
