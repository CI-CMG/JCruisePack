package edu.colorado.cires.cruisepack.app.ui.view.footer;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
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
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.OptionDialog;
import jakarta.annotation.PostConstruct;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
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
      }
      
      importExportDialog.pack();
      importExportDialog.setVisible(true);
    });
    
    stopButton.addActionListener((evt) -> footerControlController.stopPackaging());
    
    docsButton.addActionListener((evt) -> handleOpenFile());
    
    settingsButton.addActionListener((evt) -> {
      SettingsDialog settingsDialog = new SettingsDialog((Frame) SwingUtilities.getWindowAncestor(this), "Settings", true);
      settingsDialog.addMigrateListener((p) -> {
        try {
          sqliteMigrator.migrate(p);
          settingsDialog.dispose();
        } catch (Exception ignored) {}
      });
      
      settingsDialog.pack();
      settingsDialog.setVisible(true);
    });
  }
  
  private void handleOpenFile() {
    try {
      String os = System.getProperty("os.name");
      String manualPath = Paths.get(serviceProperties.getWorkDir()).resolve("config")
          .resolve(USER_MANUAL_NAME).toAbsolutePath().toFile().toString();
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
          OptionDialog saveWarningDialog =  new OptionDialog(
              (Frame) SwingUtilities.getWindowAncestor(this),
              "<html><B>The cruise ID field is empty. Please enter a cruise ID and other details before packaging.</B></html>",
              Collections.singletonList("OK")
          );

          saveWarningDialog.addListener("OK", (event) -> footerControlController.setSaveWarningDialogueVisible(false));
          saveWarningDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
              footerControlController.setSaveWarningDialogueVisible(false);
            }
          });
          
          saveWarningDialog.pack();
          saveWarningDialog.setVisible(true);
        }
      }
      break;
      case Events.UPDATE_SAVE_EXIT_APP_DIALOGUE_VISIBLE: {
        boolean newValue = (boolean) evt.getNewValue();
        if (newValue) {
          OptionDialog saveExitAppDialog = new OptionDialog(
              (Frame) SwingUtilities.getWindowAncestor(this),
              saveExitAppDialogText,
              List.of("No", "Yes")
          );

          saveExitAppDialog.addListener("Yes", (event) -> footerControlController.exitApplication());
          saveExitAppDialog.addListener("No", (event) -> footerControlController.setSaveExitAppDialogueVisible(false));
          saveExitAppDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
              footerControlController.setSaveExitAppDialogueVisible(false);
            }
          });
          saveExitAppDialog.pack();
          saveExitAppDialog.setVisible(true);
        }
      }
      break;
      case Events.UPDATE_SAVE_OR_UPDATE_DIALOG_VISIBLE: {
        boolean newValue = (boolean) evt.getNewValue();
        if (newValue) {
          OptionDialog saveOrUpdateDialog = new OptionDialog(
              (Frame) SwingUtilities.getWindowAncestor(this),
              "<html><B>The current package ID is different than the saved value. Click \"Update\" to update current record. Click \"Create New\" to create a new record for this new package ID.</B></html>",
              List.of("Cancel", "Update", "Create New")
          );

          saveOrUpdateDialog.addListener("Cancel", (event) -> footerControlController.setSaveOrUpdateDialogVisible(false));
          saveOrUpdateDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
              footerControlController.setSaveOrUpdateDialogVisible(false);
            }
          });
          saveOrUpdateDialog.addListener("Update", (event) -> {
            footerControlController.setSaveOrUpdateDialogVisible(false);
            footerControlController.update(false);
          });
          saveOrUpdateDialog.addListener("Create New", (event) -> {
            footerControlController.setSaveOrUpdateDialogVisible(false);
            footerControlController.create(false);
          });
          saveOrUpdateDialog.pack();
          saveOrUpdateDialog.setVisible(true);
        }
      }
      break;
      case Events.UPDATE_PACKAGE_ID_COLLISION_DIALOG_VISIBLE: {
        boolean newValue = (boolean) evt.getNewValue();
        if (newValue) {
          OptionDialog packageIdCollisionDialog = new OptionDialog(
              (Frame) SwingUtilities.getWindowAncestor(this),
              packageIdCollisionDialogText,
              List.of("OK")
          );

          packageIdCollisionDialog.addListener("OK", (event) -> footerControlController.setPackageIdCollisionDialogVisible(false));
          packageIdCollisionDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
              footerControlController.setPackageIdCollisionDialogVisible(false);
            }
          });
          
          packageIdCollisionDialog.pack();
          packageIdCollisionDialog.setVisible(true);
        }
      }
      break;
      case Events.UPDATE_JOB_ERRORS: {
        String newValue = (String) evt.getNewValue();
        if (newValue != null && !newValue.isBlank()) {
          OptionDialog jobErrorsDialog = new OptionDialog(
              (Frame) SwingUtilities.getWindowAncestor(this),
              "<html><B>Packaging failed</B></html>",
              List.of("OK")
          );
          updateLabelText(jobErrorsDialog.getLabel(), new PropertyChangeEvent(
              evt,
              "UPDATE_JOB_ERRORS",
              jobErrorsDialog.getLabel().getText(),
              String.format("<html><B>%s</B></html>", newValue)
          ));
          jobErrorsDialog.pack();
          jobErrorsDialog.setVisible(true);
        }
      }
      break;
      case Events.UPDATE_JOB_WARNINGS:
        List<?> newValue = (List<?>) evt.getNewValue();
        for (Object obj : newValue) {
          String message = (String) obj;
          OptionDialog optionDialog = new OptionDialog(
              (Frame) SwingUtilities.getWindowAncestor(this), 
              message, 
              List.of("OK", "Ignore")
          );
          optionDialog.addListener("Ignore", (event) ->
              footerControlModel.addIgnoreWarningMessage(message)
          );
          
          optionDialog.pack();
          optionDialog.setVisible(true);
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
