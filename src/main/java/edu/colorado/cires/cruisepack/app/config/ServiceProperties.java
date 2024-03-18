package edu.colorado.cires.cruisepack.app.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

  private String font;
  
  @NotNull
  private Integer fontSize;
  
  @NotNull
  private Integer windowPadding;

  private boolean ui;

  public boolean isUi() {
    return ui;
  }

  public void setUi(boolean ui) {
    this.ui = ui;
  }

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

  public String getFont() {
    return font;
  }

  public void setFont(String font) {
    this.font = font;
  }

  public Integer getFontSize() {
    return fontSize;
  }

  public void setFontSize(Integer fontSize) {
    this.fontSize = fontSize;
  }

  public Integer getWindowPadding() {
    return windowPadding;
  }

  public void setWindowPadding(Integer windowPadding) {
    this.windowPadding = windowPadding;
  }
}
