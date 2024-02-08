package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.view.UiRefresher;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.annotation.PostConstruct;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatasetConfigurationPanel extends JPanel {

  private static final String DIALOG_MESSAGE = "Select Dataset Type";
  private static final String DIALOG_TITLE = "New Dataset";

  private final UiRefresher uiRefresher;
  private final InstrumentDatastore instrumentDatastore;
  private final DatasetPanelFactoryResolver datasetPanelFactoryResolver;

  private List<DatasetPanel> rows = new ArrayList<>();
  private JPanel fluff = new JPanel();
  private DropDownItem[] dataTypes;

  @Autowired
  public DatasetConfigurationPanel(
      UiRefresher uiRefresher,
      InstrumentDatastore instrumentDatastore, DatasetPanelFactoryResolver datasetPanelFactoryResolver) {
    this.uiRefresher = uiRefresher;
    this.instrumentDatastore = instrumentDatastore;
    this.datasetPanelFactoryResolver = datasetPanelFactoryResolver;
  }

  @PostConstruct
  public void init() {
    dataTypes = resolveDataTypes();
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

  private DropDownItem[] resolveDataTypes() {
    List<DropDownItem> fromData = new ArrayList<>(instrumentDatastore.getDatasetTypeDropDowns());
    Set<String> implemented = datasetPanelFactoryResolver.getImplementedShortCodes();
    Iterator<DropDownItem> it = fromData.iterator();
    while (it.hasNext()) {
      DropDownItem type = it.next();
      if (!type.equals(InstrumentDatastore.UNSELECTED_DATASET_TYPE) && !implemented.contains(type.getId())) {
        it.remove();
      }
    }
    return fromData.toArray(new DropDownItem[0]);
  }

  public void addRow() {
    DropDownItem dataType = (DropDownItem) JOptionPane.showInputDialog(
        this,
        DIALOG_MESSAGE,
        DIALOG_TITLE,
        JOptionPane.PLAIN_MESSAGE,
        null,
        dataTypes,
        InstrumentDatastore.UNSELECTED_DATASET_TYPE);

    if (dataType != null && !dataType.equals(InstrumentDatastore.UNSELECTED_DATASET_TYPE)) {
      DatasetPanel row = datasetPanelFactoryResolver.createDatasetPanel(dataType);
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
  }

  public void removeRow(DatasetPanel row) {
    remove(row);
    rows.remove(row);
    uiRefresher.refresh();
  }
}
