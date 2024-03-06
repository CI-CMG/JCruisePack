package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_ANCILLARY_DETAILS;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_ANCILLARY_DETAILS_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_ANCILLARY_PATH;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_ANCILLARY_PATH_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_COMMENTS;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_COMMENTS_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_DATASET_DATA_PATH_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_DATASET_PUBLIC_RELEASE_DATE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_DATA_PATH;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_INSTRUMENT;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_INSTRUMENT_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_PROCESSING_LEVEL_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_PUBLIC_RELEASE_DATE;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.setSelectedButton;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateComboBox;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateDatePicker;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updatePathField;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateRadioButtonGroup;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateTextField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import com.github.lgooddatepicker.components.DatePicker;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.BaseDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.common.SimpleDocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class DatasetPanel<M extends BaseDatasetInstrumentModel, C extends BaseDatasetInstrumentController<M>> extends JPanel {

  private static final String PUBLIC_RELEASE_DATE = "Public Release Date";
  private static final String REMOVE = "Remove";
  private static final String SELECT_DIR_LABEL = "...";
  private static final String PATH_LABEL = "Path To Data Files";
  private static final String ANCILLARY_PATH_LABEL = "Path to Ancillary Files";
  private static final String ANCILLARY_DETAILS_LABEL = "Ancillary Data Details";



  private final String dataTypeName;
  protected final M model;
  protected final C controller;
  protected final InstrumentDatastore instrumentDatastore;

  private final DatePicker releaseDate = new DatePicker();
  private final JLabel publicReleaseDateErrorLabel = createErrorLabel();
  private final JButton removeButton = new JButton(REMOVE);
  private final JTextField directoryPath = new JTextField();
  private final JLabel dataPathErrorLabel = createErrorLabel();
  private final JButton dirSelectButton = new JButton(SELECT_DIR_LABEL);
  private final List<DatasetListener> datasetRemovedListeners = new ArrayList<>(0);
  private final JTextField ancillaryPath = new JTextField();
  private final JLabel ancillaryPathErrorLabel = createErrorLabel();
  private final CommentsTextAreaPanel ancillaryDetails = new CommentsTextAreaPanel(ANCILLARY_DETAILS_LABEL);
  private final JButton ancillaryPathSelectButton = new JButton(SELECT_DIR_LABEL);
  private final LabeledComboBoxPanel instrumentPanel = new LabeledComboBoxPanel();
  private final ProcessingLevelRadioPanel buttonPanel = new ProcessingLevelRadioPanel();
  private final CommentsTextAreaPanel commentsPanel = new CommentsTextAreaPanel();
  
  protected DatasetPanel(M model, C controller, InstrumentDatastore instrumentDatastore) {
    this.dataTypeName = instrumentDatastore.getNameForShortCode(model.getInstrumentGroupShortCode());
    this.model = model;
    this.controller = controller;
    this.instrumentDatastore = instrumentDatastore;
  }

  public void addDatasetRemovedListener(DatasetListener listener) {
    datasetRemovedListeners.add(listener);
  }

  protected abstract JPanel createAndInitializeContentPanel();

  public void init() {
    controller.setView(this);
    initializeFields();
    setupLayout();
    setupMvc();
  }

  public M getModel() {
    return model;
  }

  private void initializeFields() {
    releaseDate.setDate(model.getPublicReleaseDate());
    directoryPath.setText(model.getDataPath() == null ? null : model.getDataPath().toAbsolutePath().normalize().toString());
    instrumentPanel.getInstrumentField().setModel(
        new DefaultComboBoxModel<>(instrumentDatastore.getInstrumentDropDownsForDatasetType(model.getInstrumentGroupShortCode()).toArray(new DropDownItem[0]))
    );
    instrumentPanel.getInstrumentField().setSelectedItem(model.getInstrument());
    setSelectedButton(buttonPanel.getProcessingLevelGroup(), model.getProcessingLevel());
    commentsPanel.getCommentsField().setText(model.getComments());
  }

  private void setupLayout() {
    setLayout(new BorderLayout());
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createTitledBorder(dataTypeName));
    
    add(setupHeaderLayout(), BorderLayout.NORTH);
    
    JPanel contentPanel = createAndInitializeContentPanel();
    if (contentPanel != null) {
      add(createAndInitializeContentPanel(), BorderLayout.CENTER);
    }
    
    add(setupFooterLayout(), BorderLayout.SOUTH);
  }

  private JPanel setupHeaderLayout() {
    JPanel header = new JPanel();
    header.setLayout(new GridBagLayout());
    header.setBackground(Color.WHITE);
    JPanel row1 = new JPanel();
    row1.setLayout(new GridBagLayout());
    row1.setBackground(Color.WHITE);
    row1.add(removeButton, configureLayout(0, 0, c -> {
      c.weightx = 0;
      c.fill = GridBagConstraints.NONE;
    }));

    JPanel releaseDatePanel = new JPanel();
    releaseDatePanel.setBackground(Color.WHITE);
    releaseDatePanel.setBorder(BorderFactory.createTitledBorder(PUBLIC_RELEASE_DATE));
    releaseDatePanel.add(publicReleaseDateErrorLabel);
    releaseDatePanel.add(releaseDate);
    row1.add(releaseDatePanel, configureLayout(1, 0, c -> {
      c.weightx = 1;
      c.anchor = GridBagConstraints.LINE_END;
      c.fill = GridBagConstraints.NONE;
    }));
    header.add(row1, configureLayout(0, 0));

    JPanel directorySelectPanel = new JPanel();
    directorySelectPanel.setLayout(new GridBagLayout());
    directorySelectPanel.setBackground(Color.WHITE);
    directorySelectPanel.setBorder(BorderFactory.createTitledBorder(PATH_LABEL));
    directorySelectPanel.add(dataPathErrorLabel, configureLayout(0, 0, c -> c.weightx = 1));
    directorySelectPanel.add(directoryPath, configureLayout(0, 1, c -> c.weightx = 1));
    directorySelectPanel.add(dirSelectButton, configureLayout(1, 1, c -> c.weightx = 0));
    header.add(directorySelectPanel, configureLayout(0, 1));

    JPanel instrumentProcessingLevelPanel = new JPanel();
    instrumentProcessingLevelPanel.setLayout(new GridBagLayout());
    instrumentProcessingLevelPanel.setBackground(Color.WHITE);
    instrumentProcessingLevelPanel.add(instrumentPanel, configureLayout(0, 0));
    instrumentProcessingLevelPanel.add(buttonPanel, configureLayout(1, 0, c -> c.weightx = 0));
    
    header.add(instrumentProcessingLevelPanel, configureLayout(0, 2));

    return header;
  }
  
  private JPanel setupFooterLayout() {
    JPanel footer = new JPanel();
    footer.setLayout(new GridBagLayout());
    footer.setBackground(Color.WHITE);

    JPanel ancillaryPathPanel = new JPanel();
    ancillaryPathPanel.setLayout(new GridBagLayout());
    ancillaryPathPanel.setBackground(Color.WHITE);
    ancillaryPathPanel.setBorder(BorderFactory.createTitledBorder(ANCILLARY_PATH_LABEL));
    ancillaryPathPanel.add(ancillaryPathErrorLabel, configureLayout(0 , 0, c -> c.weightx = 1));
    ancillaryPathPanel.add(ancillaryPath, configureLayout(0, 1, c -> c.weightx = 1));
    ancillaryPathPanel.add(ancillaryPathSelectButton, configureLayout(1, 1, c -> c.weightx = 0));
    
    footer.add(ancillaryPathPanel, configureLayout(0, 0));
    footer.add(ancillaryDetails, configureLayout(0, 1));
    footer.add(commentsPanel, configureLayout(0, 2));
    
    return footer;
  }

  private void setupMvc() {
    directoryPath.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        handleDirValue(directoryPath.getText(), controller::setDataPath);
      }
    });
    ancillaryPath.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        handleDirValue(ancillaryPath.getText(), controller::setAncillaryPath);
      }
    });
    ancillaryDetails.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        controller.setAncillaryDetails(ancillaryDetails.getCommentsField().getText());
      }
    });
    releaseDate.addDateChangeListener((evt) -> controller.setPublicReleaseDate(evt.getNewDate()));
    removeButton.addActionListener((evt) -> {
      for (DatasetListener listener : datasetRemovedListeners) {
        listener.handle(this);
      }
    });
    
    dirSelectButton.addActionListener((evt) -> handleDirSelect(controller::setDataPath));
    ancillaryPathSelectButton.addActionListener((evt) -> handleDirSelect(controller::setAncillaryPath));

    instrumentPanel.getInstrumentField().addItemListener((evt) -> controller.setInstrument((DropDownItem) evt.getItem()));
    buttonPanel.addActionListener((evt) -> controller.setProcessingLevel(buttonPanel.getSelectedButtonText()));
    commentsPanel.getCommentsField().getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> controller.setComments(commentsPanel.getCommentsField().getText()));
  }

  private void handleDirSelect(Consumer<Path> consumer) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
      consumer.accept(fileChooser.getSelectedFile().toPath().toAbsolutePath().normalize());
    }
  }

  public void onChange(PropertyChangeEvent evt) {
    switch (evt.getPropertyName()) {
      case UPDATE_PUBLIC_RELEASE_DATE -> updateDatePicker(releaseDate, evt);
      case UPDATE_DATA_PATH -> updatePathField(directoryPath, evt);
      case UPDATE_DATASET_DATA_PATH_ERROR -> updateLabelText(dataPathErrorLabel, evt);
      case UPDATE_DATASET_PUBLIC_RELEASE_DATE_ERROR -> updateLabelText(publicReleaseDateErrorLabel, evt);
      case UPDATE_ANCILLARY_PATH -> updatePathField(ancillaryPath, evt);
      case UPDATE_ANCILLARY_PATH_ERROR -> updateLabelText(ancillaryPathErrorLabel, evt);
      case UPDATE_ANCILLARY_DETAILS -> updateTextField(ancillaryDetails.getCommentsField(), evt);
      case UPDATE_ANCILLARY_DETAILS_ERROR -> updateLabelText(ancillaryDetails.getErrorLabel(), evt);
      case UPDATE_INSTRUMENT -> updateComboBox(instrumentPanel.getInstrumentField(), evt);
      case UPDATE_PROCESSING_LEVEL -> updateRadioButtonGroup(buttonPanel.getProcessingLevelGroup(), evt);
      case UPDATE_COMMENTS -> updateTextField(commentsPanel.getCommentsField(), evt);
      case UPDATE_INSTRUMENT_ERROR -> updateLabelText(instrumentPanel.getErrorLabel(), evt);
      case UPDATE_PROCESSING_LEVEL_ERROR -> updateLabelText(buttonPanel.getErrorLabel(), evt);
      case UPDATE_COMMENTS_ERROR -> updateLabelText(commentsPanel.getErrorLabel(), evt);
    }
    
    customOnChange(evt);
  }

  protected abstract void customOnChange(PropertyChangeEvent evt);

  private void handleDirValue(String value, Consumer<Path> consumer) {
    Path path = Paths.get(value);
    consumer.accept(path.toAbsolutePath().normalize());
  }
}
