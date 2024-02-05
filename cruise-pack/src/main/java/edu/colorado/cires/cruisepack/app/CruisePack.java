package edu.colorado.cires.cruisepack.app;

import edu.colorado.cires.cruisepack.app.ui.view.MainFrame;
import javax.swing.SwingUtilities;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CruisePack {

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx = new SpringApplicationBuilder(CruisePack.class)
        .headless(false)
        .web(WebApplicationType.NONE)
        .run(args);

    SwingUtilities.invokeLater(() -> {
      MainFrame mainFrame = ctx.getBean(MainFrame.class);
      mainFrame.setVisible(true);
    });
  }
}
