package edu.colorado.cires.cruisepack.app.ui.view;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.view.footer.FooterPanel;
import edu.colorado.cires.cruisepack.app.ui.view.tab.ApplicationTabs;
import jakarta.annotation.PostConstruct;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class RootPanel extends JPanel {

  private final ApplicationTabs applicationTabs;
  private final FooterPanel footerPanel;
  private final ServiceProperties serviceProperties;

  @Autowired
  public RootPanel(ApplicationTabs applicationTabs, FooterPanel footerPanel, ServiceProperties serviceProperties) {
    this.applicationTabs = applicationTabs;
    this.footerPanel = footerPanel;
    this.serviceProperties = serviceProperties;
  }

  @PostConstruct
  public void init() {
    setLayout(new BorderLayout());
    int padding = serviceProperties.getWindowPadding();
    setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    add(applicationTabs, BorderLayout.CENTER);
    add(footerPanel, BorderLayout.SOUTH);
  }
}
