package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import jakarta.annotation.PostConstruct;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.springframework.stereotype.Component;

@Component
public class DatasetConfigurationPanel extends JPanel {

  @PostConstruct
  public void init() {
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    setBackground(Color.WHITE);
  }
}
