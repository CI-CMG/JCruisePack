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
  private static final String SYSTEM_LAF = "system";
  private static final String CROSS_PLATFORM_LAF = "cross-platform";

  public static void main(String[] args) {

    ConfigurableApplicationContext ctx = new SpringApplicationBuilder(CruisePack.class)
        .headless(false)
        .web(WebApplicationType.NONE)
        .run(args);

    String[] beanNames = ctx.getBeanDefinitionNames(); // get beans
    for (String beanName : beanNames) {
      System.out.println("Bean --> "+ beanName); // print bean
    }


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
        "Available LAF: {}",
        Arrays.asList(UIManager.getInstalledLookAndFeels()).stream()
            .map(LookAndFeelInfo::getClassName)
            .collect(Collectors.toList()));
    String className;
    switch (serviceProperties.getLookAndFeel()) {
      case SYSTEM_LAF:
        LOGGER.info("Using system LAF");
        className = UIManager.getSystemLookAndFeelClassName();
        break;
      case CROSS_PLATFORM_LAF:
        LOGGER.info("Using cross-platform LAF");
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
    LOGGER.info("Using LAF: {}", className);
  }
}
