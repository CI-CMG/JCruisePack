package edu.colorado.cires.cruisepack.app.ui.view.footer;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createLabelWithErrorPanel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updatePathField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ImportController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ImportDialog extends JDialog implements ReactiveView {
  
  private final PersonDatastore personDatastore;
  private final ImportController importController;
  private final ReactiveViewRegistry reactiveViewRegistry;
  
  private final JTextField importPathField = new JTextField();
  private final JLabel importPathErrorLabel = createErrorLabel();
  private final JTextField destinationPathField = new JTextField();
  private final JLabel destinationPathErrorLabel = createErrorLabel();
  private final JComboBox<DropDownItem> metadataAuthorField = new JComboBox<>();
  private final JLabel metadataAuthorErrorLabel = createErrorLabel();
  private final JButton importButton = new JButton("Import Records");
  private final JButton selectFileButton = new JButton("Select Excel File");
  private final JButton selectDirectoryButton = new JButton("Select Directory");
  private final JButton saveButton = new JButton("Download Template");

  public ImportDialog(Frame owner, PersonDatastore personDatastore, ImportController importController, ReactiveViewRegistry reactiveViewRegistry) {
    super(owner, "Import Excel Spreadsheet", true);
    this.personDatastore = personDatastore;
    this.importController = importController;
    this.reactiveViewRegistry = reactiveViewRegistry;
    setLocationRelativeTo(owner);
    init();
  }

  private void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }
  
  private void initializeFields() {
    importPathField.setText(importController.getImportPath() == null ? null : importController.getImportPath().toAbsolutePath().normalize().toString());
    destinationPathField.setText(importController.getDestinationPath() == null ? null : importController.getDestinationPath().toAbsolutePath().normalize().toString());
    metadataAuthorField.setModel(new DefaultComboBoxModel<>(
        personDatastore.getEnabledPersonDropDowns().toArray(new DropDownItem[0])
    ));
    metadataAuthorField.setSelectedItem(importController.getMetadataAuthor());
  }
  
  private void setupLayout() {
    setLayout(new GridBagLayout());

    JPanel templatePanel = new JPanel();
    templatePanel.setLayout(new GridBagLayout());
    templatePanel.add(new JLabel(
        "Import cruise metadata using an Excel spreadsheet. The file must be in the proper format. Download template if needed."
    ), configureLayout(0, 0, c -> c.weightx = 100));
    templatePanel.add(saveButton, configureLayout(1, 0, c -> c.weightx = 0));
    add(templatePanel, configureLayout(0, 0, c -> c.weighty = 0));

    JPanel pathPanel = new JPanel();
    pathPanel.setLayout(new GridBagLayout());
    pathPanel.add(createLabelWithErrorPanel("Path to Excel Spreadsheet", importPathErrorLabel), configureLayout(0, 0, c -> c.weightx = 100));
    pathPanel.add(importPathField, configureLayout(0, 1, c -> c.weightx = 100));
    pathPanel.add(selectFileButton, configureLayout(1, 1, c -> c.weightx = 0));
    add(pathPanel, configureLayout(0, 1, c -> c.weighty = 100));

    JPanel destinationPanel = new JPanel();
    destinationPanel.setLayout(new GridBagLayout());
    destinationPanel.add(createLabelWithErrorPanel("Destination (package directory will be created automatically)", destinationPathErrorLabel), configureLayout(0, 0, c -> c.weightx = 100));
    destinationPanel.add(destinationPathField, configureLayout(0, 1, c -> c.weightx = 100));
    destinationPanel.add(selectDirectoryButton, configureLayout(1, 1, c -> c.weightx = 0));
    add(destinationPanel, configureLayout(0, 2, c -> c.weighty = 100));
    
    JPanel metadataAuthorPanel = new JPanel();
    metadataAuthorPanel.setLayout(new GridBagLayout());
    metadataAuthorPanel.add(createLabelWithErrorPanel("Data Packager", metadataAuthorErrorLabel), configureLayout(0, 0));
    metadataAuthorPanel.add(metadataAuthorField, configureLayout(0, 1));
    add(metadataAuthorPanel, configureLayout(0, 3, c -> c.weighty = 100));
    
    add(importButton, configureLayout(0, 4, c -> c.weighty = 0));
  }
  
  private void setupMvc() {
    reactiveViewRegistry.register(this);
    
    importPathField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        handlePathValue(importPathField.getText(), importController::setImportPath);
      }
    });
    destinationPathField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        handlePathValue(destinationPathField.getText(), importController::setDestinationPath);
      }
    });
    metadataAuthorField.addItemListener((evt) -> importController.setMetadataAuthor((DropDownItem) evt.getItem()));
    
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        importController.resetState();
      }
    });
    
    importButton.addActionListener((evt) -> {
      boolean success = importController.importCruises();
      if (success) {
        setVisible(false);
      }
    });
    
    selectFileButton.addActionListener((evt) -> handleFileSelect());
    selectDirectoryButton.addActionListener((evt) -> handleDirectorySelect(importController::setDestinationPath));
    saveButton.addActionListener((evt) -> handleDirectorySelect(importController::saveTemplate));
  }

  private void handlePathValue(String value, Consumer<Path> consumer) {
    Path path = Paths.get(value);
    consumer.accept(path.toAbsolutePath().normalize());
  }

  private void handleDirectorySelect(Consumer<Path> consumer) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
      consumer.accept(fileChooser.getSelectedFile().toPath().toAbsolutePath().normalize());
    }
  }
  
  private void handleFileSelect() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
      importController.setImportPath(fileChooser.getSelectedFile().toPath().toAbsolutePath().normalize());
    }
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.UPDATE_IMPORT_PATH -> updatePathField(importPathField, evt);
      case Events.UPDATE_IMPORT_PATH_ERROR -> updateLabelText(importPathErrorLabel, evt);
      case Events.UPDATE_IMPORT_DESTINATION_PATH -> updatePathField(destinationPathField, evt);
      case Events.UPDATE_IMPORT_DESTINATION_PATH_ERROR -> updateLabelText(destinationPathErrorLabel, evt);
      case Events.UPDATE_IMPORT_METADATA_AUTHOR -> updateComboBox(metadataAuthorField, evt);
      case Events.UPDATE_IMPORT_METADATA_AUTHOR_ERROR -> updateLabelText(metadataAuthorErrorLabel, evt);
    }
  }
}
