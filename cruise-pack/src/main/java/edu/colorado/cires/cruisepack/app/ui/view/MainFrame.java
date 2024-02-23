package edu.colorado.cires.cruisepack.app.ui.view;

import jakarta.annotation.PostConstruct;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;

@Component
public class MainFrame extends JFrame implements ReactiveView {

  private static final String TITLE = "CruisePack";
  private static final int MIN_WIDTH = 800;
  private static final int MIN_HEIGHT = 600;

  private final UiRefresher uiRefresher;
  private final RootPanel rootPanel;
  private final FooterControlController footerControlController;
  private final JDialog saveBeforeExitDialog = new JDialog((JFrame) null, null, true);
  private final JButton cancelButton = new JButton("Cancel");
  private final JButton noButton = new JButton("No");
  private final JButton yesButton = new JButton("Yes");
  private final ReactiveViewRegistry reactiveViewRegistry;

  @Autowired
  public MainFrame(UiRefresher uiRefresher, RootPanel rootPanel, FooterControlController footerControlController, ReactiveViewRegistry reactiveViewRegistry) {
    this.uiRefresher = uiRefresher;
    this.rootPanel = rootPanel;
    this.footerControlController = footerControlController;
    this.reactiveViewRegistry = reactiveViewRegistry;
  }

  @PostConstruct
  public void init() {
    uiRefresher.setMainFrame(this);
    setDefaultLookAndFeelDecorated(true);
    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
    
    saveBeforeExitDialog.setLayout(new GridBagLayout());
    JLabel saveBeforeExitLabel = new JLabel("<html><B>The current information might not be saved. Do you want to save before exiting CruisePack?</B></html>");
    saveBeforeExitLabel.setHorizontalAlignment(JLabel.CENTER);
    saveBeforeExitDialog.add(saveBeforeExitLabel, configureLayout(0, 0, c -> {
      c.weighty = 100;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.insets = new Insets(20, 20, 20, 20);
    }));
    JPanel saveBeforeExitPanel = new JPanel();
    saveBeforeExitPanel.setLayout(new BorderLayout());
    JPanel saveBeforeExitButtonPannel = new JPanel();
    saveBeforeExitButtonPannel.setLayout(new GridBagLayout());
    saveBeforeExitButtonPannel.add(cancelButton, configureLayout(0, 0));
    saveBeforeExitButtonPannel.add(noButton, configureLayout(1, 0));
    saveBeforeExitButtonPannel.add(yesButton, configureLayout(2, 0));
    saveBeforeExitPanel.add(saveBeforeExitButtonPannel, BorderLayout.EAST);
    saveBeforeExitDialog.add(saveBeforeExitPanel, configureLayout(0, 1, c -> c.weighty = 0));
    saveBeforeExitDialog.setVisible(false);

    setTitle(TITLE);
    add(rootPanel);
    pack();

    setupMvc();
  }

  private void setupMvc() {
    reactiveViewRegistry.register(this);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent event) {
        footerControlController.setSaveBeforeExitDialogVisible(true);
      }
    });

    saveBeforeExitDialog.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent event) {
        footerControlController.setSaveBeforeExitDialogVisible(false);
      }
    });

    cancelButton.addActionListener((evt) -> footerControlController.setSaveBeforeExitDialogVisible(false));
    noButton.addActionListener((evt) -> footerControlController.exitApplication());
    yesButton.addActionListener((evt) -> {
      footerControlController.saveForms(true);
      footerControlController.exitApplication();
    });
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.UPDATE_SAVE_BEFORE_EXIT_DIALOG_VISIBLE:{
        boolean newValue = (boolean) evt.getNewValue();
        if (saveBeforeExitDialog.isVisible() != newValue) {
          saveBeforeExitDialog.pack();
          saveBeforeExitDialog.setVisible(newValue);
        }
      }  
      break;
      default:
        break;
    }
  }


}
