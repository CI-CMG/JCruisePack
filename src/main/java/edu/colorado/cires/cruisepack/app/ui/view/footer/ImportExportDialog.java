package edu.colorado.cires.cruisepack.app.ui.view.footer;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.ExportController;
import edu.colorado.cires.cruisepack.app.ui.controller.ImportController;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import java.awt.Frame;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ImportExportDialog extends JDialog {
  
  private ImportDialog importDialog;
  private final ExportController exportController;
  private final ImportController importController;
  private final PersonDatastore personDatastore;
  private final ReactiveViewRegistry reactiveViewRegistry;
  
  private final JButton importButton = new JButton("Import Excel Sheet");
  private final JButton exportButton = new JButton("Export JSON");
  
  public ImportExportDialog(Frame owner, ExportController exportController, ImportController importController, PersonDatastore personDatastore,
      ReactiveViewRegistry reactiveViewRegistry) {
    super(owner, "Import/Export", true);
    this.exportController = exportController;
    this.importController = importController;
    this.personDatastore = personDatastore;
    this.reactiveViewRegistry = reactiveViewRegistry;
    setLocationRelativeTo(owner);
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
      if (importDialog == null) {
        importDialog = new ImportDialog(
            (Frame) SwingUtilities.getWindowAncestor(this), 
            personDatastore,
            importController,
            reactiveViewRegistry
        );
      }
      
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
