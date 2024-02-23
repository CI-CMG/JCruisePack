package edu.colorado.cires.cruisepack.app.ui.view.tab.cruisetab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createLabelWithErrorPanel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updatePathField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.CruiseInformationController;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.SimpleDocumentListener;
import jakarta.annotation.PostConstruct;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CruiseDocumentsPanel extends JPanel implements ReactiveView {

  private static final String DOCUMENTS_PATH_LABEL = "Documents Path";
  private static final String DIRECTORY_LABEL = "Select Directory";

  private final JTextField pathTextField = new JTextField();
  private final JLabel docsDirectoryLabel = createErrorLabel();
  private final JButton selectDirectoryButton = new JButton(DIRECTORY_LABEL);

  private final ReactiveViewRegistry reactiveViewRegistry;
  private final CruiseInformationController cruiseInformationController;
  private final CruiseInformationModel cruiseInformationModel;

  @Autowired
  public CruiseDocumentsPanel(ReactiveViewRegistry reactiveViewRegistry, CruiseInformationController cruiseInformationController,
      CruiseInformationModel cruiseInformationModel) {
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.cruiseInformationController = cruiseInformationController;
    this.cruiseInformationModel = cruiseInformationModel;
  }

  @PostConstruct
  public void init() {
    setLayout(new GridBagLayout());
    add(createLabelWithErrorPanel(DOCUMENTS_PATH_LABEL, docsDirectoryLabel), configureLayout(0, 0, c -> { c.weightx = 0; c.weighty = 0; }));
    add(pathTextField, configureLayout(1, 0, c -> { c.weightx = 100; c.weighty = 0; }));
    add(selectDirectoryButton, configureLayout(2, 0, c -> { c.weightx = 0; c.weighty = 0; }));

    pathTextField.setText(cruiseInformationModel.getDocumentsPath() == null ? null : cruiseInformationModel.getDocumentsPath().toString());

    reactiveViewRegistry.register(this);

    pathTextField.getDocument().addDocumentListener((SimpleDocumentListener)(evt) -> handleDirValue(pathTextField.getText()));

  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.UPDATE_DOCS_DIRECTORY:
        updatePathField(pathTextField, evt);
        break;
      case Events.UPDATE_DOCS_DIRECTORY_ERROR:
        updateLabelText(docsDirectoryLabel, evt);
      default:
        break;
    }
  }

  private void handleDirValue(String value) {
    Path path = Paths.get(value);
    cruiseInformationController.setDocumentsPath(path.toAbsolutePath().normalize());
  }
}
