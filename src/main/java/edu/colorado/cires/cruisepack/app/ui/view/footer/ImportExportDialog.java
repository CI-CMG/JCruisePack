package edu.colorado.cires.cruisepack.app.ui.view.footer;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.ExportController;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class ImportExportDialog extends JDialog {
  
  private final ImportDialog importDialog;
  private final ExportController exportController;
  
  private final JButton importButton = new JButton("Import Excel Sheet");
  private final JButton exportButton = new JButton("Export JSON");
  
  public ImportExportDialog(ImportDialog importDialog, ExportController exportController) {
    super((JFrame) null, "Import/Export", true);
    this.importDialog = importDialog;
    this.exportController = exportController;
    init();
  }
  
  private void init() {
    setupLayout();
    setupMvc();
  }
  
  private void setupLayout() {
    setLayout(new GridBagLayout());
    
    JPanel exportPanel = new JPanel();
    exportPanel.setLayout(new GridBagLayout());
    exportPanel.add(new JLabel("Export"), configureLayout(0, 0));
    exportPanel.add(exportButton, configureLayout(0, 1));
    
    JPanel importPanel = new JPanel();
    importPanel.setLayout(new GridBagLayout());
    importPanel.add(new JLabel("Import"), configureLayout(0, 0));
    importPanel.add(importButton, configureLayout(0, 1));
    
    add(exportPanel, configureLayout(0, 0, c -> c.weighty = 100));
    add(importPanel, configureLayout(0, 1, c -> c.weighty = 100));
  }
  
  private void setupMvc() {
    importButton.addActionListener((evt) -> {
      importDialog.pack();
      importDialog.setVisible(true);
    });
    exportButton.addActionListener((evt) -> handleDirectorySelect());
  }

  private void handleDirectorySelect() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
      exportController.exportCruise(fileChooser.getSelectedFile().toPath().toAbsolutePath().normalize());
    }
  }
}
