package edu.colorado.cires.cruisepack.app.ui.model.queue;

import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModel;
import edu.colorado.cires.cruisepack.app.ui.view.queue.PackJobPanel;
import jakarta.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class QueueModel extends PropertyChangeModel {
  public static final String ADD_TO_QUEUE = "ADD_TO_QUEUE";
  public static final String REMOVE_FROM_QUEUE = "REMOVE_FROM_QUEUE";
  public static final String CLEAR_QUEUE = "CLEAR_QUEUE";
  public static final String UPDATE_PACKAGE_QUEUE_BUTTON = "UPDATE_PACKAGE_QUEUE_BUTTON";
  public static final String UPDATE_CLEAR_QUEUE_BUTTON = "UPDATE_CLEAR_QUEUE_BUTTON";
  
  private List<PackJob> queue = new ArrayList<>(0);

  public List<PackJob> getQueue() {
    return queue;
  }

  public void addToQueue(PackJobPanel panel) {
    List<PackJob> oldJobs = new ArrayList<>(queue);
    PackJob packJob = panel.getPackJob();
    if (!oldJobs.contains(packJob)) {
      List<PackJob> newJobs = new ArrayList<>(queue);
      newJobs.add(packJob);
      this.queue = newJobs;
      fireChangeEvent(ADD_TO_QUEUE, null, panel);
    }
  }
  
  public void removeFromQueue(PackJobPanel panel) {
    List<PackJob> oldJobs = new ArrayList<>(queue);
    PackJob packJob = panel.getPackJob();
    if (oldJobs.contains(packJob)) {
      List<PackJob> newJobs = new ArrayList<>(queue);
      newJobs.remove(packJob);
      queue = newJobs;
      fireChangeEvent(REMOVE_FROM_QUEUE, panel, null);
    }
  }
  
  public void clearQueue() {
    List<PackJob> oldJobs = new ArrayList<>(queue);
    queue = new ArrayList<>();
    fireChangeEvent(CLEAR_QUEUE, oldJobs, queue);
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
