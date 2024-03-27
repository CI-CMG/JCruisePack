package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createLabelWithErrorPanel;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class DropDownItemList extends JComponent {
  
  private final JPanel listingsPanel = new JPanel();
  private final JLabel errorLabel = createErrorLabel();
  private final JPanel fluff = new JPanel();

  private final String headerLabelText;
  private final JButton addButton;
  private List<DropDownItem> options;
  private final DropDownItem defaultOption;
  
  private List<DropDownItemPanel> dropDownItems;
  private final List<ComponentEventListener<DropDownItemPanel>> addItemListeners = new ArrayList<>(0);
  private final List<ComponentEventListener<DropDownItemPanel>> removeItemListeners = new ArrayList<>(0);
  private final boolean editableItems;
  
  public DropDownItemList(String headerLabelText, String addButtonText, List<DropDownItem> options, DropDownItem defaultOption) {
    this.headerLabelText = headerLabelText;
    this.addButton = new JButton(addButtonText);
    this.options = options;
    this.defaultOption = defaultOption;
    editableItems = false;
    
    init();
  }

  public DropDownItemList(String headerLabelText, String addButtonText, List<DropDownItem> options, DropDownItem defaultOption, boolean editableItems) {
    this.headerLabelText = headerLabelText;
    this.addButton = new JButton(addButtonText);
    this.options = options;
    this.defaultOption = defaultOption;
    this.editableItems = editableItems;

    init();
  }
  
  public void addAddItemListener(ComponentEventListener<DropDownItemPanel> listener) {
    addItemListeners.add(listener);
  }
  
  public void addRemoveItemListener(ComponentEventListener<DropDownItemPanel> listener) {
    removeItemListeners.add(listener);
  }
  
  public void addItem(DropDownItemPanel panel) {
    panel.addRemoveListener((p) -> {
      for (ComponentEventListener<DropDownItemPanel> listener : removeItemListeners) {
        listener.handle(panel);
      }
    });
    listingsPanel.remove(fluff);
    listingsPanel.add(panel, configureLayout(0, dropDownItems.size(), c -> c.weighty = 0));
    dropDownItems.add(panel);
    listingsPanel.add(fluff, configureLayout(0, dropDownItems.size(), c -> c.weighty = 100));
    
    revalidate();
  }
  
  public void removeItem(DropDownItemPanel panel) {
    listingsPanel.remove(panel);
    dropDownItems.remove(panel);
    
    revalidate();
  }
  
  public void clearItems() {
    int nItems = dropDownItems.size();
    while (nItems > 0) {
      removeItem(dropDownItems.get(nItems - 1));
      nItems = dropDownItems.size();
    }
  }
  
  public void updateOptions(List<DropDownItem> options) {
    this.options = options;
    dropDownItems.forEach(item -> item.updateOptions(options));
  }
  
  public JLabel getErrorLabel() {
    return errorLabel;
  }
  
  private void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }
  
  private void initializeFields() {
    dropDownItems = new ArrayList<>(0);
  }
  
  private void setupLayout() {
    setLayout(new GridBagLayout());
    add(createLabelWithErrorPanel(headerLabelText, errorLabel), configureLayout(0, 0, c -> c.weighty = 0));
    
    fluff.setBackground(Color.WHITE);
    
    listingsPanel.setLayout(new GridBagLayout());
    listingsPanel.setBackground(Color.WHITE);
    listingsPanel.add(fluff, configureLayout(0, dropDownItems.size(), c -> c.weighty = 100));
    
    add(new JScrollPane(listingsPanel), configureLayout(0, 1, c -> c.weighty = 100));
    add(addButton, configureLayout(0, 2, c -> c.weighty = 0));
  }
  
  private void setupMvc() {
    addButton.addActionListener((evt) -> {
      DropDownItemPanel panel = new DropDownItemPanel(options, defaultOption, true);
      
      for (ComponentEventListener<DropDownItemPanel> listener : addItemListeners) {
        listener.handle(panel);
      }
    });
  }
  
}
