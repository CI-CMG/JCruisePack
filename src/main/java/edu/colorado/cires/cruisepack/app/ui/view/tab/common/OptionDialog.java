package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class OptionDialog extends JDialog {

  private final Map<String, JButton> buttons;
    private final List<String> buttonLabels;
    private final JLabel label;

    public OptionDialog(Frame owner, String dialogMessage, List<String> buttonLabels) {
      super(owner, null, true);
      this.label = new JLabel(dialogMessage);
      this.buttonLabels = buttonLabels;
      this.buttons = buttonLabels.stream()
          .collect(Collectors.toMap(
              l -> l,
              JButton::new
          ));
      setLocationRelativeTo(owner);
      init();
    }

    public JLabel getLabel() {
        return label;
    }

    public void addListener(String buttonLabel, ActionListener listener) {
        Optional.ofNullable(buttons.get(buttonLabel))
            .ifPresent(b -> b.addActionListener(listener));
    }

    public void removeListener(String buttonLabel, ActionListener listener) {
        Optional.ofNullable(buttons.get(buttonLabel))
            .ifPresent(b -> b.removeActionListener(listener));
    }

    private void init() {
        setupLayout();
        setupDefaultListeners();
    }

    private void setupLayout() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new GridBagLayout());
        label.setHorizontalAlignment(JLabel.CENTER);
        add(label, configureLayout(0, 0, c -> {
            c.weighty = 100;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.insets = new Insets(20, 20, 20, 20);
        }));
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        
        for (int i = 0; i < buttons.size(); i++) {
            buttonPanel.add(buttons.get(buttonLabels.get(i)), configureLayout(i, 0));
        }

        panel.add(buttonPanel, BorderLayout.EAST);
        add(panel, configureLayout(0, 1, c -> c.weighty = 0));
    }

    private void setupDefaultListeners() {
        buttons.values().forEach(b -> b.addActionListener((evt) -> close()));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                close();
            }
        });
    }

    private void close() {
        setVisible(false);
    }
    
}
