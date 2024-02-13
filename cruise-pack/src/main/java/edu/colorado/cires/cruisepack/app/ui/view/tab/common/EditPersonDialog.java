package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import org.springframework.beans.factory.BeanFactory;
import edu.colorado.cires.cruisepack.app.ui.view.MainFrame;
import edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab.PeopleList;

public class EditPersonDialog extends JComponent {

    private static final String PEOPLE_EDITOR_HEADER = "People Editor";

    
    public EditPersonDialog(BeanFactory beanFactory, PeopleList peopleList) {
        this.init(beanFactory, peopleList);
    }

    private void init(BeanFactory beanFactory, PeopleList peopleList) {
        JDialog dialog = new JDialog(beanFactory.getBean(MainFrame.class), PEOPLE_EDITOR_HEADER, true);
        dialog.setLayout(new BorderLayout(0, 0));
        dialog.add(new PersonForm(peopleList));
        dialog.pack();
        dialog.setVisible(true);
    }


}