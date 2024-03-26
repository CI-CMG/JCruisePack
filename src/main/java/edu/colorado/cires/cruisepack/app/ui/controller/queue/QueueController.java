package edu.colorado.cires.cruisepack.app.ui.controller.queue;

import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.service.pack.ClearJobsPublisher;
import edu.colorado.cires.cruisepack.app.service.pack.PackQueuePublisher;
import edu.colorado.cires.cruisepack.app.service.pack.StopJobPublisher;
import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
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
  private final FooterControlController footerControlController;

  @Autowired
  public QueueController(ReactiveViewRegistry reactiveViewRegistry, QueueModel queueModel, PackQueuePublisher packQueuePublisher,
      StopJobPublisher stopJobPublisher, FooterControlController footerControlController) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.queueModel = queueModel;
    this.packQueuePublisher = packQueuePublisher;
    this.stopJobPublisher = stopJobPublisher;
    this.footerControlController = footerControlController;
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
          
          footerControlController.setStopButtonEnabled(true);
        },
        (queueEmpty) -> {
          queueModel.updateStopButton(false, processId);
          queueModel.updateRemoveButton(true, processId);
          
          queueModel.removeFromQueue(packJobPanel);
          
          footerControlController.setStopButtonEnabled(!queueEmpty);
        }
    );
  }
  
  public void stop(String processId) {
    stopJobPublisher.publish(this, processId);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    for (ReactiveView reactiveView : reactiveViewRegistry.getViews()) {
      reactiveView.onChange(evt);
    }
  }
}
