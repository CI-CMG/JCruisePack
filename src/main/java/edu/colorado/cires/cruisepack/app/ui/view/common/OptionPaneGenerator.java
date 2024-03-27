package edu.colorado.cires.cruisepack.app.ui.view.common;

import edu.colorado.cires.cruisepack.app.ui.view.MainFrame;
import jakarta.annotation.Nullable;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.springframework.stereotype.Component;

@Component
public class OptionPaneGenerator {
  
  private MainFrame mainFrame;

  public void setMainFrame(MainFrame mainFrame) {
    this.mainFrame = mainFrame;
  }

  public void createMessagePane(String message, @Nullable String title, int messageType) {
    if (mainFrame != null) {
      JOptionPane.showMessageDialog(
          mainFrame,
          message,
          title,
          messageType
      );
    }
  }
  
  public int createOptionPane(String message, String title, int messageType, String icon, String[] options, String highlightedOption) {
    if (mainFrame != null) {
      Icon iconComponent = UIManager.getIcon(icon); 
      return JOptionPane.showOptionDialog(
          mainFrame,
          message,
          title,
          JOptionPane.DEFAULT_OPTION,
          messageType,
          iconComponent,
          options,
          highlightedOption
      );
    }
    
    return -1;
  }
}
