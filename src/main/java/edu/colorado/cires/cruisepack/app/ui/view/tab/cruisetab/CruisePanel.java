package edu.colorado.cires.cruisepack.app.ui.view.tab.cruisetab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createLabelWithErrorPanel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.ui.controller.CruiseInformationController;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.SimpleDocumentListener;
import jakarta.annotation.PostConstruct;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CruisePanel extends JPanel implements ReactiveView {

  private static final String CRUISE_TITLE_LABEL = "Cruise Title";
  private static final String CRUISE_PURPOSE_LABEL = "Cruise Purpose";
  private static final String CRUISE_DESCRIPTION_LABEL = "Cruise Description (overview of cruise-level metadata)";

  private final JTextField cruiseTitleField = new JTextField();
  private final JTextArea cruisePurposeField = new JTextArea();
  private final JTextArea cruiseDescriptionField = new JTextArea();

  private final JLabel cruiseTitleErrorLabel = createErrorLabel();
  private final JLabel cruisePurposeErrorLabel = createErrorLabel();
  private final JLabel cruiseDescriptionErrorLabel = createErrorLabel();
  
  private final CruiseDocumentsPanel cruiseDocumentsPanel;
  private final CruiseInformationModel cruiseInformationModel;
  private final ReactiveViewRegistry reactiveViewRegistry;
  private final CruiseInformationController cruiseInformationController;

  @Autowired
  public CruisePanel(CruiseDocumentsPanel cruiseDocumentsPanel, CruiseInformationModel cruiseInformationModel,
      ReactiveViewRegistry reactiveViewRegistry, CruiseInformationController cruiseInformationController) {
    this.cruiseDocumentsPanel = cruiseDocumentsPanel;
    this.cruiseInformationModel = cruiseInformationModel;
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.cruiseInformationController = cruiseInformationController;
  }

  @PostConstruct
  public void init() {
    setLayout(new GridBagLayout());
    add(createLabelWithErrorPanel(CRUISE_TITLE_LABEL, cruiseTitleErrorLabel), configureLayout(0, 0, c -> c.weighty = 0));
    add(cruiseTitleField, configureLayout(0, 1, c -> c.weighty = 0));
    add(createLabelWithErrorPanel(CRUISE_PURPOSE_LABEL, cruisePurposeErrorLabel), configureLayout(0, 2, c -> c.weighty = 0));
    add(cruisePurposeField, configureLayout(0, 3, c -> { c.insets = new Insets(4, 3, 4, 3); c.weighty = 100; }));
    add(createLabelWithErrorPanel(CRUISE_DESCRIPTION_LABEL, cruiseDescriptionErrorLabel), configureLayout(0, 4, c -> c.weighty = 0));
    add(cruiseDescriptionField, configureLayout(0, 5, c -> { c.insets = new Insets(4, 3, 4, 3); c.weighty = 100; }));
    add(cruiseDocumentsPanel, configureLayout(0, 6, c -> c.weighty = 0));

    cruiseTitleField.setText(cruiseInformationModel.getCruiseTitle());
    cruisePurposeField.setText(cruiseInformationModel.getCruisePurpose());
    cruiseDescriptionField.setText(cruiseInformationModel.getCruiseDescription());

    reactiveViewRegistry.register(this);

    cruiseTitleField.getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> cruiseInformationController.setCruiseTitle(cruiseTitleField.getText()));
    cruisePurposeField.getDocument().addDocumentListener((SimpleDocumentListener)(evt) -> cruiseInformationController.setCruisePurpose(cruisePurposeField.getText()));
    cruiseDescriptionField.getDocument().addDocumentListener((SimpleDocumentListener)(evt) -> cruiseInformationController.setCruiseDescription(cruiseDescriptionField.getText()));

  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.UPDATE_CRUISE_TITLE:
        updateTextField(cruiseTitleField, evt);
        break;
      case Events.UPDATE_CRUISE_PURPOSE:
        updateTextField(cruisePurposeField, evt);
        break;
      case Events.UPDATE_CRUISE_DESCRIPTION:
        updateTextField(cruiseDescriptionField, evt);
        break;
      case Events.UPDATE_CRUISE_TITLE_ERROR:
        updateLabelText(cruiseTitleErrorLabel, evt);
        break;
      case Events.UPDATE_CRUISE_PURPOSE_ERROR:
        updateLabelText(cruisePurposeErrorLabel, evt);
        break;
      case Events.UPDATE_CRUISE_DESCRIPTION_ERROR:
        updateLabelText(cruiseDescriptionErrorLabel, evt);
        break;
      default:
        break;
    }
  }
}
