package edu.colorado.cires.cruisepack.app.ui.view.tab.common;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import org.springframework.beans.factory.BeanFactory;
import edu.colorado.cires.cruisepack.app.ui.view.MainFrame;
import edu.colorado.cires.cruisepack.app.ui.view.tab.peopletab.OrganizationList;

public class EditOrgDialog extends JComponent {

    private static final String ORG_EDITOR_HEADER = "Organization Editor";

    public EditOrgDialog(BeanFactory beanFactory, OrganizationList organizationList) {
        this.init(beanFactory, organizationList);
    }

    private void init(BeanFactory beanFactory, OrganizationList organizationList) {
        JDialog dialog = new JDialog(beanFactory.getBean(MainFrame.class), ORG_EDITOR_HEADER, true);
        dialog.setLayout(new BorderLayout(0, 0));
        dialog.add(new OrgForm(organizationList));
        dialog.pack();
        dialog.setVisible(true);
    }
    

}