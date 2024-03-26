package edu.colorado.cires.cruisepack.app.ui.view;

import jakarta.annotation.PostConstruct;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.OptionDialog;

@Component
@ConditionalOnProperty(value="cruise-pack.ui", havingValue = "true")
public class MainFrame extends JFrame {

  private static final String TITLE = "CruisePack";
  private static final int MIN_WIDTH = 800;
  private static final int MIN_HEIGHT = 600;

  private final UiRefresher uiRefresher;
  private final RootPanel rootPanel;
  private final FooterControlController footerControlController;

  @Autowired
  public MainFrame(UiRefresher uiRefresher, RootPanel rootPanel, FooterControlController footerControlController) {
    this.uiRefresher = uiRefresher;
    this.rootPanel = rootPanel;
    this.footerControlController = footerControlController;
  }

  @PostConstruct
  public void init() {
    uiRefresher.setMainFrame(this);
    setDefaultLookAndFeelDecorated(true);
    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

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
        OptionDialog optionDialog = new OptionDialog(
            ancestor,
            "<html><B>The current information might not be saved. Do you want to save before exiting CruisePack?</B></html>",
            List.of("Cancel", "No", "Yes")
        );

        optionDialog.addListener("No", (evt) -> footerControlController.exitApplication());
        optionDialog.addListener("Yes", (evt) -> {
          footerControlController.saveForms(true);
          footerControlController.exitApplication();
        });
        
        optionDialog.pack();
        optionDialog.setVisible(true);
      }
    });


  }


}
