package edu.colorado.cires.cruisepack.app.ui.model.queue;

import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModel;
import edu.colorado.cires.cruisepack.app.ui.view.queue.PackJobPanel;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class QueueModel extends PropertyChangeModel {
  public static final String ADD_TO_QUEUE = "ADD_TO_QUEUE";
  public static final String REMOVE_FROM_QUEUE = "REMOVE_FROM_QUEUE";
  public static final String CLEAR_QUEUE = "CLEAR_QUEUE";
  public static final String UPDATE_PACKAGE_QUEUE_BUTTON = "UPDATE_PACKAGE_QUEUE_BUTTON";
  public static final String UPDATE_CLEAR_QUEUE_BUTTON = "UPDATE_CLEAR_QUEUE_BUTTON";
  public static final String EMIT_QUEUE_SIZE = "EMIT_QUEUE_SIZE";
  
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
      emitQueueSize(oldJobs, newJobs);
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
      emitQueueSize(oldJobs, newJobs);
    }
  }
  
  private void emitQueueSize(List<PackJob> oldJobs, List<PackJob> newJobs) {
    fireChangeEvent(EMIT_QUEUE_SIZE, oldJobs.size(), newJobs.size());
  }
  
  public void clearQueue() {
    List<PackJob> oldJobs = new ArrayList<>(queue);
    queue = new ArrayList<>();
    fireChangeEvent(CLEAR_QUEUE, oldJobs, queue);
    emitQueueSize(oldJobs, queue);
  }
  
  public void updatePackageQueueButton(boolean enabled) {
    fireChangeEvent(UPDATE_PACKAGE_QUEUE_BUTTON, !enabled, enabled);
  }
  
  public void updateClearQueueButton(boolean enabled) {
    fireChangeEvent(UPDATE_CLEAR_QUEUE_BUTTON, !enabled, enabled);
  }
  
  public void updatePackageButton(boolean enabled, String processId) {
    fireChangeEvent(String.format(
        "UPDATE_PACKAGE_BUTTON_%s", processId
    ), !enabled, enabled);
  }
  
  public void updateRemoveButton(boolean enabled, String processId) {
    fireChangeEvent(String.format(
        "UPDATE_REMOVE_BUTTON_%s", processId
    ), !enabled, enabled);
  }
  
  public void updateStopButton(boolean enabled, String processId) {
    fireChangeEvent(String.format(
        "UPDATE_STOP_BUTTON_%s", processId
    ), !enabled, enabled);
  }

}
