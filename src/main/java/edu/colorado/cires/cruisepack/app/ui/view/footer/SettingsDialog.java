package edu.colorado.cires.cruisepack.app.ui.view.footer;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.view.common.ValueChangeListener;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

public class SettingsDialog extends JDialog {
  
  private final JButton migrateButton = new JButton("Migrate Sqlite Database");
  
  private final List<ValueChangeListener<Path>> migrateListeners = new ArrayList<>(0);

  public SettingsDialog(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
    init();
  }
  
  public void addMigrateListener(ValueChangeListener<Path> listener) {
    migrateListeners.add(listener);
  }

  private void init() {
    setupLayout();
    setupMvc();
  }
  
  private void setupLayout() {
    setLayout(new GridBagLayout());
    add(migrateButton, configureLayout(0, 0, c -> c.weighty = 0));
  }
  
  private void setupMvc() {
    migrateButton.addActionListener((evt) -> handleFileSelect());
  }
  
  private void handleFileSelect() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setAcceptAllFileFilterUsed(false);
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      migrateListeners.forEach(listener -> listener.handleChange(
          fileChooser.getSelectedFile().toPath().toAbsolutePath().normalize()
      ));
    }
  }
}
