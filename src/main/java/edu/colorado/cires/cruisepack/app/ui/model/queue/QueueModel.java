package edu.colorado.cires.cruisepack.app.ui.model.queue;

import edu.colorado.cires.cruisepack.app.ui.model.DropDownItemModel;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidDropDownItemModel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.DropDownItemPanel;
import jakarta.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class QueueModel extends PropertyChangeModel {
  
  public static final String UPDATE_QUEUE_ERROR = "UPDATE_QUEUE_ERROR";
  public static final String ADD_TO_QUEUE = "ADD_TO_QUEUE";
  public static final String REMOVE_FROM_QUEUE = "REMOVE_FROM_QUEUE";
  public static final String CLEAR_QUEUE = "CLEAR_QUEUE";
  public static final String UPDATE_PACKAGE_QUEUE_BUTTON = "UPDATE_PACKAGE_QUEUE_BUTTON";
  public static final String UPDATE_CLEAR_QUEUE_BUTTON = "UPDATE_CLEAR_QUEUE_BUTTON";
  
  private List<@ValidDropDownItemModel DropDownItemModel> queue = new ArrayList<>(0);
  private String queueError;

  public List<DropDownItemModel> getQueue() {
    return queue;
  }

  public void addToQueue(DropDownItemPanel panel) {
    List<DropDownItemModel> oldModels = new ArrayList<>(queue);
    DropDownItemModel model = panel.getModel();
    if (!oldModels.contains(model)) {
      List<DropDownItemModel> newModels = new ArrayList<>(queue);
      newModels.add(model);
      this.queue = newModels;
      fireChangeEvent(ADD_TO_QUEUE, null, panel);
    }
  }
  
  public void removeFromQueue(DropDownItemPanel panel) {
    List<DropDownItemModel> oldModels = new ArrayList<>(queue);
    DropDownItemModel model = panel.getModel();
    if (oldModels.contains(model)) {
      List<DropDownItemModel> newModels = new ArrayList<>(queue);
      newModels.remove(model);
      queue = newModels;
      fireChangeEvent(REMOVE_FROM_QUEUE, panel, null);
    }
  }
  
  public void clearQueue() {
    List<DropDownItemModel> oldModels = new ArrayList<>(queue);
    queue = new ArrayList<>();
    fireChangeEvent(CLEAR_QUEUE, oldModels, queue);
  }

  public String getQueueError() {
    return queueError;
  }

  public void setQueueError(String queueError) {
    setIfChanged(UPDATE_QUEUE_ERROR, queueError, () -> this.queueError, (e) -> this.queueError = e);
  }
  
  public void updatePackageQueueButton(boolean enabled) {
    fireChangeEvent(UPDATE_PACKAGE_QUEUE_BUTTON, !enabled, enabled);
  }
  
  public void updateClearQueueButton(boolean enabled) {
    fireChangeEvent(UPDATE_CLEAR_QUEUE_BUTTON, !enabled, enabled);
  }
  
  public void setErrors(Set<ConstraintViolation<QueueModel>> constraintViolations) {
    
  }
}
