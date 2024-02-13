package edu.colorado.cires.cruisepack.app.datastore;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.organization.Organization;
import edu.colorado.cires.cruisepack.xml.organization.OrganizationData;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

@Component
public class OrganizationDatasore {
    public static final DropDownItem UNSELECTED_ORGANIZATION = new DropDownItem("", "Select Organization");

    private final ServiceProperties serviceProperties;
    private List<DropDownItem> organizationDropDowns;

    @Autowired
    public OrganizationDatasore(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    @PostConstruct
    public void init() {
        Path workDir = Paths.get(serviceProperties.getWorkDir());
        Path dataDir = workDir.resolve("data");
        Path organizationsFile = dataDir.resolve("organizations.xml");
        if (!Files.isRegularFile(organizationsFile)) {
            throw new IllegalStateException("Unable to read " + organizationsFile);
        }
        OrganizationData organizationData;
        try (Reader reader = Files.newBufferedReader(organizationsFile)) {
            organizationData = (OrganizationData) JAXBContext.newInstance(OrganizationData.class)
                .createUnmarshaller().unmarshal(reader);
        } catch (IOException | JAXBException e) {
            throw new IllegalStateException("Unable to parse " + organizationsFile, e);
        }
        organizationDropDowns = new ArrayList<>(organizationData.getOrganizations().getOrganizations().size() + 1);
        organizationDropDowns.add(UNSELECTED_ORGANIZATION);
        organizationData.getOrganizations().getOrganizations().stream()
            .filter(Organization::isUse)
            .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
            .map(organization -> new DropDownItem(organization.getUuid(), organization.getName()))
            .forEach(organizationDropDowns::add);
    }

    public List<DropDownItem> getOrganizationDropDowns() {
        return organizationDropDowns;
    }
}
