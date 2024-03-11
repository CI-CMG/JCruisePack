package edu.colorado.cires.cruisepack.app.ui.view;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class UiRefresher {

  private MainFrame mainFrame;

  public void setMainFrame(MainFrame mainFrame) {
    this.mainFrame = mainFrame;
  }

  public void refresh() {
    mainFrame.validate();
    mainFrame.repaint();
  }
}
