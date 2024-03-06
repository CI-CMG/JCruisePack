package edu.colorado.cires.cruisepack.app.ui.controller;

import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.model.DatasetsModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.DatasetPanel;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatasetsController implements PropertyChangeListener {

  private final ReactiveViewRegistry reactiveViewRegistry;
  private final DatasetsModel datasetsModel;

  @Autowired
  public DatasetsController(ReactiveViewRegistry reactiveViewRegistry, DatasetsModel datasetsModel) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.datasetsModel = datasetsModel;
  }

  @PostConstruct
  public void init() {
    datasetsModel.addChangeListener(this);
  }

  public void addDataset(DatasetPanel<? extends AdditionalFieldsModel, ?> row) {
    datasetsModel.addDataset(row);
  }

  public void removeDataset(DatasetPanel<? extends AdditionalFieldsModel, ?> row) {
    datasetsModel.removeDataset(row);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    for (ReactiveView view : reactiveViewRegistry.getViews()) {
      view.onChange(evt);
    }
  }


}
