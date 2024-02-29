package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.DropDownItemController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.DropDownItemModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

public class DropDownItemPanel extends JPanel implements ReactiveView {
  
  private final DropDownItemController controller;
  private final DropDownItemModel model;
  private List<DropDownItem> options;
  private final DropDownItem defaultOption;
  
  private final List<ComponentEventListener<DropDownItemPanel>> removeListeners = new ArrayList<>(0);
  
  private final JComboBox<DropDownItem> comboBox = new JComboBox<>();
  private final JButton removeButton = new JButton("Remove");
  
  public DropDownItemPanel(List<DropDownItem> options, DropDownItem defaultOption) {
    this.defaultOption = defaultOption;
    this.model = new DropDownItemModel(defaultOption);
    this.controller = new DropDownItemController(this.model, this);
    this.options = options;
    
    init();
  }
  
  private void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }
  
  private void initializeFields() {
    comboBox.setModel(new DefaultComboBoxModel<>(options.toArray(new DropDownItem[0])));
    comboBox.setSelectedItem(controller.getItem());
  }
  
  private void setupLayout() {
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);
    
    add(removeButton, configureLayout(0, 0, c -> c.weightx = 0));
    add(comboBox, configureLayout(1, 0, c -> c.weightx = 100));
  }
  
  private void setupMvc() {
    removeButton.addActionListener((evt) -> {
      for (ComponentEventListener<DropDownItemPanel> listener : removeListeners) {
        listener.handle(this);
      }
    });
    
    comboBox.addItemListener((evt) -> {
      DropDownItem item = (DropDownItem) evt.getItem();
      controller.setItem(item);
    });
  }
  
  public DropDownItemModel getModel() {
    return model;
  }
  
  public void addRemoveListener(ComponentEventListener<DropDownItemPanel> listener) {
    removeListeners.add(listener);
  }
  
  public void updateOptions(List<DropDownItem> options) {
    this.options = options;
    
    DropDownItem selectedItem = (DropDownItem) comboBox.getSelectedItem();
    boolean currentSelectedItemExists = selectedItem != null && options.stream()
            .anyMatch(dd -> dd.getId().equals(selectedItem.getId()));
    
    comboBox.setModel(new DefaultComboBoxModel<>(options.toArray(new DropDownItem[0])));
    comboBox.setSelectedItem(currentSelectedItemExists ? selectedItem : defaultOption);
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(DropDownItemModel.UPDATE_ITEM)) {
      updateComboBox(comboBox, evt);
    } else if (evt.getPropertyName().equals(DropDownItemModel.UPDATE_ITEM_ERROR)) {
      String errorMessage = (String) evt.getNewValue();
      List<DropDownItem> newOptions = new ArrayList<>(options);
      DropDownItem selected = (DropDownItem) comboBox.getSelectedItem();
      if (errorMessage != null) {
        selected = new DropDownItem("", errorMessage);
        newOptions.add(selected);
        comboBox.setRenderer(new DefaultListCellRenderer() {
          @Override
          public Component getListCellRendererComponent(
              JList<?> list, Object value, int index,boolean isSelected, boolean cellHasFocus
          ) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (((DropDownItem) value).getValue().equals(errorMessage)) {
              setForeground(Color.RED);
              comboBox.setForeground(Color.RED);
            } else {
              setForeground(Color.BLACK);
              comboBox.setForeground(Color.BLACK);
            }

            return this;
          }
        });
      }
      comboBox.setModel(new DefaultComboBoxModel<>(newOptions.toArray(new DropDownItem[0])));
      comboBox.setSelectedItem(selected);
    }
  }
}
