package edu.colorado.cires.cruisepack.app.ui.view.tab.cruisetab;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import jakarta.annotation.PostConstruct;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CruisePanel extends JPanel {

  private static final String CRUISE_TITLE_LABEL = "Cruise Title";
  private static final String CRUISE_PURPOSE_LABEL = "Cruise Purpose";
  private static final String CRUISE_DESCRIPTION_LABEL = "Cruise Description (overview of cruise-level metadata)";

  private final JTextField cruiseTitleField = new JTextField();
  private final JTextArea cruisePurposeField = new JTextArea();
  private final JTextArea cruiseDescriptionField = new JTextArea();

  private final CruiseDocumentsPanel cruiseDocumentsPanel;

  @Autowired
  public CruisePanel(CruiseDocumentsPanel cruiseDocumentsPanel) {
    this.cruiseDocumentsPanel = cruiseDocumentsPanel;
  }

  @PostConstruct
  public void init() {
    setLayout(new GridBagLayout());
    add(new JLabel(CRUISE_TITLE_LABEL), configureLayout(0, 0));
    add(cruiseTitleField, configureLayout(0, 1));
    add(new JLabel(CRUISE_PURPOSE_LABEL), configureLayout(0, 2));
    add(cruisePurposeField, configureLayout(0, 3, c -> c.ipady = 200));
    add(new JLabel(CRUISE_DESCRIPTION_LABEL), configureLayout(0, 4));
    add(cruiseDescriptionField, configureLayout(0, 5, c -> c.ipady = 200));
    add(cruiseDocumentsPanel, configureLayout(0, 6));
  }
}
