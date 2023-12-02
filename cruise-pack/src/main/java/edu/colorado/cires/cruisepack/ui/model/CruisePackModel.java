package edu.colorado.cires.cruisepack.ui.model;

import edu.colorado.cires.cruisepack.datastore.DatasetDatastore;
import edu.colorado.cires.cruisepack.ui.controller.DefaultController;
import edu.colorado.cires.cruisepack.ui.controller.Events;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CruisePackModel extends AbstractModel {


  private String cruiseId = "";
  private String segment = "";
  private final DropDownModel dropDownModel;
  private final DatasetDatastore datasetDatastore;
  private final DefaultController controller;

  private List<DataWidgetModel> instruments = new ArrayList<>();

  public CruisePackModel(DropDownModel dropDownModel, DatasetDatastore datasetDatastore, DefaultController controller) {
    this.dropDownModel = dropDownModel;
    this.datasetDatastore = datasetDatastore;
    this.controller = controller;
  }


  public String getCruiseId() {
    return cruiseId;
  }

  public void setCruiseId(String cruiseId) {
    String oldCruiseId = this.cruiseId;
    this.cruiseId = cruiseId;
    fireChangeEvent(Events.UPDATE_CRUISE_ID, oldCruiseId, cruiseId);
  }

  public String getSegment() {
    return segment;
  }

  public void setSegment(String newValue) {
    String oldValue = this.segment;
    this.segment = newValue;
    fireChangeEvent(Events.UPDATE_SEGMENT, oldValue, cruiseId);
  }

  public List<DataWidgetModel> getInstruments() {
    return instruments;
  }

  public void setInstruments(List<DataWidgetModel> instruments) {
    this.instruments = instruments;
  }

  public void addInstrument() {
    DataWidgetModel instrument = new DataWidgetModel("Other", dropDownModel.getDatasetTypes(), dropDownModel.getInstruments("Other"));
    instruments.add(instrument);
    fireChangeEvent(Events.ADD_DATASET, null, datasetDatastore.getDatasetFactory("Other").initialize(instrument, controller));
  }

  public void removeDataset(String id) {
    Iterator<DataWidgetModel> it = instruments.iterator();
    while (it.hasNext()) {
      if (it.next().getId().equals(id)) {
        it.remove();
      }
    }
    fireChangeEvent(Events.REMOVE_DATASET, null, id);
  }

  public void changeDataset(String id, String dataType) {
    DataWidgetModel existing = null;
    Iterator<DataWidgetModel> it = instruments.iterator();
    while (it.hasNext()) {
      DataWidgetModel old = it.next();
      if (old.getId().equals(id)) {
        existing = old;
        break;
      }
    }
    if (existing != null && !existing.getType().equals(dataType)) {
      DataWidgetModel instrument = new DataWidgetModel(dataType, dropDownModel.getDatasetTypes(), dropDownModel.getInstruments(dataType));
      instruments.add(instrument);
      instruments.remove(existing);
      fireChangeEvent(Events.CHANGE_DATASET, id, datasetDatastore.getDatasetFactory(dataType).initialize(instrument, controller));
    }

  }
}
