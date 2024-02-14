package edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;

public class AppendableTableWithSelections extends JComponent {
  
  private final String tableHeader;
  private final String addText;
  private final DropDownItem defaultValue;
  private final List<DropDownItem> options;

  private final JPanel listingValuesPannel = new JPanel();
  private final List<ValuesChangedListener> listeners = new ArrayList<>(0);

  public AppendableTableWithSelections(String tableHeader, String addText, DropDownItem defaultValue, List<DropDownItem> options) {
    this.tableHeader = tableHeader;
    this.addText = addText;
    this.defaultValue = defaultValue;
    this.options = options;
    init();
  }

  public void addValuesChangedListener(ValuesChangedListener listener) {
    listeners.add(listener);
  }

  public void removeValuedChangedListener(ValuesChangedListener listener) {
    listeners.remove(listener);
  }
  
  
  private void init() {
    setLayout(new GridBagLayout());

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    panel.add(new JLabel(tableHeader), configureLayout(0, 0, c -> {
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

    panel.add(new JScrollPane(listingPanel), configureLayout(0, 1, c -> {
      c.weighty = 100;
    }));

    JButton addButton = new JButton(addText);
    addButton.addActionListener(e -> {
      handleAddChange();
    });
    panel.add(addButton, configureLayout(0, 2, c -> {
      c.weighty = 0;
    }));

    add(panel, configureLayout(0, 0, c -> c.weighty = 100));
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

  private List<DropDownItem> getDropDowns() {
    return Arrays.stream(this.listingValuesPannel.getComponents())
    .map(c -> (JComboBox<?>) c)
    .map(c -> (DropDownItem) c.getSelectedItem())
    .collect(Collectors.toList());
  }

  public void redrawComboboxes(List<DropDownItem> dropDownItems) {
    listingValuesPannel.removeAll();

    for (DropDownItem item : dropDownItems) {
      JComboBox<DropDownItem> comboBox = new JComboBox<>();
      comboBox.setModel(new DefaultComboBoxModel<>(options.toArray(new DropDownItem[0])));
      comboBox.setSelectedItem(item);

      comboBox.addItemListener((e) -> handleUpdateChange());

      listingValuesPannel.add(comboBox, configureLayout(0, listingValuesPannel.getComponents().length));
    }

    revalidate();
  }

}
