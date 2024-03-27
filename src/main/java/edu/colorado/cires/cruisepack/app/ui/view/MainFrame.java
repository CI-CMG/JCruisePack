package edu.colorado.cires.cruisepack.app.ui.view;

import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
import edu.colorado.cires.cruisepack.app.ui.view.common.OptionPaneGenerator;
import jakarta.annotation.PostConstruct;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class MainFrame extends JFrame {

  private static final String TITLE = "CruisePack";
  private static final int MIN_WIDTH = 800;
  private static final int MIN_HEIGHT = 600;

  private final UiRefresher uiRefresher;
  private final OptionPaneGenerator optionPaneGenerator;
  private final RootPanel rootPanel;
  private final FooterControlController footerControlController;

  @Autowired
  public MainFrame(UiRefresher uiRefresher, OptionPaneGenerator optionPaneGenerator, RootPanel rootPanel, FooterControlController footerControlController) {
    this.uiRefresher = uiRefresher;
    this.optionPaneGenerator = optionPaneGenerator;
    this.rootPanel = rootPanel;
    this.footerControlController = footerControlController;
  }

  @PostConstruct
  public void init() {
    uiRefresher.setMainFrame(this);
    optionPaneGenerator.setMainFrame(this);
    setDefaultLookAndFeelDecorated(true);
    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
    setLocationRelativeTo(null);

    setTitle(TITLE);
    add(rootPanel);
    pack();

    setupMvc();
  }

  private void setupMvc() {
    Frame ancestor = (Frame) SwingUtilities.getWindowAncestor(this);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent event) {
        int choice = JOptionPane.showConfirmDialog(
            ancestor,
            "<html><B>The current information might not be saved. Do you want to save before exiting CruisePack?</B></html>",
            null,
            JOptionPane.YES_NO_CANCEL_OPTION
        );
        
        if (choice == JOptionPane.YES_OPTION) {
          footerControlController.saveForms(true);
          footerControlController.exitApplication();
        } else if (choice == JOptionPane.NO_OPTION) {
          footerControlController.exitApplication();
        }
      }
    });


  }


}
