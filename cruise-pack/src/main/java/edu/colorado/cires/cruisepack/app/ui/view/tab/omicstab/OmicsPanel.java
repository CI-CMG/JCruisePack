package edu.colorado.cires.cruisepack.app.ui.view.tab.omicstab;

import jakarta.annotation.PostConstruct;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.ui.view.tab.common.EditPersonDialog;
import edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab.PeopleList;

@Component
public class OmicsPanel extends JPanel {

  private final PeopleList peopleList;
  private final BeanFactory beanFactory;

  @Autowired
  public OmicsPanel(PeopleList peopleList, BeanFactory beanFactory) {
    this.peopleList = peopleList;
    this.beanFactory = beanFactory;
  }

  @PostConstruct
  public void init() {
    setLayout(new BorderLayout(5, 5));

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    panel.add(new JLabel("Omics sampling conducted?"), configureLayout(0, 0));
    panel.add(new JRadioButton("Yes"), configureLayout(1, 0));
    panel.add(new JRadioButton("No"), configureLayout(2, 0));
    panel.add(new JComboBox<>(peopleList.getPeopleList()), configureLayout(3, 0));
    JButton editPeopleButton = new JButton("Create/Edit People");
    editPeopleButton.addActionListener(e -> new EditPersonDialog(beanFactory, peopleList));
    panel.add(editPeopleButton, configureLayout(4, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));

    panel.add(new JLabel("Sample Tracking Sheet"), configureLayout(0, 1));
    panel.add(new JTextField(), configureLayout(1, 1));
    JButton selectFileButton = new JButton("Select File");
    selectFileButton.addActionListener(e -> {
      JDialog dialog = new JDialog();
      dialog.add(new JFileChooser());
      dialog.pack();
      dialog.setVisible(true);
    });
    panel.add(selectFileButton, configureLayout(2, 1));
    panel.add(new JLabel("NCBI BioProject Accession:"), configureLayout(3, 1));
    panel.add(new JTextField(), configureLayout(4, 1, c -> c.gridwidth = GridBagConstraints.REMAINDER));

    JPanel samplingTypes = new JPanel();
    samplingTypes.setLayout(new GridBagLayout());
    samplingTypes.setBorder(new TitledBorder("Sampling Types"));
    samplingTypes.add(new JCheckBox("Water"), configureLayout(0, 0));
    samplingTypes.add(new JCheckBox("Soil/Sediment"), configureLayout(1, 0));
    samplingTypes.add(new JCheckBox("Organic Tissue"), configureLayout(2, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));

    panel.add(samplingTypes, configureLayout(0, 2, c -> c.gridwidth = GridBagConstraints.REMAINDER));

    JPanel expectedAnalyses = new JPanel();
    expectedAnalyses.setLayout(new GridBagLayout());
    expectedAnalyses.setBorder(new TitledBorder("Expected Analyses"));
    expectedAnalyses.add(new JCheckBox("Barcoding"), configureLayout(0, 0));
    expectedAnalyses.add(new JCheckBox("Genomics"), configureLayout(1, 0));
    expectedAnalyses.add(new JCheckBox("Transcriptomics"), configureLayout(2, 0));
    expectedAnalyses.add(new JCheckBox("Proteomics"), configureLayout(3, 0));
    expectedAnalyses.add(new JCheckBox("Metabolomics"), configureLayout(4, 0, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    expectedAnalyses.add(new JCheckBox("Epigenetics"), configureLayout(0, 1));
    expectedAnalyses.add(new JCheckBox("Other"), configureLayout(1, 1));
    expectedAnalyses.add(new JCheckBox("Metabarcoding"), configureLayout(2, 1));
    expectedAnalyses.add(new JCheckBox("Metagenomics"), configureLayout(3, 1));
    expectedAnalyses.add(new JCheckBox("Metatranscriptomics"), configureLayout(4, 1, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    expectedAnalyses.add(new JCheckBox("Metaproteomics"), configureLayout(0, 2));
    expectedAnalyses.add(new JCheckBox("Metametabolomics"), configureLayout(1, 2));
    expectedAnalyses.add(new JCheckBox("Microbiome"), configureLayout(2, 2, c -> c.gridwidth = GridBagConstraints.REMAINDER));

    panel.add(expectedAnalyses, configureLayout(0, 3, c -> c.gridwidth = GridBagConstraints.REMAINDER));

    panel.add(new JLabel("Additional Omics Sampling Information"), configureLayout(0, 4, c -> c.gridwidth = GridBagConstraints.REMAINDER));
    panel.add(new JTextArea(), configureLayout(0, 5, c -> {
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.ipady = 300;
    }));

    add(panel, BorderLayout.PAGE_START);
  }
}
