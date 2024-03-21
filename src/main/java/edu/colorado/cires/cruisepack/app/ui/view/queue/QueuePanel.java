package edu.colorado.cires.cruisepack.app.ui.view.queue;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.controller.queue.QueueController;
import edu.colorado.cires.cruisepack.app.ui.model.queue.QueueModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.DropDownItemList;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.DropDownItemPanel;
import jakarta.annotation.PostConstruct;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class QueuePanel extends JPanel implements ReactiveView {
  
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final QueueController queueController;
  private final CruiseDataDatastore cruiseDataDatastore;
  
  private DropDownItemList itemList;
  private final JButton processAllButton = new JButton("Package Queue");
  private final JButton clearButton = new JButton("Clear Queue");

  @Autowired
  public QueuePanel(ReactiveViewRegistry reactiveViewRegistry, QueueController queueController, CruiseDataDatastore cruiseDataDatastore) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.queueController = queueController;
    this.cruiseDataDatastore = cruiseDataDatastore;
  }

  @PostConstruct
  public void init() {
    reactiveViewRegistry.register(this);
    
    initializeFields();
    setupLayout();
    setupMvc();
  }
  
  private void initializeFields() {
    itemList = new DropDownItemList(
        null,
        "Add Item",
        cruiseDataDatastore.getDropDownItems(),
        CruiseDataDatastore.UNSELECTED_CRUISE
    );
  }
  
  private void setupLayout() {
    setLayout(new GridBagLayout());
    add(itemList, configureLayout(0, 0, c -> c.weighty = 100));
    
    JPanel footerButtonPanel = new JPanel();
    footerButtonPanel.setLayout(new BorderLayout());
    footerButtonPanel.add(processAllButton, BorderLayout.EAST);
    footerButtonPanel.add(clearButton, BorderLayout.WEST);
    add(footerButtonPanel, configureLayout(0, 1, c -> c.weighty = 0));
  }
  
  private void setupMvc() {
    itemList.addAddItemListener(queueController::addToQueue);
    itemList.addRemoveItemListener(queueController::removeFromQueue);
    clearButton.addActionListener((evt) -> queueController.clearQueue());
    processAllButton.addActionListener((evt) -> queueController.packageQueue());
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case QueueModel.ADD_TO_QUEUE -> itemList.addItem(
          (DropDownItemPanel) evt.getNewValue()
      );
      case QueueModel.REMOVE_FROM_QUEUE -> itemList.removeItem(
          (DropDownItemPanel) evt.getOldValue()
      );
      case QueueModel.CLEAR_QUEUE -> itemList.clearItems();
      case QueueModel.UPDATE_QUEUE_ERROR -> updateLabelText(
          itemList.getErrorLabel(), evt
      );
      case Events.UPDATE_CRUISE_DATA_STORE -> itemList.updateOptions(
          cruiseDataDatastore.getDropDownItems()
      );
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
    }
  }
}
