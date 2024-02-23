package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class SaveBeforeExitDialog extends JDialog {

    private final String dialogMessage;
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton noButton = new JButton("No");
    private final JButton yesButton = new JButton("Yes");

    public SaveBeforeExitDialog(String dialogMessage) {
        super((JFrame) null, null, true);
        this.dialogMessage = dialogMessage;
        init();
    }

    public void addNoListener(ActionListener listener) {
        noButton.addActionListener(listener);
    }

    public void removeNoListener(ActionListener listener) {
        noButton.removeActionListener(listener);
    }

    public void addYesListener(ActionListener listener) {
        yesButton.addActionListener(listener);
    }

    public void removeYesListener(ActionListener listener) {
        yesButton.removeActionListener(listener);
    }

    private void init() {
        setupLayout();
        setupDefaultListeners();
    }

    private void setupLayout() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new GridBagLayout());
        JLabel label = new JLabel(dialogMessage);
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
        buttonPanel.add(cancelButton, configureLayout(0, 0));
        buttonPanel.add(noButton, configureLayout(1, 0));
        buttonPanel.add(yesButton, configureLayout(2, 0));

        panel.add(buttonPanel, BorderLayout.EAST);
        add(panel, configureLayout(0, 1, c -> c.weighty = 0));
    }

    private void setupDefaultListeners() {
        cancelButton.addActionListener((evt) -> close());
        noButton.addActionListener((evt) -> close());
        yesButton.addActionListener((evt) -> close());
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
