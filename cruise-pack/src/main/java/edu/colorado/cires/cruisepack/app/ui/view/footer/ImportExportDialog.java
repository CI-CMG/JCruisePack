package edu.colorado.cires.cruisepack.app.ui.view.footer;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.springframework.stereotype.Component;

@Component
public class ImportExportDialog extends JDialog {
  
  public ImportExportDialog() {
    super((JFrame) null, "Import/Export", true);
    init();
  }
  
  private void init() {
    setLayout(new GridBagLayout());
    
    JPanel exportPanel = new JPanel();
    exportPanel.setLayout(new GridBagLayout());
    exportPanel.add(new JLabel("Export"), configureLayout(0, 0));
    JButton exportButton = new JButton("Export JSON");
    exportPanel.add(exportButton, configureLayout(0, 1));
    
    JPanel importPanel = new JPanel();
    importPanel.setLayout(new GridBagLayout());
    importPanel.add(new JLabel("Import"), configureLayout(0, 0));
    JButton importButton = new JButton("Import Excel Sheet");
    importPanel.add(importButton, configureLayout(0, 1));
    
    add(exportPanel, configureLayout(0, 0, c -> c.weighty = 100));
    add(importPanel, configureLayout(0, 1, c -> c.weighty = 100));
  }
}
