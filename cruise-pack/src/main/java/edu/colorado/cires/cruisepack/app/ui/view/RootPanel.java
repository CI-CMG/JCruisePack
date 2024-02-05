package edu.colorado.cires.cruisepack.app.ui.view;

import edu.colorado.cires.cruisepack.app.ui.view.footer.FooterPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.ApplicationTabs;
import jakarta.annotation.PostConstruct;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RootPanel extends JPanel {

  private final ApplicationTabs applicationTabs;
  private final FooterPanel footerPanel;

  @Autowired
  public RootPanel(ApplicationTabs applicationTabs, FooterPanel footerPanel) {
    this.applicationTabs = applicationTabs;
    this.footerPanel = footerPanel;
  }

  @PostConstruct
  public void init() {
    setLayout(new BorderLayout());
    add(applicationTabs, BorderLayout.CENTER);
    add(footerPanel, BorderLayout.SOUTH);
  }
}
