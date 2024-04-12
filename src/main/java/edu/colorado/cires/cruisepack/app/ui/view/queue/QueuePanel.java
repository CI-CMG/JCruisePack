package edu.colorado.cires.cruisepack.app.ui.view.queue;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateButtonEnabled;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateProgressBarModel;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.controller.queue.QueueController;
import edu.colorado.cires.cruisepack.app.ui.model.queue.QueueModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import jakarta.annotation.PostConstruct;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class QueuePanel extends JPanel implements ReactiveView {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(QueuePanel.class);
  
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final QueueController queueController;
  private final CruiseDataDatastore cruiseDataDatastore;
  
  private final JPanel listingPanel = new JPanel();
  private final JPanel fluff = new JPanel();

  @Autowired
  public QueuePanel(ReactiveViewRegistry reactiveViewRegistry, QueueController queueController, CruiseDataDatastore cruiseDataDatastore) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.queueController = queueController;
    this.cruiseDataDatastore = cruiseDataDatastore;
  }

  @PostConstruct
  public void init() {
    reactiveViewRegistry.register(this);
    
    setupLayout();
  }

  public int getNRows() {
    return (int) Arrays.stream(listingPanel.getComponents())
        .filter(c -> c instanceof PackJobPanel)
        .count();
  }
  
  private void setupLayout() {
    setLayout(new GridBagLayout());
    listingPanel.setLayout(new GridBagLayout());
    listingPanel.add(fluff, configureLayout(0, listingPanel.getComponentCount(), c -> c.weighty = 100));
    add(new JScrollPane(listingPanel), configureLayout(0, 0, c -> c.weighty = 100));
    
    queueController.getQueue().stream()
        .map(PackJobPanel::new)
        .forEach(this::addPackJob);
  }
  
  private void addPackJob(PackJobPanel panel) {
    listingPanel.remove(fluff);
    listingPanel.add(panel, configureLayout(0, panel.getComponentCount() - 1, c -> c.weighty = 0));
    listingPanel.add(fluff, configureLayout(0, panel.getComponentCount() - 1, c -> c.weighty = 100));

    panel.addRemoveListener(queueController::removeFromQueue);
    panel.addStopListener(packJobPanel -> queueController.stop(packJobPanel.getProcessId()));
    panel.init();
    
    revalidate();
    LOGGER.warn("Listing pannel size: {}", listingPanel.getComponentCount());

    queueController.submit(panel);
  }
  
  private void removePackJob(PackJobPanel panel) {
    listingPanel.remove(panel);
    
    revalidate();
    LOGGER.warn("Listing pannel size: {}", listingPanel.getComponentCount());
  }
  
  private void clearPackJobs() {
    int nItems = getComponentCount();
    while (nItems > 1) {
      java.awt.Component component = listingPanel.getComponent(nItems - 1);
      if (component instanceof PackJobPanel) {
        removePackJob((PackJobPanel) component);
        nItems = listingPanel.getComponentCount();
      }
    }
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case QueueModel.ADD_TO_QUEUE -> addPackJob((PackJobPanel) evt.getNewValue());
      case QueueModel.REMOVE_FROM_QUEUE -> removePackJob((PackJobPanel) evt.getOldValue());
      case QueueModel.CLEAR_QUEUE -> clearPackJobs();
      case Events.UPDATE_CRUISE_DATA_STORE -> {
       List<String> datastorePackageIds = cruiseDataDatastore.getCruises().stream()
           .map(CruiseData::getPackageId)
           .toList();
       
       // remove panels removed from datastore
        Arrays.stream(listingPanel.getComponents())
            .filter(c -> c instanceof PackJobPanel)
            .map(c -> (PackJobPanel) c)
           .filter(packJobPanel -> !datastorePackageIds.contains(packJobPanel.getPackJob().getPackageId()))
           .forEach(queueController::removeFromQueue);
      }
      default -> Arrays.stream(listingPanel.getComponents())
          .filter(c -> c instanceof PackJobPanel)
          .map(c -> (PackJobPanel) c)
          .filter(p -> evt.getPropertyName().endsWith(p.getProcessId()))
          .findFirst()
          .ifPresent(packJobPanel -> {
            if (evt.getPropertyName().startsWith("UPDATE_PROGRESS")) {
              updateProgressBarModel(packJobPanel.getProgressBarModel(), evt);
            } else if (evt.getPropertyName().startsWith("UPDATE_REMOVE_BUTTON")) {
              updateButtonEnabled(packJobPanel.getRemoveButton(), evt);
            } else if (evt.getPropertyName().startsWith("UPDATE_STOP_BUTTON")) {
              updateButtonEnabled(packJobPanel.getStopButton(), evt);
            }
          });
    }
  }
}
