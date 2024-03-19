package edu.colorado.cires.cruisepack.app.ui.view.footer;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateProgressBarModel;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.migration.SqliteMigrator;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.FooterControlModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.UiRefresher;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.OptionDialog;
import jakarta.annotation.PostConstruct;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
  private final ManageRecordsDialog manageRecordsDialog;
  private final ImportExportDialog importExportDialog;
  private final SqliteMigrator sqliteMigrator;

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
  private final OptionDialog saveWarningDialog = new OptionDialog(
      "<html><B>The cruise ID field is empty. Please enter a cruise ID and other details before packaging.</B></html>",
      Collections.singletonList("OK")
  );
  private final OptionDialog saveExitAppDialog = new OptionDialog(
      "<html><B>Record data has been updated. Do you want to exit editor?</B></html>",
      List.of("No", "Yes")
  );
  private final OptionDialog saveOrUpdateDialog = new OptionDialog(
      "<html><B>The current package ID is different than the saved value. Click \"Update\" to update current record. Click \"Create New\" to create a new record for this new package ID.</B></html>",
      List.of("Cancel", "Update", "Create New")
  );
  private final OptionDialog packageIdCollisionDialog = new OptionDialog(
      "<html><B>This package name already exists. Please modify the Cruise ID or Segment or Leg value to create a unique package ID.</B></html>",
      List.of("OK")
  );
  
  private final OptionDialog jobErrorsDialog = new OptionDialog(
      "<html><B>Packaging failed</B></html>",
      List.of("OK")
  );
  private final JButton closeExitAppButton = new JButton("No");
  private final JButton confirmExitAppButton = new JButton("Yes");
  private final UiRefresher uiRefresher;

  @Autowired
  public FooterPanel(ReactiveViewRegistry reactiveViewRegistry, FooterControlController footerControlController,
      FooterControlModel footerControlModel, ManageRecordsDialog manageRecordsDialog, ImportExportDialog importExportDialog,
      SqliteMigrator sqliteMigrator, UiRefresher uiRefresher)
      throws IOException {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.footerControlController = footerControlController;
    this.footerControlModel = footerControlModel;
    this.manageRecordsDialog = manageRecordsDialog;
    this.importExportDialog = importExportDialog;
    this.sqliteMigrator = sqliteMigrator;
    this.uiRefresher = uiRefresher;
    this.docsButton = new JButton(new ImageIcon(ImageIO.read(
        Objects.requireNonNull(getClass().getResource(String.format(
            "/%s", HELP_ICON_NAME
        )))
    )));
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
    packageButton.addActionListener((evt) -> handlePackage());
    clearFormButton.addActionListener((evt) -> footerControlController.restoreDefaultsGlobal());
    saveButton.addActionListener((evt) -> {
      footerControlController.saveForms(false);
      uiRefresher.refresh();
    });
    saveWarningDialog.addListener("OK", (evt) -> footerControlController.setSaveWarningDialogueVisible(false));
    saveWarningDialog.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        footerControlController.setSaveWarningDialogueVisible(false);
      }
    });

    closeExitAppButton.addActionListener((evt) -> footerControlController.setSaveExitAppDialogueVisible(false));
    confirmExitAppButton.addActionListener((evt) -> footerControlController.exitApplication());
    
    saveExitAppDialog.addListener("Yes", (evt) -> footerControlController.exitApplication());
    saveExitAppDialog.addListener("No", (evt) -> footerControlController.setSaveExitAppDialogueVisible(false));
    saveExitAppDialog.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent event) {
        footerControlController.setSaveExitAppDialogueVisible(false);
      }
    });
    
    packageIdCollisionDialog.addListener("OK", (evt) -> footerControlController.setPackageIdCollisionDialogVisible(false));
    packageIdCollisionDialog.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        footerControlController.setPackageIdCollisionDialogVisible(false);
      }
    });
    
    saveOrUpdateDialog.addListener("Cancel", (evt) -> footerControlController.setSaveOrUpdateDialogVisible(false));
    saveOrUpdateDialog.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        footerControlController.setSaveOrUpdateDialogVisible(false);
      }
    });
    saveOrUpdateDialog.addListener("Update", (evt) -> {
      footerControlController.setSaveOrUpdateDialogVisible(false);
      footerControlController.update(false);
    });
    saveOrUpdateDialog.addListener("Create New", (evt) -> {
      footerControlController.setSaveOrUpdateDialogVisible(false);
      footerControlController.create(false);
    });
    
    manageRecordsButton.addActionListener((evt) -> {
      manageRecordsDialog.pack();
      manageRecordsDialog.setVisible(true);
    });
    
    importExportButton.addActionListener((evt) -> {
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
    if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop().open(
            new File(
                Objects.requireNonNull(getClass().getResource(String.format(
                    "/%s", USER_MANUAL_NAME
                ))).toURI()
            )
        );
      } catch (IOException | URISyntaxException e) {
        throw new IllegalStateException(String.format(
            "Failed to open %s", USER_MANUAL_NAME
        ), e);
      }
    }
  }

  private void handlePackage() {
    footerControlController.startPackaging();
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
        if (saveWarningDialog.isVisible() != newValue) {
          saveWarningDialog.pack();
          saveWarningDialog.setVisible(newValue);
        }
      }
      break;
      case Events.UPDATE_SAVE_EXIT_APP_DIALOGUE_VISIBLE: {
        boolean newValue = (boolean) evt.getNewValue();
        if (saveExitAppDialog.isVisible() != newValue) {
          saveExitAppDialog.pack();
          saveExitAppDialog.setVisible(newValue);
        }
      }
      break;
      case Events.EMIT_PACKAGE_ID:
        updateLabelText(saveExitAppDialog.getLabel(), new PropertyChangeEvent(
          evt,
          "UPDATE_APP_EXIT_LABEL",
          saveExitAppDialog.getLabel().getText(),
          String.format("<html><B>%s data has been updated. Do you want to exit editor?</B></html>", evt.getNewValue())
        ));
        updateLabelText(packageIdCollisionDialog.getLabel(), new PropertyChangeEvent(
            evt,
            "UPDATE_PACKAGE_ID_COLLISION_LABEL",
            packageIdCollisionDialog.getLabel().getText(),
            String.format("<html><B>The package name \"%s\" already exists. Please modify the Cruise ID or Segment or Leg value to create a unique package ID.</B></html>", evt.getNewValue())
        ));
        break;
      case Events.UPDATE_SAVE_OR_UPDATE_DIALOG_VISIBLE: {
        boolean newValue = (boolean) evt.getNewValue();
        if (saveOrUpdateDialog.isVisible() != newValue) {
          saveOrUpdateDialog.pack();
          saveOrUpdateDialog.setVisible(newValue);
        }
      }
      break;
      case Events.UPDATE_PACKAGE_ID_COLLISION_DIALOG_VISIBLE: {
        boolean newValue = (boolean) evt.getNewValue();
        if (packageIdCollisionDialog.isVisible() != newValue) {
          packageIdCollisionDialog.pack();
          packageIdCollisionDialog.setVisible(newValue);
        }
      }
      break;
      case Events.UPDATE_JOB_ERRORS: {
        String newValue = (String) evt.getNewValue();
        if (newValue != null && !newValue.isBlank()) {
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
      case Events.UPDATE_PROGRESS:
        updateProgressBarModel(progressBarModel, evt);
        break;
      case Events.UPDATE_JOB_WARNINGS:
        List<?> newValue = (List<?>) evt.getNewValue();
        for (Object obj : newValue) {
          String message = (String) obj;
          OptionDialog optionDialog = new OptionDialog(message, List.of(
              "OK", "Ignore"
          ));
          optionDialog.addListener("Ignore", (event) ->
              footerControlModel.addIgnoreWarningMessage(message)
          );
          
          optionDialog.pack();
          optionDialog.setVisible(true);
        }
      default:
        break;
    }
  }
}
