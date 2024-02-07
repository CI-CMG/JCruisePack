package edu.colorado.cires.cruisepack.app;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.view.MainFrame;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CruisePack {

  private static final Logger LOGGER = LoggerFactory.getLogger(CruisePack.class);
  private static final String SYSTEM_LNF = "system";
  private static final String CROSS_PLATFORM_LNF = "cross-platform";

  public static void main(String[] args) {

    ConfigurableApplicationContext ctx = new SpringApplicationBuilder(CruisePack.class)
        .headless(false)
        .web(WebApplicationType.NONE)
        .run(args);



    SwingUtilities.invokeLater(() -> {
      MainFrame mainFrame = ctx.getBean(MainFrame.class);
      setLookAndFeel(ctx.getBean(ServiceProperties.class));
      SwingUtilities.updateComponentTreeUI(mainFrame);
      mainFrame.pack();
      mainFrame.setVisible(true);
    });
  }

  private static void setLookAndFeel(ServiceProperties serviceProperties) {
    LOGGER.info(
        "Available LNF: {}",
        Arrays.asList(UIManager.getInstalledLookAndFeels()).stream()
            .map(LookAndFeelInfo::getClassName)
            .collect(Collectors.toList()));
    String className;
    switch (serviceProperties.getLookAndFeel()) {
      case SYSTEM_LNF:
        LOGGER.info("Using system LNF");
        className = UIManager.getSystemLookAndFeelClassName();
        break;
      case CROSS_PLATFORM_LNF:
        LOGGER.info("Using cross-platform LNF");
        className = UIManager.getCrossPlatformLookAndFeelClassName();
        break;
      default:
        className = serviceProperties.getLookAndFeel();
        break;
    }
    try {
      UIManager.setLookAndFeel(className);
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
      throw new IllegalStateException("Unable to set look and feel for '" + serviceProperties.getLookAndFeel() + "'", e);
    }
    LOGGER.info("Using LNF: {}", className);
  }
}
