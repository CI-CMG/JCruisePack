package edu.colorado.cires.cruisepack.app.ui.view.footer;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.FooterControlModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import jakarta.annotation.PostConstruct;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FooterPanel extends JPanel implements ReactiveView {

  private static final String HIDE_RECORDS_LABEL = "Hide Records";
  private static final String IMPORT_EXPORT_LABEL = "Import / Export";
  private static final String CLEAR_FORM_LABEL = "Clear Form";
  private static final String STOP_LABEL = "Stop Packaging";
  private static final String SAVE_LABEL = "Save For Later";
  private static final String PACKAGE_LABEL = "Package Data";
  private static final String SETTINGS_LABEL = "Settings";

  private final ReactiveViewRegistry reactiveViewRegistry;
  private final FooterControlController footerControlController;
  private final FooterControlModel footerControlModel;

  private final JButton hideRecordsButton = new JButton(HIDE_RECORDS_LABEL);
  private final JButton importExportButton = new JButton(IMPORT_EXPORT_LABEL);
  private final JButton clearFormButton = new JButton(CLEAR_FORM_LABEL);
  private final JButton stopButton = new JButton(STOP_LABEL);
  private final JButton saveButton = new JButton(SAVE_LABEL);
  private final JButton packageButton = new JButton(PACKAGE_LABEL);
  private final JButton settingsButton = new JButton(SETTINGS_LABEL);
  private final JProgressBar progressBar = new JProgressBar();

  @Autowired
  public FooterPanel(ReactiveViewRegistry reactiveViewRegistry, FooterControlController footerControlController,
      FooterControlModel footerControlModel) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.footerControlController = footerControlController;
    this.footerControlModel = footerControlModel;
  }

  @PostConstruct
  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void initializeFields() {
    stopButton.setEnabled(footerControlModel.isStopButtonEnabled());
    packageButton.setEnabled(footerControlModel.isPackageButtonEnabled());
    saveButton.setEnabled(footerControlModel.isSaveButtonEnabled());
  }

  private void setupLayout() {
    setLayout(new GridBagLayout());

    JPanel row1 = new JPanel();
    row1.setLayout(new GridBagLayout());
    row1.add(hideRecordsButton, configureLayout(0, 0));
    row1.add(importExportButton, configureLayout(1, 0));
    row1.add(clearFormButton, configureLayout(2, 0));
    row1.add(stopButton, configureLayout(3, 0));
    row1.add(saveButton, configureLayout(4, 0));
    row1.add(packageButton, configureLayout(5, 0));
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
      default:
        break;
    }
  }
}
