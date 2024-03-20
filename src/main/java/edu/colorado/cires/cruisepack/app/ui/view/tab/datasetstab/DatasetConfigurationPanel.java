package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.AdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.UiRefresher;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.cruisetab.CruiseDocumentsPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.documents.DocumentsPanel;
import jakarta.annotation.PostConstruct;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class DatasetConfigurationPanel extends JPanel implements ReactiveView {

  private static final String DIALOG_MESSAGE = "Select Dataset Type";
  private static final String DIALOG_TITLE = "New Dataset";

  private final UiRefresher uiRefresher;
  private final InstrumentDatastore instrumentDatastore;
  private final DatasetPanelFactoryResolver datasetPanelFactoryResolver;
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final List<DatasetListener> datasetCreatedListeners = new ArrayList<>(0);
  private final List<DatasetListener> datasetRemovedListeners = new ArrayList<>(0);
  private final CruiseDocumentsPanel cruiseDocumentsPanel;

  private List<JPanel> rows = new ArrayList<>();
  private JLabel datasetsErrorLabel = createErrorLabel();
  private JPanel fluff = new JPanel();
  private DropDownItem[] dataTypes;

  @Autowired
  public DatasetConfigurationPanel(
      UiRefresher uiRefresher,
      InstrumentDatastore instrumentDatastore, DatasetPanelFactoryResolver datasetPanelFactoryResolver, ReactiveViewRegistry reactiveViewRegistry,
      CruiseDocumentsPanel cruiseDocumentsPanel) {
    this.uiRefresher = uiRefresher;
    this.instrumentDatastore = instrumentDatastore;
    this.datasetPanelFactoryResolver = datasetPanelFactoryResolver;
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.cruiseDocumentsPanel = cruiseDocumentsPanel;
  }

  public void addDatasetCreatedListener(DatasetListener listener) {
    datasetCreatedListeners.add(listener);
  }

  public void removeDatasetCreatedListener(DatasetListener listener) {
    datasetCreatedListeners.remove(listener);
  }

  public void addDatasetRemovedListener(DatasetListener listener) {
    datasetRemovedListeners.add(listener);
  }

  public void removeDatasetRemovedListener(DatasetListener listener) {
    datasetRemovedListeners.remove(listener);
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
    fluff.add(datasetsErrorLabel, configureLayout(0, 0, c -> {
      c.weightx = 1;
    }));
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
    reactiveViewRegistry.register(this);
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
      if (dataType.getId().equals(InstrumentGroupName.DOCUMENTS.getShortName())) {
        if (rows.stream().noneMatch(p -> p instanceof DocumentsPanel)) {
          createDocumentsPanel();
        }
      } else {
        DatasetPanel<? extends AdditionalFieldsModel, ?> row = datasetPanelFactoryResolver.createDatasetPanel(dataType);
        for (DatasetListener listener : datasetCreatedListeners) {
          listener.handle(row);
        }
      }
    }
  }
  
  private void createDocumentsPanel() {
    DocumentsPanel documentsPanel = new DocumentsPanel(cruiseDocumentsPanel);
    documentsPanel.addRemovedListener((p) -> {
      removeRow(p);
      p.restoreDefaultState();
      
    });
    remove(fluff);
    add(documentsPanel, configureLayout(0, rows.size(), c -> {
      c.fill = GridBagConstraints.HORIZONTAL;
      c.weighty = 0;
      c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    rows.add(documentsPanel);
    addFluff();
    uiRefresher.refresh();
  }
  
  public void createRow(DatasetPanel<? extends AdditionalFieldsModel, ?> row) {
    row.addDatasetRemovedListener((evt) -> handleRowRemove(row));
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

  public void removeRow(JPanel row) {
    remove(row);
    rows.remove(row);
    uiRefresher.refresh();
  }
  
  public void removeAllRows() {
    List<JPanel> panels = new ArrayList<>(rows);
    for (JPanel panel : panels) {
      removeRow(panel);
    }
  }
  
  private void handleRowRemove(DatasetPanel<? extends AdditionalFieldsModel, ?> row) {
    for (DatasetListener listener : datasetRemovedListeners) {
      listener.handle(row);
    }
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.UPDATE_DATASETS_ERROR:
        updateLabelText(datasetsErrorLabel, evt);
        break;
      default:
        break;
    }
  }
}
