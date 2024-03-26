package edu.colorado.cires.cruisepack.app.ui.controller.queue;

import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.service.pack.ClearJobsPublisher;
import edu.colorado.cires.cruisepack.app.service.pack.PackQueuePublisher;
import edu.colorado.cires.cruisepack.app.service.pack.StopJobPublisher;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.queue.QueueModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.queue.PackJobPanel;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueueController implements PropertyChangeListener {
  
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final QueueModel queueModel;
  private final PackQueuePublisher packQueuePublisher;
  private final StopJobPublisher stopJobPublisher;
  private final ClearJobsPublisher clearJobsPublisher;

  @Autowired
  public QueueController(ReactiveViewRegistry reactiveViewRegistry, QueueModel queueModel, PackQueuePublisher packQueuePublisher,
      StopJobPublisher stopJobPublisher, ClearJobsPublisher clearJobsPublisher) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.queueModel = queueModel;
    this.packQueuePublisher = packQueuePublisher;
    this.stopJobPublisher = stopJobPublisher;
    this.clearJobsPublisher = clearJobsPublisher;
  }
  
  @PostConstruct
  public void init() {
    queueModel.addChangeListener(this);
  }

  public void removeFromQueue(PackJobPanel panel) {
    queueModel.removeFromQueue(panel);
    stopJobPublisher.publish(this, panel.getProcessId());
  }

  public List<PackJob> getQueue() {
    return queueModel.getQueue();
  }

  public void submit(PackJobPanel packJobPanel) {
    String processId = packJobPanel.getProcessId();
    packQueuePublisher.publish(
        this,
        processId,
        packJobPanel.getPackJob(),
        () -> {
          queueModel.updateStopButton(true, processId);
          queueModel.updateRemoveButton(false, processId);
          queueModel.updateStopAllButton(true);
        },
        (jobsInProgress) -> {
          queueModel.updateStopButton(false, processId);
          queueModel.updateRemoveButton(true, processId);
          queueModel.updateStopAllButton(jobsInProgress);
          
          queueModel.removeFromQueue(packJobPanel);
        }
    );
  }
  
  public void stop() {
    clearJobsPublisher.publish(this, queueModel::clearQueue);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    for (ReactiveView reactiveView : reactiveViewRegistry.getViews()) {
      reactiveView.onChange(evt);
    }
  }
}
