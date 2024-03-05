package edu.colorado.cires.cruisepack.app.ui.controller.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.Path;
import java.time.LocalDate;

public abstract class BaseDatasetInstrumentController<M extends BaseDatasetInstrumentModel> implements PropertyChangeListener {

  protected final M model;
  private DatasetPanel<?, ?> view;

  public BaseDatasetInstrumentController(M model) {
    this.model = model;
  }

  public void init() {
    model.addChangeListener(this);
  }

  public void setView(DatasetPanel<?, ?> view) {
    this.view = view;
  }

  public void setPublicReleaseDate(LocalDate publicReleaseDate) {
    model.setPublicReleaseDate(publicReleaseDate);
  }

  public void setDataPath(Path dataPath) {
    model.setDataPath(dataPath);
  }
  
  public void setInstrument(DropDownItem instrument) {
    model.setInstrument(instrument);
  }
  
  public void setProcessingLevel(String processingLevel) {
    model.setProcessingLevel(processingLevel);
  }
  
  public void setComments(String comments) {
    model.setComments(comments);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    view.onChange(evt);
  }


}
