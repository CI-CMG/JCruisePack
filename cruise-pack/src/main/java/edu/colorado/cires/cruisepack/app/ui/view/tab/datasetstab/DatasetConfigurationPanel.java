package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.view.UiRefresher;
import jakarta.annotation.PostConstruct;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatasetConfigurationPanel extends JPanel {

  private final UiRefresher uiRefresher;

  private List<DatasetPanel> rows = new ArrayList<>();
  private JPanel fluff = new JPanel();

  @Autowired
  public DatasetConfigurationPanel(UiRefresher uiRefresher) {
    this.uiRefresher = uiRefresher;
  }

  @PostConstruct
  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void initializeFields() {

  }

  private void addFluff() {
    add(fluff, configureLayout(0, rows.size(), c -> {
      c.fill = GridBagConstraints.BOTH;
      c.weighty = 1;
      c.gridwidth = GridBagConstraints.REMAINDER;
    }));
  }

  private void setupLayout() {
    fluff.setBackground(Color.WHITE);

    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);

    addFluff();

  }

  private void setupMvc() {

  }


  public void addRow() {
    UndefinedDatasetPanel row = new UndefinedDatasetPanel(this);
    row.init();
    remove(fluff);
    add(row, configureLayout(0, rows.size(), c -> {
      c.fill = GridBagConstraints.HORIZONTAL;
      c.weighty = 0;
      c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    rows.add(row);
    addFluff();
    uiRefresher.refresh();
  }

  public void removeRow(DatasetPanel row) {
    remove(row);
    rows.remove(row);
    uiRefresher.refresh();
  }
}
