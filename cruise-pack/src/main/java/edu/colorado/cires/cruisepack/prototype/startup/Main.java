package edu.colorado.cires.cruisepack.prototype.startup;

import javax.swing.SwingUtilities;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

//@SpringBootApplication
public class Main implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplicationBuilder(Main.class)
        .web(WebApplicationType.NONE)
        .headless(false)
        .build();
    app.setWebApplicationType(WebApplicationType.NONE);
    app.setHeadless(false);
    app.run(args);
//    SpringApplication.run(Main.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
//    CruisePackInitializer.initialize();
    SwingUtilities.invokeLater(ApplicationContext::initialize);
  }
}
