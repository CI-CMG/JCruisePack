package edu.colorado.cires.cruisepack.app.ui.view.common;

import static edu.colorado.cires.cruisepack.app.ui.util.LayoutUtils.configureLayout;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class StatefulRadioButton extends JPanel {

    private boolean selectedValue;
    private final String label;
    private final JRadioButton yesButton = new JRadioButton("Yes");
    private final JRadioButton noButton = new JRadioButton("No");
    

    public StatefulRadioButton(String label) {
        this.label = label;
        init();
    }

    

    private void init() {
        setLayout(new GridBagLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(new JLabel(label));

        yesButton.addActionListener(e -> {
            setSelectedValue(yesButton.isSelected());
        });

        panel.add(yesButton);

        noButton.addActionListener(e -> {
            setSelectedValue(!noButton.isSelected());
        });

        panel.add(noButton);

        add(panel, configureLayout(0, 0));
    }

    private void setSelectedValue(boolean value) {
        selectedValue = value;

        if (value) {
            yesButton.setSelected(true);
            noButton.setSelected(false);
        } else {
            yesButton.setSelected(false);
            noButton.setSelected(true);
        }
    }



    public boolean isSelected() {
        return selectedValue;
    }

}