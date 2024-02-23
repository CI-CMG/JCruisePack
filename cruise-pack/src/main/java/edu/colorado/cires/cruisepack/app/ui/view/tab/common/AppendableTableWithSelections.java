package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

public class AppendableTableWithSelections extends JComponent {

  private static final Color ERROR_COLOR = new Color(Color.RED.getRGB());
  
  private final String tableHeader;
  private final String addText;
  private final DropDownItem defaultValue;
  private List<DropDownItem> options;

  private final JPanel listingValuesPannel = new JPanel();
  private final JLabel errorLabel = new JLabel();
  private final List<ValuesChangedListener> listeners = new ArrayList<>(0);

  public AppendableTableWithSelections(String tableHeader, String addText, DropDownItem defaultValue, List<DropDownItem> options) {
    this.tableHeader = tableHeader;
    this.addText = addText;
    this.defaultValue = defaultValue;
    this.options = options;
    init();
  }

  public void setOptions(List<DropDownItem> items) {
    this.options = items;
  }

  public void addValuesChangedListener(ValuesChangedListener listener) {
    listeners.add(listener);
  }

  public void removeValuedChangedListener(ValuesChangedListener listener) {
    listeners.remove(listener);
  }
  
  
  private void init() {
    setLayout(new GridBagLayout());

    JPanel pannel = new JPanel();
    pannel.setLayout(new GridBagLayout());

    JPanel labelPanel = new JPanel();
    labelPanel.setLayout(new BorderLayout(10, 0));
    labelPanel.add(new JLabel(tableHeader), BorderLayout.LINE_START);
    errorLabel.setForeground(ERROR_COLOR);
    labelPanel.add(errorLabel, BorderLayout.CENTER);

    pannel.add(labelPanel, configureLayout(0, 0, c -> {
      c.weighty = 0;
    }));

    JPanel listingPanel = new JPanel();
    listingPanel.setBackground(new Color(Color.WHITE.getRGB()));
    listingPanel.setLayout(new GridBagLayout());
    listingValuesPannel.setLayout(new GridBagLayout());
    listingValuesPannel.setBackground(new Color(Color.WHITE.getRGB()));
    JPanel fillPanel = new JPanel();
    fillPanel.setBackground(new Color(Color.WHITE.getRGB()));

    listingPanel.add(listingValuesPannel, configureLayout(0, 0, c -> {
      c.weighty = 0;
    }));
    listingPanel.add(fillPanel, configureLayout(0, 1, c -> {
      c.weighty = 100;
    }));

    pannel.add(new JScrollPane(listingPanel), configureLayout(0, 1, c -> {
      c.weighty = 100;
    }));

    JButton addButton = new JButton(addText);
    addButton.addActionListener(e -> {
      handleAddChange();
    });
    pannel.add(addButton, configureLayout(0, 2, c -> {
      c.weighty = 0;
    }));

    add(pannel, configureLayout(0, 0, c -> c.weighty = 100));
  }

  private void handleUpdateChange() {
    List<DropDownItem> items = getDropDowns();

    for (ValuesChangedListener listener : listeners) {
      listener.handleValuesChanged(items);
    }
  }

  private void handleAddChange() {
    List<DropDownItem> items = getDropDowns();

    items.add(defaultValue);

    for (ValuesChangedListener listener : listeners) {
      listener.handleValuesChanged(items);
    }
  }

  public List<DropDownItem> getDropDowns() {
    return Arrays.stream(this.listingValuesPannel.getComponents())
    .map(c -> (JComboBox<?>) c)
    .map(c -> (DropDownItem) c.getSelectedItem())
    .collect(Collectors.toList());
  }

  public void redrawComboboxes(List<DropDownItem> dropDownItems) {
    listingValuesPannel.removeAll();

    for (DropDownItem item : dropDownItems) {
      JComboBox<DropDownItem> comboBox = new JComboBox<>();
      List<DropDownItem> itemOptions = new ArrayList<>(options);
      final String errorMessage = item.getErrorMessage();
      if (errorMessage != null) {
        item = new DropDownItem("", errorMessage);
        itemOptions.add(item);
        comboBox.setRenderer(new DefaultListCellRenderer() {
          @Override
          public Component getListCellRendererComponent(
            JList<?> list, Object value, int index,boolean isSelected, boolean cellHasFocus
          ) {
              super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

              if (((DropDownItem) value).getValue().equals(errorMessage)) {
                  setForeground(ERROR_COLOR);
                  comboBox.setForeground(ERROR_COLOR);
              } else {
                  setForeground(Color.BLACK);
              }
              
              return this;
          }
        });
      }
      comboBox.setModel(new DefaultComboBoxModel<>(itemOptions.toArray(new DropDownItem[0])));
      comboBox.setSelectedItem(item);

      comboBox.addItemListener((e) -> handleUpdateChange());

      listingValuesPannel.add(comboBox, configureLayout(0, listingValuesPannel.getComponents().length));
    }

    revalidate();
  }

  public JLabel getErrorLabel() {
    return errorLabel;
  }

}
