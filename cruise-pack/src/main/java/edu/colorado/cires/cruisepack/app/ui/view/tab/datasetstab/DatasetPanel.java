package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_DATASET_DATA_PATH_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_DATASET_PUBLIC_RELEASE_DATE_ERROR;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_DATA_PATH;
import static edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel.UPDATE_PUBLIC_RELEASE_DATE;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.createErrorLabel;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateDatePicker;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updateLabelText;
import static edu.colorado.cires.cruisepack.app.ui.util.FieldUtils.updatePathField;
import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import com.github.lgooddatepicker.components.DatePicker;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.dataset.BaseDatasetInstrumentController;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class DatasetPanel<M extends BaseDatasetInstrumentModel, C extends BaseDatasetInstrumentController<M>> extends JPanel {

  private static final String PUBLIC_RELEASE_DATE = "Public Release Date";
  private static final String REMOVE = "Remove";
  private static final String SELECT_DIR_LABEL = "...";
  private static final String PATH_LABEL = "Path To Data Files";



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
  private final List<DatasetListener<BaseDatasetInstrumentModel>> datasetRemovedListeners = new ArrayList<>(0);

  protected DatasetPanel(M model, C controller, InstrumentDatastore instrumentDatastore) {
    dataTypeName = instrumentDatastore.getNameForShortCode(getInstrumentShortCode());
    this.model = model;
    this.controller = controller;
    this.instrumentDatastore = instrumentDatastore;
  }

  public void addDatasetRemovedListener(DatasetListener<BaseDatasetInstrumentModel> listener) {
    datasetRemovedListeners.add(listener);
  }

  public void removeDatasetRemovedListener(DatasetListener<BaseDatasetInstrumentModel> listener) {
    datasetRemovedListeners.remove(listener);
  }

  protected abstract String getInstrumentShortCode();

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
  }

  private void setupLayout() {
    setLayout(new BorderLayout());
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createTitledBorder(dataTypeName));
    add(setupHeaderLayout(), BorderLayout.NORTH);
    add(createAndInitializeContentPanel(), BorderLayout.CENTER);
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

    return header;
  }

  private void setupMvc() {
//    packageDirectoryField.getDocument().addDocumentListener((SimpleDocumentListener) (evt) -> handleDirValue(packageDirectoryField.getText()));
    releaseDate.addDateChangeListener((evt) -> controller.setPublicReleaseDate(evt.getNewDate()));
    removeButton.addActionListener((evt) -> {
      for (DatasetListener<BaseDatasetInstrumentModel> listener : datasetRemovedListeners) {
        listener.handle(model);
      }
    });
  }

  public void onChange(PropertyChangeEvent evt) {
    if (UPDATE_PUBLIC_RELEASE_DATE.equals(evt.getPropertyName())) {
      updateDatePicker(releaseDate, evt);
    } else if (UPDATE_DATA_PATH.equals(evt.getPropertyName())) {
      updatePathField(directoryPath, evt);
    } else if (UPDATE_DATASET_DATA_PATH_ERROR.equals(evt.getPropertyName())) {
      updateLabelText(dataPathErrorLabel, evt);
    } else if (UPDATE_DATASET_PUBLIC_RELEASE_DATE_ERROR.equals(evt.getPropertyName())) {
      updateLabelText(publicReleaseDateErrorLabel, evt);
    }
    customOnChange(evt);
  }

  protected abstract void customOnChange(PropertyChangeEvent evt);
}
