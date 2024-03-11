package edu.colorado.cires.cruisepack.app.ui.view.tab.common;


import java.util.function.Consumer;

import edu.colorado.cires.cruisepack.app.ui.view.common.StatefulRadioButton;

public class RadioButtonFormField extends StatefulRadioButton {

    private final Consumer<Boolean> valueSetter;

    public RadioButtonFormField(String label, Consumer<Boolean> valueSetter) {
        super(label);
        this.valueSetter = valueSetter;
        setSelectedValue(false);
    }

    @Override
    public void setSelectedValue(Boolean value) {
        valueSetter.accept(value);
        // super.setSelectedValue(value);
    }
    
}
