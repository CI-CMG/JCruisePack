package edu.colorado.cires.cruisepack.app.ui.view.footer;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateProgressBarModel;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.migration.SqliteMigrator;
import edu.colorado.cires.cruisepack.app.ui.controller.CruiseDataController;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ExportController;
import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
import edu.colorado.cires.cruisepack.app.ui.controller.ImportController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.FooterControlModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.UiRefresher;
import jakarta.annotation.PostConstruct;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class FooterPanel extends JPanel implements ReactiveView {

  private static final String MANAGE_RECORDS_LABEL = "Manage Records";
  private static final String IMPORT_EXPORT_LABEL = "Import / Export";
  private static final String CLEAR_FORM_LABEL = "Clear Form";
  private static final String STOP_LABEL = "Stop Packaging";
  private static final String SAVE_LABEL = "Save For Later";
  private static final String PACKAGE_LABEL = "Package Data";
  private static final String SETTINGS_LABEL = "Settings";
  private static final String USER_MANUAL_NAME = "CruisePack_manual.pdf";
  private static final String HELP_ICON_NAME = "help.png";

  private final ReactiveViewRegistry reactiveViewRegistry;
  private final FooterControlController footerControlController;
  private final FooterControlModel footerControlModel;
  private ManageRecordsDialog manageRecordsDialog;
  private ImportExportDialog importExportDialog;
  private final SqliteMigrator sqliteMigrator;
  private final CruiseDataController cruiseDataController;
  private final ExportController exportController;
  private final ImportController importController;
  private final PersonDatastore personDatastore;
  private final String processId;

  private final JButton manageRecordsButton = new JButton(MANAGE_RECORDS_LABEL);
  private final JButton importExportButton = new JButton(IMPORT_EXPORT_LABEL);
  private final JButton clearFormButton = new JButton(CLEAR_FORM_LABEL);
  private final JButton stopButton = new JButton(STOP_LABEL);
  private final JButton saveButton = new JButton(SAVE_LABEL);
  private final JButton packageButton = new JButton(PACKAGE_LABEL);
  private final JButton settingsButton = new JButton(SETTINGS_LABEL);
  private final JButton docsButton;
  private final JProgressBar progressBar = new JProgressBar();
  private final BoundedRangeModel progressBarModel = new DefaultBoundedRangeModel();
  private final JButton closeExitAppButton = new JButton("No");
  private final JButton confirmExitAppButton = new JButton("Yes");
  private final UiRefresher uiRefresher;
  private String packageIdCollisionDialogText;
  private String saveExitAppDialogText;
  private final ServiceProperties serviceProperties;

  @Autowired
  public FooterPanel(ReactiveViewRegistry reactiveViewRegistry, FooterControlController footerControlController,
      FooterControlModel footerControlModel,
      SqliteMigrator sqliteMigrator, CruiseDataController cruiseDataController, ExportController exportController, ImportController importController,
      PersonDatastore personDatastore, UiRefresher uiRefresher, ServiceProperties serviceProperties)
      throws IOException {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.footerControlController = footerControlController;
    this.footerControlModel = footerControlModel;
    this.sqliteMigrator = sqliteMigrator;
    this.cruiseDataController = cruiseDataController;
    this.exportController = exportController;
    this.importController = importController;
    this.personDatastore = personDatastore;
    this.uiRefresher = uiRefresher;
    this.serviceProperties = serviceProperties;
    this.docsButton = new JButton(new ImageIcon(ImageIO.read(
        Objects.requireNonNull(getClass().getResource(String.format(
            "/%s", HELP_ICON_NAME
        )))
    )));
    this.processId = UUID.randomUUID().toString();
  }

  @PostConstruct
  public void init() throws IOException {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void initializeFields() {
    stopButton.setEnabled(footerControlModel.isStopButtonEnabled());
    packageButton.setEnabled(footerControlModel.isPackageButtonEnabled());
    saveButton.setEnabled(footerControlModel.isSaveButtonEnabled());
    progressBar.setModel(progressBarModel);
  }

  private void setupLayout() {
    setLayout(new GridBagLayout());

    JPanel row1 = new JPanel();
    row1.setLayout(new GridBagLayout());
    row1.add(docsButton, configureLayout(0, 0, c -> c.weightx = 0));
    row1.add(manageRecordsButton, configureLayout(1, 0));
    row1.add(importExportButton, configureLayout(2, 0));
    row1.add(clearFormButton, configureLayout(3, 0));
    row1.add(stopButton, configureLayout(4, 0));
    row1.add(saveButton, configureLayout(5, 0));
    row1.add(packageButton, configureLayout(6, 0));
    add(row1, configureLayout(0, 0));


    JPanel row2 = new JPanel();
    row2.setLayout(new GridBagLayout());
    row2.add(progressBar, configureLayout(0, 0));
    row2.add(settingsButton, configureLayout(1, 0, c -> c.weightx = 0));
    add(row2, configureLayout(0, 1));
  }

  private void setupMvc() {
    reactiveViewRegistry.register(this);
    clearFormButton.addActionListener((evt) -> footerControlController.restoreDefaultsGlobal());
    saveButton.addActionListener((evt) -> {
      footerControlController.saveForms(false);
      uiRefresher.refresh();
    });
    packageButton.addActionListener((evt) -> footerControlController.addToQueue());

    closeExitAppButton.addActionListener((evt) -> footerControlController.setSaveExitAppDialogueVisible(false));
    confirmExitAppButton.addActionListener((evt) -> footerControlController.exitApplication());
    
    manageRecordsButton.addActionListener((evt) -> {
      if (manageRecordsDialog == null) {
        manageRecordsDialog = new ManageRecordsDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            cruiseDataController,
            reactiveViewRegistry
        );
        manageRecordsDialog.init();
        manageRecordsDialog.addCloseListener((dialog) -> {
          manageRecordsDialog.dispose();
          manageRecordsDialog = null;
        });
      }
      
      manageRecordsDialog.pack();
      manageRecordsDialog.setVisible(true);
    });
    
    Frame ancestor = (Frame) SwingUtilities.getWindowAncestor(this);
    
    importExportButton.addActionListener((evt) -> {
      if (importExportDialog == null) {
        importExportDialog = new ImportExportDialog(
            ancestor,
            exportController,
            importController,
            personDatastore,
            reactiveViewRegistry
        );
        importExportDialog.addCloseListener((dialog) -> {
          importExportDialog.dispose();
          importExportDialog = null;
        });
      }
      
      importExportDialog.pack();
      importExportDialog.setVisible(true);
    });
    
    stopButton.addActionListener((evt) -> footerControlController.stopPackaging());
    
    docsButton.addActionListener((evt) -> handleOpenFile());
    
    settingsButton.addActionListener((evt) -> {
      SettingsDialog settingsDialog = new SettingsDialog((Frame) SwingUtilities.getWindowAncestor(this), "Settings", true);
      settingsDialog.addMigrateListener((p) -> {
        new Thread(() -> sqliteMigrator.migrate(p, processId)).start();
        settingsDialog.dispose();
      });
      
      settingsDialog.pack();
      settingsDialog.setVisible(true);
    });
  }
  
  private void handleOpenFile() {
    String manualPath = Paths.get(serviceProperties.getWorkDir()).resolve("config")
        .resolve(USER_MANUAL_NAME).toAbsolutePath().toFile().toString();
    try {
      String os = System.getProperty("os.name");
      String command = null;
      if (os.contains("Mac")) {
        command = String.format(
            "open %s", manualPath
        );
      } else if (os.contains("Windows")) {
        command = String.format(
            "start %s", manualPath
        );
      } else if (os.contains("Linux")) {
        command = String.format(
            "xdg-open %s", manualPath
        );
      }
      if (command != null) {
        Runtime.getRuntime().exec(command);
      }
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          SwingUtilities.getWindowAncestor(this),
          String.format(
              "Failed to open user manual. User manual can be opened at %s", manualPath
          ),
          "Error",
          JOptionPane.ERROR_MESSAGE
      );
      throw new IllegalStateException(String.format(
          "Failed to open %s", USER_MANUAL_NAME
      ), e);
    }
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.UPDATE_SAVE_BUTTON_ENABLED: {
        boolean newValue = (boolean) evt.getNewValue();
        if (saveButton.isEnabled() != newValue) {
          saveButton.setEnabled(newValue);
        }
      }
      break;
      case Events.UPDATE_PACKAGE_BUTTON_ENABLED: {
        boolean newValue = (boolean) evt.getNewValue();
        if (packageButton.isEnabled() != newValue) {
          packageButton.setEnabled(newValue);
        }
      }
      break;
      case Events.UPDATE_STOP_BUTTON_ENABLED: {
        boolean newValue = (boolean) evt.getNewValue();
        if (stopButton.isEnabled() != newValue) {
          stopButton.setEnabled(newValue);
        }
      }
      break;
      case Events.UPDATE_SAVE_WARNING_DIALOGUE_VISIBLE: {
        boolean newValue = (boolean) evt.getNewValue();
        
        if (newValue) {
          JOptionPane.showMessageDialog(
              SwingUtilities.getWindowAncestor(this),
              "<html><B>The cruise ID field is empty. Please enter a cruise ID and other details before packaging.</B></html>",
              "Error",
              JOptionPane.ERROR_MESSAGE
          );
          footerControlController.setSaveWarningDialogueVisible(false);
        }
      }
      break;
      case Events.UPDATE_SAVE_EXIT_APP_DIALOGUE_VISIBLE: {
        boolean newValue = (boolean) evt.getNewValue();
        if (newValue) {
          int choice = JOptionPane.showConfirmDialog(
              SwingUtilities.getWindowAncestor(this),
              saveExitAppDialogText,
              null,
              JOptionPane.YES_NO_OPTION,
              JOptionPane.WARNING_MESSAGE
          );
          
          if (choice == JOptionPane.YES_OPTION) {
            footerControlController.exitApplication();
          }
          
          footerControlController.setSaveExitAppDialogueVisible(false);
        }
      }
      break;
      case Events.UPDATE_SAVE_OR_UPDATE_DIALOG_VISIBLE: {
        boolean newValue = (boolean) evt.getNewValue();
        if (newValue) {
          int choice = JOptionPane.showOptionDialog(
              SwingUtilities.getWindowAncestor(this),
              "<html><B>The current package ID is different than the saved value. Click \"Update\" to update current record. Click \"Create New\" to create a new record for this new package ID.</B></html>",
              null,
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.WARNING_MESSAGE,
              UIManager.getIcon("JOptionPane.warningIcon"),
              new Object[]{ "Cancel", "Update", "Create New" },
              "Create New"
          );
          
          if (choice == 1) {
            footerControlController.update(false);
            footerControlController.setSaveOrUpdateDialogVisible(false);
          } else if (choice == 2) {
            footerControlController.create(false);
          }
          
          footerControlController.setSaveOrUpdateDialogVisible(false);
        }
      }
      break;
      case Events.UPDATE_PACKAGE_ID_COLLISION_DIALOG_VISIBLE: {
        boolean newValue = (boolean) evt.getNewValue();
        if (newValue) {
          JOptionPane.showMessageDialog(
              SwingUtilities.getWindowAncestor(this),
              packageIdCollisionDialogText,
              "Error",
              JOptionPane.ERROR_MESSAGE
          );
          footerControlController.setPackageIdCollisionDialogVisible(false);
        }
      }
      break;
      case Events.UPDATE_JOB_ERRORS: {
        String newValue = (String) evt.getNewValue();
        if (newValue != null && !newValue.isBlank()) {
          JOptionPane.showMessageDialog(
              SwingUtilities.getWindowAncestor(this),
              String.format(
                  "<html><B>%s</B></html>", newValue
              ),
              "Error",
              JOptionPane.ERROR_MESSAGE
          );
        }
      }
      break;
      case Events.UPDATE_JOB_WARNINGS:
        List<?> newValue = (List<?>) evt.getNewValue();
        for (Object obj : newValue) {
          String message = (String) obj;
          
          int choice = JOptionPane.showOptionDialog(
              SwingUtilities.getWindowAncestor(this),
              message,
              null,
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.WARNING_MESSAGE,
              UIManager.getIcon("JOptionPane.warningIcon"),
              new Object[] { "OK", "Ignore" },
              "OK"
          );
          
          if (choice == 1) {
            footerControlModel.addIgnoreWarningMessage(message);
          }
        }
      case Events.EMIT_PACKAGE_ID:
        saveExitAppDialogText = String.format("<html><B>%s data has been updated. Do you want to exit editor?</B></html>", evt.getNewValue());
        packageIdCollisionDialogText = String.format("<html><B>The package name \"%s\" already exists. Please modify the Cruise ID or Segment or Leg value to create a unique package ID.</B></html>", evt.getNewValue());
        break;
      default:
        if (evt.getPropertyName().startsWith("UPDATE_PROGRESS")) {
          if (evt.getPropertyName().endsWith(processId)) {
            updateProgressBarModel(progressBarModel, evt);
          }
        }
        break;
    }
  }
}
