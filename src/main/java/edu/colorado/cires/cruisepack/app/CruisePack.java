package edu.colorado.cires.cruisepack.app;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import com.github.weisj.darklaf.theme.IntelliJTheme;
import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.init.CruisePackPreSpringStarter;
import edu.colorado.cires.cruisepack.app.ui.view.MainFrame;
import java.awt.Font;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class CruisePack implements ApplicationListener<ApplicationStartingEvent> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CruisePack.class);
  private static final String SYSTEM_LAF = "system";
  private static final String CROSS_PLATFORM_LAF = "cross-platform";
  private static final String INTELLIJ = "intellij";
  private static final String DARCULA = "darcula";


  private static ConfigurableApplicationContext createCtx(String[] args) {
    ConfigurableApplicationContext ctx;
    try {
      ctx = new SpringApplicationBuilder(CruisePack.class)
          .listeners(new CruisePack())
          .headless(false)
          .web(WebApplicationType.NONE)
          .run(args);
    } catch (Exception e) {
      LOGGER.warn("Failed first attempt to start context. Trying in headless mode.", e);
      ctx = new SpringApplicationBuilder(CruisePack.class)
          .listeners(new CruisePack())
          .headless(true)
          .web(WebApplicationType.NONE)
          .run(args);
    }
    return ctx;
  }

  public static void main(String[] args) {

    CruisePackPreSpringStarter.start();

    ConfigurableApplicationContext ctx = createCtx(args);

    SwingUtilities.invokeLater(() -> {
      if (ctx.getBeanNamesForType(MainFrame.class).length > 0 ) {
        MainFrame mainFrame = ctx.getBean(MainFrame.class);
        setLookAndFeel(ctx.getBean(ServiceProperties.class));
        SwingUtilities.updateComponentTreeUI(mainFrame);
        mainFrame.pack();
        mainFrame.setVisible(true);
      }
    });
  }

//  @EventListener
//  public void applicationStarting(ApplicationStartingEvent event) {
//    System.out.println("ApplicationStartingEvent");
//  }
//
//  @EventListener
//  public void applicationEnvironmentPreparedEvent(ApplicationEnvironmentPreparedEvent event) {
//    System.out.println("ApplicationEnvironmentPreparedEvent");
//  }
//
//  @EventListener
//  public void applicationContextInitializedEvent(ApplicationContextInitializedEvent event) {
//    System.out.println("ApplicationContextInitializedEvent");
//  }
//
//  @EventListener
//  public void ApplicationPreparedEvent(ApplicationPreparedEvent event) {
//    System.out.println("ApplicationPreparedEvent");
//  }

  private static void setLookAndFeel(ServiceProperties serviceProperties) {
    LOGGER.info(
        "Available LAF: {}",
        Arrays.asList(UIManager.getInstalledLookAndFeels()).stream()
            .map(LookAndFeelInfo::getClassName)
            .collect(Collectors.toList()));
    try {
      java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
      while (keys.hasMoreElements()) {
        Object key = keys.nextElement();
        Object value = UIManager.get(key);
        if (value instanceof javax.swing.plaf.FontUIResource) {
          UIManager.put(key, new FontUIResource(serviceProperties.getFont(), Font.PLAIN, serviceProperties.getFontSize()));
        }
      }

      switch (serviceProperties.getLookAndFeel()) {
        case SYSTEM_LAF:
          LOGGER.info("Using system LAF");
          UIManager.setLookAndFeel(
              UIManager.getSystemLookAndFeelClassName()
          );
          break;
        case CROSS_PLATFORM_LAF:
          LOGGER.info("Using cross-platform LAF");
          UIManager.setLookAndFeel(
              UIManager.getCrossPlatformLookAndFeelClassName()
          );
          break;
        case INTELLIJ:
          LOGGER.info("Using intellij theme");
          LafManager.install(new IntelliJTheme());
          break;
        case DARCULA:
          LOGGER.info("Using darcula theme");
          LafManager.install(new DarculaTheme());
          break;
        default:
          UIManager.setLookAndFeel(
              serviceProperties.getLookAndFeel()
          );
          break;
      }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
      throw new IllegalStateException("Unable to set look and feel for '" + serviceProperties.getLookAndFeel() + "'", e);
    }
  }

  @Override
  public void onApplicationEvent(ApplicationStartingEvent event) {
    System.out.println("ApplicationStartingEvent");
  }
}
