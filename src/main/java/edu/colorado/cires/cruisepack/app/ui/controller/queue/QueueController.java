package edu.colorado.cires.cruisepack.app.ui.controller.queue;

import edu.colorado.cires.cruisepack.app.service.pack.PackQueuePublisher;
import edu.colorado.cires.cruisepack.app.service.pack.StopJobPublisher;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.queue.QueueModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.queue.PackJobPanel;
import jakarta.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueueController implements PropertyChangeListener {
  
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final QueueModel queueModel;
  private final PackQueuePublisher packQueuePublisher;
  private final StopJobPublisher stopJobPublisher;

  @Autowired
  public QueueController(ReactiveViewRegistry reactiveViewRegistry, QueueModel queueModel, PackQueuePublisher packQueuePublisher,
      StopJobPublisher stopJobPublisher) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.queueModel = queueModel;
    this.packQueuePublisher = packQueuePublisher;
    this.stopJobPublisher = stopJobPublisher;
  }
  
  @PostConstruct
  public void init() {
    queueModel.addChangeListener(this);
  }

  public void removeFromQueue(PackJobPanel panel) {
    queueModel.removeFromQueue(panel);
  }
  
  public void clearQueue() {
    queueModel.clearQueue();
  }

  public void submit(PackJobPanel packJobPanel) {
    String processId = packJobPanel.getProcessId();
    packQueuePublisher.publish(
        this,
        processId,
        packJobPanel.getPackJob(),
        () -> {
          queueModel.updateStopButton(true, processId);
          queueModel.updatePackageButton(false, processId);
          queueModel.updateRemoveButton(false, processId);
          queueModel.updateClearQueueButton(false);
          queueModel.updatePackageQueueButton(false);
        },
        () -> {
          queueModel.updateStopButton(false, processId);
          queueModel.updatePackageButton(true, processId);
          queueModel.updateRemoveButton(true, processId);
          queueModel.updateClearQueueButton(true);
          queueModel.updatePackageQueueButton(true);
        }
    );
  }
  
  public void stop(PackJobPanel packJobPanel) {
    stopJobPublisher.publish(
        this,
        packJobPanel.getPackJob().getPackageId()
    );
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    for (ReactiveView reactiveView : reactiveViewRegistry.getViews()) {
      reactiveView.onChange(evt);
    }
  }
}
