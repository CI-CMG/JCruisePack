package edu.colorado.cires.cruisepack.app.ui.view.tab.cruisetab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import jakarta.annotation.PostConstruct;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.springframework.stereotype.Component;

@Component
public class CruiseDocumentsPanel extends JPanel {

  private static final String DOCUMENTS_PATH_LABEL = "Documents Path";
  private static final String DIRECTORY_LABEL = "Select Directory";

  private final JTextField pathTextField = new JTextField();
  private final JButton selectDirectoryButton = new JButton(DIRECTORY_LABEL);

  @PostConstruct
  public void init() {
    setLayout(new GridBagLayout());
    add(new JLabel(DOCUMENTS_PATH_LABEL), configureLayout(0, 0));
    add(pathTextField, configureLayout(1, 0));
    add(selectDirectoryButton, configureLayout(2, 0));
  }
}
