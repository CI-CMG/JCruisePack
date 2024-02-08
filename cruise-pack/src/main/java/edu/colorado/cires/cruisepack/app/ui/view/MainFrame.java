package edu.colorado.cires.cruisepack.app.ui.view;

import jakarta.annotation.PostConstruct;
import java.awt.Dimension;
import javax.swing.JFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainFrame extends JFrame {

  private static final String TITLE = "CruisePack";
  private static final int MIN_WIDTH = 800;
  private static final int MIN_HEIGHT = 600;

  private final UiRefresher uiRefresher;
  private final RootPanel rootPanel;

  @Autowired
  public MainFrame(UiRefresher uiRefresher, RootPanel rootPanel) {
    this.uiRefresher = uiRefresher;
    this.rootPanel = rootPanel;
  }

  @PostConstruct
  public void init() {
    uiRefresher.setMainFrame(this);
    setDefaultLookAndFeelDecorated(true);
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
    setTitle(TITLE);
    add(rootPanel);
    pack();
  }


}
