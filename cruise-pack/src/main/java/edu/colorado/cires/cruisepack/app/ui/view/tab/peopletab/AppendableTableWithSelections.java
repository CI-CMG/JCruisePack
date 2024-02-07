package edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class AppendableTableWithSelections extends JPanel {
  
  private final String[] valueList;
  private final String tableHeader;
  private final String addText;
  private final String selectText;

  public AppendableTableWithSelections(String[] valueList, String tableHeader, String addText, String selectText) {
    this.valueList = valueList;
    this.tableHeader = tableHeader;
    this.addText = addText;
    this.selectText = selectText;
    
    init();
  }
  
  
  private void init() {
    setLayout(new GridBagLayout());
    
    add(new JLabel(tableHeader), configureLayout(0, 0));
    
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    JTable table = new JTable(0, 1);
    table.setRowHeight(25);
    TableColumn column = table.getColumnModel().getColumn(0);
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    renderer.setHorizontalAlignment(SwingConstants.CENTER);
    column.setCellRenderer(renderer);
    column.setCellEditor(new DefaultCellEditor(new JComboBox<>(valueList))); // TODO: Need to find a way to maintain the appearance of JComboBox. Right now this is only text
    column.setCellRenderer((table1, value, isSelected, hasFocus, row, column1) -> new JComboBox<>(valueList));
    addRow(table);
    table.setTableHeader(null);
    add(new JScrollPane(table), configureLayout(0, 1, c -> {
      c.fill = GridBagConstraints.BOTH;
      c.weightx = 100;
      c.weighty = 100;
    }));
    
    JButton button = new JButton(addText);
    button.addActionListener(e -> addRow(table));
    add(button, configureLayout(0, 2));
  }

  private void addRow(JTable table) {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    Vector<JComboBox<String>> vector = new Vector<>();
    vector.add(new JComboBox<>(new String[]{selectText}));
    model.addRow(vector);
  }

}
