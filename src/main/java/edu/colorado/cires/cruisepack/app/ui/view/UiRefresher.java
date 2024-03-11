package edu.colorado.cires.cruisepack.app.ui.view;

import org.springframework.stereotype.Component;

@Component
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
