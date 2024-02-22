package edu.colorado.cires.cruisepack.app.ui.view.tab.omicstab;

import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createLabelWithErrorPanel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateCheckBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updatePathField;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateStatefulRadioButton;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextArea;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.OmicsController;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.SimpleDocumentListener;
import edu.colorado.cires.cruisepack.app.ui.view.common.StatefulRadioButton;
import edu.colorado.cires.cruisepack.app.ui.view.tab.common.EditPersonDialog;
import edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab.PeopleList;
import jakarta.annotation.PostConstruct;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OmicsPanel extends JPanel implements ReactiveView {

  private final PeopleList peopleList;
  private final BeanFactory beanFactory;
  private final OmicsModel omicsModel;
  private final PersonDatastore personDatastore;
  private final OmicsController omicsController;
  private final ReactiveViewRegistry reactiveViewRegistry;

  private final StatefulRadioButton samplingConductedField = new StatefulRadioButton("Omics sampling conducted?");
  private final JComboBox<DropDownItem> contactField = new JComboBox<>();
  private final JLabel contactErrorLabel = createErrorLabel();
  private final JTextField trackingSheetField = new JTextField();
  private final JLabel trackingSheetErrorLabel = createErrorLabel();
  private final JTextField bioProjectAcessionField = new JTextField();
  private final JLabel bioProjectAccessionErrorLabel = createErrorLabel();
  
  // Sampling types
  private final JCheckBox waterField = new JCheckBox("Water");
  private final JCheckBox soilSedimentField = new JCheckBox("Soil/Sediment");
  private final JCheckBox organicTissueField = new JCheckBox("Organic Tissue");
  private final JLabel samplingTypesErrorLabel = createErrorLabel();
  
  // Expected analyses
  private final JCheckBox barcodingField = new JCheckBox("Barcoding");
  private final JCheckBox genomicsField = new JCheckBox("Genomics");
  private final JCheckBox transcriptomicsField = new JCheckBox("Transcriptomics");
  private final JCheckBox proteomicsField = new JCheckBox("Proteomics");
  private final JCheckBox metabolomicsField = new JCheckBox("Metabolomics");
  private final JCheckBox epigeneticsField = new JCheckBox("Epigenetics");
  private final JCheckBox otherField = new JCheckBox("Other");
  private final JCheckBox metabarcodingField = new JCheckBox("Metabarcoding");
  private final JCheckBox metagenomicsField = new JCheckBox("Metagenomics");
  private final JCheckBox metatranscriptomicsField = new JCheckBox("Metatranscriptomics");
  private final JCheckBox metaproteomicsField = new JCheckBox("Metaproteomics");
  private final JCheckBox metametabolomicsField = new JCheckBox("Metametabolomics");
  private final JCheckBox microbiomeField = new JCheckBox("Microbiome");
  private final JLabel expectedAnalysesErrorLabel = createErrorLabel();
  private final JButton editPeopleButton = new JButton("Create/Edit People");
  private final EditPersonDialog editPersonDialog;

  private final JTextArea additionalSamplingInformationField = new JTextArea();
  private final JLabel additionalSamplingInformationErrorLabel = new JLabel();

  @Autowired
  public OmicsPanel(PeopleList peopleList, BeanFactory beanFactory, OmicsModel omicsModel, PersonDatastore personDatastore, OmicsController omicsController, ReactiveViewRegistry reactiveViewRegistry, EditPersonDialog editPersonDialog) {
    this.peopleList = peopleList;
    this.beanFactory = beanFactory;
    this.omicsModel = omicsModel;
    this.personDatastore = personDatastore;
    this.omicsController = omicsController;
    this.reactiveViewRegistry = reactiveViewRegistry;
    this.editPersonDialog = editPersonDialog;
  }

  @PostConstruct
  public void init() {
    initializeFields();
    setupLayout();
    setupMvc();
  }

  private void handlePathValue(String value) {
    Path path = Paths.get(value);
    omicsController.setSampleTrackingSheet(path.toAbsolutePath().normalize());
  }

  private void initializeFields() {
    samplingConductedField.setSelectedValue(omicsModel.isSamplingConducted());
    contactField.setModel(new DefaultComboBoxModel<>(personDatastore.getEnabledPersonDropDowns().toArray(new DropDownItem[0])));
    contactField.setSelectedItem(omicsModel.getContact());
    trackingSheetField.setText(omicsModel.getSampleTrackingSheet() == null ? null : omicsModel.getSampleTrackingSheet().toAbsolutePath().normalize().toString());
    bioProjectAcessionField.setText(omicsModel.getBioProjectAccession());
    waterField.setSelected(omicsModel.getSamplingTypes().isWater());
    soilSedimentField.setSelected(omicsModel.getSamplingTypes().isSoilSediment());
    organicTissueField.setSelected(omicsModel.getSamplingTypes().isOrganicTissue());
    barcodingField.setSelected(omicsModel.getExpectedAnalyses().isBarcoding());
    genomicsField.setSelected(omicsModel.getExpectedAnalyses().isGenomics());
    transcriptomicsField.setSelected(omicsModel.getExpectedAnalyses().isTranscriptomics());
    proteomicsField.setSelected(omicsModel.getExpectedAnalyses().isProteomics());
    metabolomicsField.setSelected(omicsModel.getExpectedAnalyses().isMetabolomics());
    epigeneticsField.setSelected(omicsModel.getExpectedAnalyses().isEpigenetics());
    otherField.setSelected(omicsModel.getExpectedAnalyses().isOther());
    metabarcodingField.setSelected(omicsModel.getExpectedAnalyses().isMetabarcoding());
    metagenomicsField.setSelected(omicsModel.getExpectedAnalyses().isMetagenomics());
    metatranscriptomicsField.setSelected(omicsModel.getExpectedAnalyses().isMetatranscriptomics());
    metaproteomicsField.setSelected(omicsModel.getExpectedAnalyses().isMetaproteomics());
    metametabolomicsField.setSelected(omicsModel.getExpectedAnalyses().isMetametabolomics());
    microbiomeField.setSelected(omicsModel.getExpectedAnalyses().isMicrobiome());
    additionalSamplingInformationField.setText(omicsModel.getAdditionalSamplingInformation());
    additionalSamplingInformationErrorLabel.setText("");
    additionalSamplingInformationErrorLabel.setForeground(new Color(Color.RED.getRGB()));
  }

  private void handleFileSelect() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
      omicsController.setSampleTrackingSheet(fileChooser.getSelectedFile().toPath().toAbsolutePath().normalize());
    }
  }

  private void setupLayout() {
    setLayout(new GridBagLayout());

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    JPanel omicsPersonPanel = new JPanel();
    omicsPersonPanel.setLayout(new GridBagLayout());
    omicsPersonPanel.add(samplingConductedField, configureLayout(0, 0, c -> {
      c.weightx = 0;
      c.insets = new Insets(0, 0, 0, 100);
    }));
    omicsPersonPanel.add(contactErrorLabel, configureLayout(1, 0, c -> { c.weightx = 0; c.insets = new Insets(0, 0, 0, 5); }));
    omicsPersonPanel.add(contactField, configureLayout(2, 0, c -> {
      c.weightx = 100;
    }));
    omicsPersonPanel.add(editPeopleButton, configureLayout(3, 0, c -> {
      c.weightx = 0;
    }));
    panel.add(omicsPersonPanel, configureLayout(0, 0, c -> {
      c.gridwidth = GridBagConstraints.REMAINDER;
    }));

    JPanel trackingSheetNCBIAccessionPanel = new JPanel();
    trackingSheetNCBIAccessionPanel.setLayout(new GridBagLayout());
    JPanel trackingSheetPanel = new JPanel();
    trackingSheetPanel.setLayout(new GridBagLayout());
    trackingSheetPanel.add(createLabelWithErrorPanel("Sample Tracking Sheet", trackingSheetErrorLabel), configureLayout(0, 0, c -> {
      c.weightx = 0;
    }));
    trackingSheetPanel.add(trackingSheetField, configureLayout(1, 0, c -> {
      c.weightx = 100;
    }));

    JButton selectFileButton = new JButton("...");
    selectFileButton.addActionListener((evt) -> handleFileSelect());

    trackingSheetPanel.add(selectFileButton, configureLayout(2, 0, c -> {
      c.weightx = 0;
    }));
    trackingSheetNCBIAccessionPanel.add(trackingSheetPanel, configureLayout(0, 0, c -> {
      c.weightx = 50;
      c.insets = new Insets(0, 0, 0, 100);
    }));

    JPanel ncbiaAccessionPanel = new JPanel();
    ncbiaAccessionPanel.setLayout(new GridBagLayout());
    ncbiaAccessionPanel.add(createLabelWithErrorPanel("NCBI BioProject Accession", bioProjectAccessionErrorLabel), configureLayout(0, 0, c -> {
      c.weightx = 0;
    }));
    ncbiaAccessionPanel.add(bioProjectAcessionField, configureLayout(1, 0, c -> {
      c.weightx = 100;
    }));
    trackingSheetNCBIAccessionPanel.add(ncbiaAccessionPanel, configureLayout(1, 0, c -> {
      c.weightx = 50;
    }));

    panel.add(trackingSheetNCBIAccessionPanel, configureLayout(0, 1, c -> {
      c.gridwidth = GridBagConstraints.REMAINDER;
    }));

    JPanel samplingTypesPanel = new JPanel();
    samplingTypesPanel.setLayout(new BorderLayout());
    samplingTypesPanel.setBorder(new TitledBorder("Sampling Types"));

    JPanel samplingTypes = new JPanel();
    samplingTypes.setLayout(new GridBagLayout());
    samplingTypes.add(samplingTypesErrorLabel, configureLayout(0, 0, c -> c.insets = new Insets(0, 5, 0, 0)));
    samplingTypes.add(waterField, configureLayout(0, 1));
    samplingTypes.add(soilSedimentField, configureLayout(1, 1));
    samplingTypes.add(organicTissueField, configureLayout(2, 1));
    samplingTypesPanel.add(samplingTypes, BorderLayout.LINE_START);

    panel.add(samplingTypesPanel, configureLayout(0, 2, c -> c.gridwidth = GridBagConstraints.REMAINDER));

    JPanel expectedAnalyses = new JPanel();
    expectedAnalyses.setLayout(new GridBagLayout());
    expectedAnalyses.setBorder(new TitledBorder("Expected Analyses"));
    expectedAnalyses.add(expectedAnalysesErrorLabel, configureLayout(0, 0, c -> c.insets = new Insets(0, 5, 0, 0)));
    expectedAnalyses.add(barcodingField, configureLayout(0, 1));
    expectedAnalyses.add(genomicsField, configureLayout(1, 1));
    expectedAnalyses.add(transcriptomicsField, configureLayout(2, 1));
    expectedAnalyses.add(proteomicsField, configureLayout(3, 1));
    expectedAnalyses.add(metabolomicsField, configureLayout(4, 1, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    expectedAnalyses.add(epigeneticsField, configureLayout(0, 2));
    expectedAnalyses.add(otherField, configureLayout(1, 2));
    expectedAnalyses.add(metabarcodingField, configureLayout(2, 2));
    expectedAnalyses.add(metagenomicsField, configureLayout(3, 2));
    expectedAnalyses.add(metatranscriptomicsField, configureLayout(4, 2, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    expectedAnalyses.add(metaproteomicsField, configureLayout(0, 3));
    expectedAnalyses.add(metametabolomicsField, configureLayout(1, 3));
    expectedAnalyses.add(microbiomeField, configureLayout(2, 3, c -> c.gridwidth = GridBagConstraints.REMAINDER));

    panel.add(expectedAnalyses, configureLayout(0, 3, c -> c.gridwidth = GridBagConstraints.REMAINDER));

    JPanel additionalSamplingInformationLabelPanel = new JPanel();
    additionalSamplingInformationLabelPanel.setLayout(new BorderLayout(10, 0));
    additionalSamplingInformationLabelPanel.add(new JLabel("Additional Omics Sampling Information"), BorderLayout.LINE_START);
    additionalSamplingInformationLabelPanel.add(additionalSamplingInformationErrorLabel, BorderLayout.CENTER);

    panel.add(additionalSamplingInformationLabelPanel, configureLayout(0, 4, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    panel.add(new JScrollPane(additionalSamplingInformationField), configureLayout(0, 5, c -> {
      c.weighty = 100;
    }));

    add(panel, configureLayout(0, 0, c -> c.weighty = 100));
  }

  private void setupMvc() {
    reactiveViewRegistry.register(this);

    samplingConductedField.addValueChangeListener((v) -> omicsController.setSamplingConducted(v));
    contactField.addItemListener((e) -> omicsController.setContact((DropDownItem) e.getItem()));
    trackingSheetField.getDocument().addDocumentListener((SimpleDocumentListener)(evt) -> handlePathValue(trackingSheetField.getText()));
    bioProjectAcessionField.getDocument().addDocumentListener((SimpleDocumentListener)(evt) -> omicsController.setBioProjectAccession(bioProjectAcessionField.getText()));
    waterField.addItemListener(createItemListener(omicsController::setWaterSamplingType));
    soilSedimentField.addItemListener(createItemListener(omicsController::setSoilSedimentSamplingType));
    organicTissueField.addItemListener(createItemListener(omicsController::setOrganicTissueSamplingType));
    barcodingField.addItemListener(createItemListener(omicsController::setBarcodingExpectedAnalysis));
    genomicsField.addItemListener(createItemListener(omicsController::setGenomicsExpectedAnalysis));
    transcriptomicsField.addItemListener(createItemListener(omicsController::setTranscriptomicsExpectedAnalysis));
    proteomicsField.addItemListener(createItemListener(omicsController::setProteomicsExpectedAnalysis));
    metabolomicsField.addItemListener(createItemListener(omicsController::setMetabolomicsExpectedAnalysis));
    epigeneticsField.addItemListener(createItemListener(omicsController::setEpigeneticsExpectedAnalysis));
    otherField.addItemListener(createItemListener(omicsController::setOtherExpectedAnalysis));
    metabarcodingField.addItemListener(createItemListener(omicsController::setMetaBarcodingExpectedAnalysis));
    metagenomicsField.addItemListener(createItemListener(omicsController::setMetaGenomicsExpectedAnalysis));
    metatranscriptomicsField.addItemListener(createItemListener(omicsController::setMetatranscriptomicsExpectedAnalysis));
    metaproteomicsField.addItemListener(createItemListener(omicsController::setMetaproteomicsExpectedAnalysis));
    metametabolomicsField.addItemListener(createItemListener(omicsController::setMetametabolomicsExpectedAnalysis));
    microbiomeField.addItemListener(createItemListener(omicsController::setMicrobiomeExpectedAnalysis));
    additionalSamplingInformationField.getDocument().addDocumentListener((SimpleDocumentListener)(evt) -> omicsController.setAdditionalSamplingInformation(additionalSamplingInformationField.getText()));
    editPeopleButton.addActionListener(e -> {
      editPersonDialog.pack();
      editPersonDialog.setVisible(true);
    });
  }

  private ItemListener createItemListener(Consumer<Boolean> consumer) {
    return new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        consumer.accept(e.getStateChange() == 1);
      }
    };
  }

  @Override
  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case Events.UPDATE_OMICS_SAMPLING_CONDUCTED:
        updateStatefulRadioButton(samplingConductedField, evt);
        break;
      case Events.UPDATE_OMICS_CONTACT:
        updateComboBox(contactField, evt);
        break;
      case Events.UPDATE_OMICS_CONTACT_ERROR:
        updateLabelText(contactErrorLabel, evt);
        break;
      case Events.UPDATE_OMICS_SAMPLE_TRACKING_SHEET:
        updatePathField(trackingSheetField, evt);
        break;
      case Events.UPDATE_OMICS_SAMPLE_TRACKING_SHEET_ERROR:
        updateLabelText(trackingSheetErrorLabel, evt);
        break;
      case Events.UPDATE_OMICS_BIO_PROJECT_ACCESSION:
        updateTextField(bioProjectAcessionField, evt);
        break;
      case Events.UPDATE_OMICS_BIO_PROJECT_ACCESSION_ERROR:
        updateLabelText(bioProjectAccessionErrorLabel, evt);
        break;
      case Events.UPDATE_OMICS_WATER_SAMPLING_TYPE:
        updateCheckBox(waterField, evt);
        break;
      case Events.UPDATE_OMICS_SOIL_SEDIMENT_SAMPLING_TYPE:
        updateCheckBox(soilSedimentField, evt);
        break;
      case Events.UPDATE_OMICS_ORGANIC_TISSUE_SAMPLING_TYPE:
        updateCheckBox(organicTissueField, evt);
        break;
      case Events.UPDATE_OMICS_BARCODING_EXPECTED_ANALYSIS:
        updateCheckBox(barcodingField, evt);
        break;
      case Events.UPDATE_OMICS_GENOMICS_EXPECTED_ANALYSIS:
        updateCheckBox(genomicsField, evt);
        break;
      case Events.UPDATE_OMICS_TRANSCRIPTOMICS_EXPECTED_ANALYSIS:
        updateCheckBox(transcriptomicsField, evt);
        break;
      case Events.UPDATE_OMICS_PROTEOMICS_EXPECTED_ANALYSIS:
        updateCheckBox(proteomicsField, evt);
        break;
      case Events.UPDATE_OMICS_METABOLOMICS_EXPECTED_ANALYSIS:
        updateCheckBox(metabolomicsField, evt);
        break;
      case Events.UPDATE_OMICS_EPIGENETICS_EXPECTED_ANALYSIS:
        updateCheckBox(epigeneticsField, evt);
        break;
      case Events.UPDATE_OMICS_OTHER_EXPECTED_ANALYSIS:
        updateCheckBox(otherField, evt);
        break;
      case Events.UPDATE_OMICS_METABARCODING_EXPECTED_ANALYSIS:
        updateCheckBox(metabarcodingField, evt);
        break;
      case Events.UPDATE_OMICS_METAGENOMICS_EXPECTED_ANALYSIS:
        updateCheckBox(metagenomicsField, evt);
        break;
      case Events.UPDATE_OMICS_METATRANSCRIPTOMICS_EXPECTED_ANALYSIS:
        updateCheckBox(metatranscriptomicsField, evt);
        break;
      case Events.UPDATE_OMICS_METAPROTEOMICS_EXPECTED_ANALYSIS:
        updateCheckBox(metaproteomicsField, evt);
        break;
      case Events.UPDATE_OMICS_METAMETABOLOMICS_EXPECTED_ANALYSIS:
        updateCheckBox(metametabolomicsField, evt);
        break;
      case Events.UPDATE_OMICS_MICROBIOME_EXPECTED_ANALYSIS:
        updateCheckBox(microbiomeField, evt);
        break;
      case Events.UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION:
        updateTextArea(additionalSamplingInformationField, evt);
        break;
      case Events.UPDATE_OMICS_ADDITIONAL_SAMPLING_INFORMATION_ERROR:
        updateLabelText(additionalSamplingInformationErrorLabel, evt);
        break;
      case Events.UPDATE_OMICS_SAMPLING_TYPES_ERROR:
        updateLabelText(samplingTypesErrorLabel, evt);
        break;
      case Events.UPDATE_OMICS_EXPECTED_ANALYSES_ERROR:
        updateLabelText(expectedAnalysesErrorLabel, evt);
      default:
        break;
    }
  }
}
