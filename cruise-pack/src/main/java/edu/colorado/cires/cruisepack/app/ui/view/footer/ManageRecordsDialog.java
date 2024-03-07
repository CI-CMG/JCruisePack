package edu.colorado.cires.cruisepack.app.ui.view.footer;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.ui.controller.CruiseDataController;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.OptionDialog;
import jakarta.annotation.PostConstruct;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ManageRecordsDialog extends JDialog implements ReactiveView {
  private final CruiseDataController cruiseDataController;
  private final ReactiveViewRegistry reactiveViewRegistry;
  
  private final JTable table = new JTable();
  private final DefaultTableModel tableModel = new CheckboxTableModel();
  private final JButton saveButton = new JButton("Save");
  private final OptionDialog exitAfterSaveDialog = new OptionDialog(
      "<html><B>Package display status was updated. Do you want to exit editor?</B></html>",
      List.of("No", "Yes")
  );

  @Autowired
  public ManageRecordsDialog(CruiseDataController cruiseDataDatastore, ReactiveViewRegistry reactiveViewRegistry) {
    super((JFrame) null, "Manage Package Records", true);
    this.cruiseDataController = cruiseDataDatastore;
    this.reactiveViewRegistry = reactiveViewRegistry;
  }
  
  @PostConstruct
  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }
  
  private void initializeFields() {
    table.setModel(tableModel);
    table.getColumn("").setMinWidth(0);
    table.getColumn("").setMaxWidth(0);
    
    addCruiseDataToTable();
  }
  
  private void resetTable() {
    int rowCount = tableModel.getRowCount();
    while (rowCount > 0) {
      tableModel.removeRow(rowCount - 1);
      rowCount = tableModel.getRowCount();
    }
    
    addCruiseDataToTable();
  }
  
  private void addCruiseDataToTable() {
    cruiseDataController.getCruises().forEach(cruiseData ->
        tableModel.addRow(
            new Object[]{ cruiseData.isUse(), cruiseData.getPackageId(), cruiseData, false }
        )
    );
  }
  
  private void setupLayout() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    
    panel.add(new JScrollPane(table), configureLayout(0, 0, c -> c.weighty = 100));
    panel.add(saveButton, configureLayout(0, 1, c -> c.weighty = 0));
    
    add(panel);
  }
  
  private void setupMvc() {
    reactiveViewRegistry.register(this);
    
    exitAfterSaveDialog.addListener("Yes", (evt) -> setVisible(false));
    saveButton.addActionListener((evt) -> save());
  }
  
  private void save() {
    final int rowCount = tableModel.getRowCount();
    List<CruiseData> newData = new ArrayList<>(0);
    for (int i = 0; i < rowCount; i++) {
      CruiseData existingData = (CruiseData) tableModel.getValueAt(i, 2);
      boolean use = (boolean) tableModel.getValueAt(i, 0);
      boolean delete = (boolean) tableModel.getValueAt(i, 3); 
      newData.add(CruiseData.builder(existingData)
          .withUse(use)
          .withDelete(delete)
          .build());
    }
    cruiseDataController.updateCruises(newData);

    exitAfterSaveDialog.pack();
    exitAfterSaveDialog.setVisible(true);
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(Events.UPDATE_CRUISE_DATA_STORE)) {
      resetTable();
    }
  }
  
  public static class CheckboxTableModel extends DefaultTableModel {
    public CheckboxTableModel() {
      super(new String[] {"Show", "Package Name", "", "Delete"}, 0);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
      Class<?> clazz = String.class;
      
      if (columnIndex == 0) {
        clazz = Boolean.class;
      }
      
      if (columnIndex == 2) {
        clazz = CruiseData.class;
      }
      
      if (columnIndex == 3) {
        clazz = Boolean.class;
      }
      
      return clazz;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return column == 0 || column == 3;
    }
  }
}
