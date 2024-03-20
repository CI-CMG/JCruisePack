package edu.colorado.cires.cruisepack.app.ui.view.tab.cruisetab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updatePathField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.DatasetsController;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.DatasetsModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import jakarta.annotation.PostConstruct;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class CruiseDocumentsPanel extends JPanel implements ReactiveView {

  private static final String DIRECTORY_LABEL = "...";

  private final JTextField pathTextField = new JTextField();
  private final JButton selectDirectoryButton = new JButton(DIRECTORY_LABEL);
  private final JLabel errorLabel = createErrorLabel();

  private final ReactiveViewRegistry reactiveViewRegistry;
  private final DatasetsController datasetsController;
  private final DatasetsModel datasetsModel;

  @Autowired
  public CruiseDocumentsPanel(ReactiveViewRegistry reactiveViewRegistry, DatasetsController datasetsController,
      DatasetsModel datasetsModel) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.datasetsController = datasetsController;
    this.datasetsModel = datasetsModel;
  }

  @PostConstruct
  public void init() {
    setLayout(new GridBagLayout());
    setBorder(BorderFactory.createTitledBorder("Path To Data Files"));
    setBackground(Color.WHITE);
    add(errorLabel, configureLayout(0, 0, c -> { c.weightx = 0; c.weighty = 0; }));
    add(pathTextField, configureLayout(0, 1, c -> { c.weightx = 100; c.weighty = 0; }));
    add(selectDirectoryButton, configureLayout(1, 1, c -> { c.weightx = 0; c.weighty = 0; }));

    pathTextField.setText(datasetsModel.getDocumentsPath() == null ? null : datasetsModel.getDocumentsPath().toString());

    reactiveViewRegistry.register(this);

    selectDirectoryButton.addActionListener((evt) -> handleDirSelect());
    
    pathTextField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        handleDirValue(pathTextField.getText());
      }
    });

  }

  private void handleDirSelect() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
      datasetsController.setDocumentsPath(fileChooser.getSelectedFile().toPath().toAbsolutePath().normalize());
    }
  }
  
  public void restoreDefaultState() {
    datasetsController.setDocumentsPath(null);
    datasetsController.setDocumentsPathError(null);
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.UPDATE_DOCS_DIRECTORY:
        updatePathField(pathTextField, evt);
        break;
      case Events.UPDATE_DOCS_DIRECTORY_ERROR:
        updateLabelText(errorLabel, evt);
      default:
        break;
    }
  }

  private void handleDirValue(String value) {
    Path path = Paths.get(value);
    datasetsController.setDocumentsPath(path.toAbsolutePath().normalize());
  }
}
