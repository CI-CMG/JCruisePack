package edu.colorado.cires.cruisepack.app.ui.view.tab.packagetab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createLabelWithErrorPanel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateStatefulRadioButton;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.ProjectController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.ProjectModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.StatefulRadioButton;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.OptionDialog;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class EditProjectDialog extends JDialog implements ReactiveView {
  
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final ProjectController projectController;
  
  private final JTextField nameField = new JTextField();
  private final JLabel nameErrorLabel = createErrorLabel();
  private final JTextField uuidField = new JTextField();
  private final StatefulRadioButton useField = new StatefulRadioButton("Display in pull-down lists:");
  private final JButton clearButton = new JButton("Clear");
  private final JButton saveButton = new JButton("Save");

  public EditProjectDialog(Frame owner, ReactiveViewRegistry reactiveViewRegistry, ProjectController projectController) {
    super(owner, "Create Project", true);
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.projectController = projectController;
  }

  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  @Override
  public void setVisible(boolean b) {
    super.setVisible(b);
    projectController.restoreDefaults();
  }

  private void initializeFields() {
    nameField.setText(projectController.getName());
    uuidField.setText(projectController.getUUID());
    uuidField.setEditable(false);
    useField.setSelectedValue(projectController.isUse());
    useField.setVisible(false);
  }
  
  private void setupLayout() {
    setLayout(new GridBagLayout());
    add(createLabelWithErrorPanel("Name", nameErrorLabel), configureLayout(0, 0, c -> c.weighty = 0));
    add(nameField, configureLayout(0 , 1, c -> c.weighty = 0));

    JPanel uuidPanel = new JPanel();
    uuidPanel.setLayout(new GridBagLayout());
    uuidPanel.add(new JLabel("UUID (automatically generated when you click save)"), configureLayout(0, 0, c -> c.weighty = 0));
    uuidPanel.add(uuidField, configureLayout(0, 1, c -> c.weighty = 0));
    add(uuidPanel, configureLayout(0, 3, c -> c.weighty = 100));
    
    JPanel footerPanel = new JPanel();
    footerPanel.setLayout(new BorderLayout());
    footerPanel.add(useField, BorderLayout.WEST);
    
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BorderLayout());
    buttonPanel.add(clearButton, BorderLayout.WEST);
    buttonPanel.add(saveButton, BorderLayout.EAST);
    footerPanel.add(buttonPanel, BorderLayout.EAST);
    
    add(footerPanel, configureLayout(0, 4, c -> c.weighty = 0));
  }
  
  private void setupMvc() {
    reactiveViewRegistry.register(this);
    
    nameField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        projectController.setName(nameField.getText());
      }
    });
    useField.addValueChangeListener(projectController::setUse);
    
    clearButton.addActionListener((evt) -> projectController.restoreDefaults());
    saveButton.addActionListener((evt) -> projectController.saveProject());
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case ProjectModel.UPDATE_NAME -> updateTextField(nameField, evt);
      case ProjectModel.UPDATE_NAME_ERROR -> updateLabelText(nameErrorLabel, evt);
      case ProjectModel.UPDATE_UUID -> updateTextField(uuidField, evt);
      case ProjectModel.UPDATE_USE -> updateStatefulRadioButton(useField, evt);
      case ProjectModel.EMIT_SAVE_FAILURE -> {
        OptionDialog collisionDialog = new OptionDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            "<html><B>This name already exists. Check the pull-down for the existing entry for this name. CruisePack requires unique names. If this is a new project, please modify the name to make it unique.</B></html>",
            List.of("OK")
        );
        
        collisionDialog.pack();
        collisionDialog.setVisible(true);
      }
    }
  }
}
