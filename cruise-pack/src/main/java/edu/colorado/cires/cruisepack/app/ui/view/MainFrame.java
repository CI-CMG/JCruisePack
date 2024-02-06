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
  private static final int MAX_WIDTH = 800;

  private final RootPanel rootPanel;

  @Autowired
  public MainFrame(RootPanel rootPanel) {
    this.rootPanel = rootPanel;
  }

  @PostConstruct
  public void init() {
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(MIN_WIDTH, MAX_WIDTH));
    setTitle(TITLE);
    add(rootPanel);
    pack();
  }


}
