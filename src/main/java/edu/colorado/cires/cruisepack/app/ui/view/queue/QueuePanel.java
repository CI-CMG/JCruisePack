package edu.colorado.cires.cruisepack.app.ui.view.queue;

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
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class QueuePanel extends JPanel implements ReactiveView {
  
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final QueueController queueController;
  private final CruiseDataDatastore cruiseDataDatastore;
  private final String processId;
  
  private final List<PackJobPanel> rows = new ArrayList<>(0);
  private final JPanel listingPanel = new JPanel();
  
  private final JPanel fluff = new JPanel();
  private final JButton processAllButton = new JButton("Package Queue");
  private final JButton clearButton = new JButton("Clear Queue");

  @Autowired
  public QueuePanel(ReactiveViewRegistry reactiveViewRegistry, QueueController queueController, CruiseDataDatastore cruiseDataDatastore) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.queueController = queueController;
    this.cruiseDataDatastore = cruiseDataDatastore;
    this.processId = UUID.randomUUID().toString();
  }

  @PostConstruct
  public void init() {
    reactiveViewRegistry.register(this);
    
    initializeFields();
    setupLayout();
    setupMvc();
  }
  
  private void initializeFields() {
  }
  
  private void setupLayout() {
    setLayout(new GridBagLayout());
    
    listingPanel.setLayout(new GridBagLayout());
    listingPanel.add(fluff, configureLayout(0, rows.size(), c -> c.weighty = 100));
    add(new JScrollPane(listingPanel), configureLayout(0, 0, c -> c.weighty = 100));
    
    JPanel footerButtonPanel = new JPanel();
    footerButtonPanel.setLayout(new BorderLayout());
    footerButtonPanel.add(processAllButton, BorderLayout.EAST);
    footerButtonPanel.add(clearButton, BorderLayout.WEST);
    add(footerButtonPanel, configureLayout(0, 1, c -> c.weighty = 0));
  }
  
  private void setupMvc() {
    clearButton.addActionListener((evt) -> queueController.clearQueue());
    processAllButton.addActionListener((evt) -> queueController.packageQueue(processId));
  }
  
  private void addPackJob(PackJobPanel panel) {
    listingPanel.remove(fluff);
    listingPanel.add(panel, configureLayout(0, rows.size(), c -> { c.weighty = 0; c.insets = new Insets(5, 5, 5, 5); }));
    rows.add(panel);
    listingPanel.add(fluff, configureLayout(0, rows.size(), c -> c.weighty = 100));

    panel.addRemoveListener(queueController::removeFromQueue);
    panel.addPackageListener(queueController::submitOne);
    panel.init();
    
    revalidate();
  }
  
  private void removePackJob(PackJobPanel panel) {
    listingPanel.remove(panel);
    rows.remove(panel);
    
    revalidate();
  }
  
  private void clearPackJobs() {
    int nItems = rows.size();
    while (nItems > 0) {
      removePackJob(rows.get(nItems - 1));
      nItems = rows.size();
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
       rows.stream()
           .filter(packJobPanel -> !datastorePackageIds.contains(packJobPanel.getPackJob().getPackageId()))
           .forEach(queueController::removeFromQueue);
      }
      case QueueModel.UPDATE_CLEAR_QUEUE_BUTTON -> {
        boolean enable = (boolean) evt.getNewValue();
        boolean currentEnabled = clearButton.isEnabled();
        if (enable != currentEnabled) {
          clearButton.setEnabled(enable);
        }
      }
      case QueueModel.UPDATE_PACKAGE_QUEUE_BUTTON -> {
        boolean enable = (boolean) evt.getNewValue();
        boolean currentEnabled = processAllButton.isEnabled();
        if (enable != currentEnabled) {
          processAllButton.setEnabled(enable);
        }
      }
      default -> {
        if (evt.getPropertyName().startsWith("UPDATE_PROGRESS")) {
          rows.stream()
              .filter(p -> evt.getPropertyName().endsWith(p.getProcessId()))
              .findFirst()
              .map(PackJobPanel::getProgressBarModel)
              .ifPresent(progressBarModel -> updateProgressBarModel(progressBarModel, evt));

        }
      }
    }
  }
}
