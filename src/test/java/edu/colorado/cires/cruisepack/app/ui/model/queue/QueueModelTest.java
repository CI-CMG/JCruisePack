package edu.colorado.cires.cruisepack.app.ui.model.queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModelTest;
import edu.colorado.cires.cruisepack.app.ui.view.queue.PackJobPanel;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class QueueModelTest extends PropertyChangeModelTest<QueueModel> {

  @Override
  protected QueueModel createModel() {
    return new QueueModel();
  }
  
  private PackJobPanel createPanel(String packageId) {
    return new PackJobPanel(
        PackJob.builder()
            .setPackageId(packageId)
            .setCruiseId(UUID.randomUUID().toString())
            .build()
    );
  }

  @Test
  void addToQueue() {
    PackJobPanel panel1 = createPanel(UUID.randomUUID().toString());
    PackJobPanel panel2 = createPanel(UUID.randomUUID().toString());

    List<PackJobPanel> panels = List.of(
        panel1, panel2
    );
    panels.forEach(model::addToQueue);
    
    assertChangeEvents(
        QueueModel.ADD_TO_QUEUE,
        panels.stream()
            .map(p -> (PackJobPanel) null)
            .toList(),
        panels,
        (v) -> v
    );
    assertEquals(
        panels.stream()
            .map(PackJobPanel::getPackJob)
            .toList(),
        model.getQueue()
    );
  }

  @Test
  void removeFromQueue() {
    PackJobPanel panel1 = createPanel(UUID.randomUUID().toString());
    PackJobPanel panel2 = createPanel(UUID.randomUUID().toString());

    List<PackJobPanel> panels = List.of(
        panel1, panel2
    );
    panels.forEach(model::addToQueue);
    panels.forEach(model::removeFromQueue);
    
    assertChangeEvents(
        QueueModel.REMOVE_FROM_QUEUE,
        panels,
        panels.stream()
            .map(p -> (PackJobPanel) null)
            .toList(),
        (v) -> v
    );
    assertEquals(Collections.emptyList(), model.getQueue());
  }

  @Test
  void clearQueue() {
    PackJobPanel panel1 = createPanel(UUID.randomUUID().toString());
    PackJobPanel panel2 = createPanel(UUID.randomUUID().toString());

    List<PackJobPanel> panels = List.of(
        panel1, panel2
    );
    panels.forEach(model::addToQueue);
    model.clearQueue();
    
    assertChangeEvent(
        QueueModel.CLEAR_QUEUE,
        panels.stream()
            .map(PackJobPanel::getPackJob)
            .toList(),
        Collections.emptyList()
    );
    assertEquals(Collections.emptyList(), model.getQueue());
  }

  @Test
  void updateRemoveButton() {
    String processId = UUID.randomUUID().toString();
    model.updateRemoveButton(true, processId);
    model.updateRemoveButton(false, processId);
    
    assertChangeEvents(
        String.format(
            "UPDATE_REMOVE_BUTTON_%s", processId
        ),
        List.of(
            false, true
        ),
        List.of(
            true, false
        ),
        (v) -> v
    );
  }

  @Test
  void updateStopButton() {
    String processId = UUID.randomUUID().toString();
    model.updateStopButton(true, processId);
    model.updateStopButton(false, processId);
    
    assertChangeEvents(
        String.format(
            "UPDATE_STOP_BUTTON_%s", processId
        ),
        List.of(
            false, true
        ),
        List.of(
            true, false
        ),
        (v) -> v
    );
  }
}