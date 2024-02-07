package edu.colorado.cires.cruisepack.app.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties("cruise-pack")
public class ServiceProperties {

  @NotBlank
  private String workDir;

  @NotBlank
  private String lookAndFeel;

  public String getWorkDir() {
    return workDir;
  }

  public void setWorkDir(String workDir) {
    this.workDir = workDir;
  }

  public String getLookAndFeel() {
    return lookAndFeel;
  }

  public void setLookAndFeel(String lookAndFeel) {
    this.lookAndFeel = lookAndFeel;
  }
}
