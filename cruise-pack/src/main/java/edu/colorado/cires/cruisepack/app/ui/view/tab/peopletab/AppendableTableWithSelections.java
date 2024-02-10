package edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AppendableTableWithSelections extends JPanel {
  
  private final String[] valueList;
  private final String tableHeader;
  private final String addText;

  public AppendableTableWithSelections(String[] valueList, String tableHeader, String addText) {
    this.valueList = valueList;
    this.tableHeader = tableHeader;
    this.addText = addText;
    
    init();
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
    JPanel listingValuesPannel = new JPanel();
    listingValuesPannel.setLayout(new GridBagLayout());
    listingValuesPannel.setBackground(new Color(Color.WHITE.getRGB()));
    listingValuesPannel.add(new JComboBox<>(valueList), configureLayout(0, 0));
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
      listingValuesPannel.add(new JComboBox<>(valueList), configureLayout(0, listingValuesPannel.getComponents().length));
      revalidate();
    });
    panel.add(addButton, configureLayout(0, 2, c -> {
      c.weighty = 0;
    }));

    add(panel, configureLayout(0, 0, c -> c.weighty = 100));
  }

}
