package edu.colorado.cires.cruisepack.app.ui.controller.dataset;

import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.Path;
import java.time.LocalDate;

public abstract class BaseDatasetInstrumentController<T extends AdditionalFieldsModel> implements PropertyChangeListener {

  protected final BaseDatasetInstrumentModel<T> model;
  private DatasetPanel<T, ?> view;

  public BaseDatasetInstrumentController(BaseDatasetInstrumentModel<T> model) {
    this.model = model;
  }

  public void init() {
    model.addChangeListener(this);
    AdditionalFieldsModel additionalFieldsModel = model.getAdditionalFieldsModel();
    if (additionalFieldsModel != null) {
      additionalFieldsModel.addChangeListener(this);
    }
  }

  public void setView(DatasetPanel<T, ?> view) {
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
  
  public void setAncillaryPath(Path ancillaryPath) {
    model.setAncillaryPath(ancillaryPath);
  }
  
  public void setAncillaryDetails(String ancillaryDetails) {
    model.setAncillaryDetails(ancillaryDetails);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    view.onChange(evt);
  }


}
