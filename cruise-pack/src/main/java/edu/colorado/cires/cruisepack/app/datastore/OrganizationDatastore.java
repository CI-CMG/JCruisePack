package edu.colorado.cires.cruisepack.app.datastore;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.model.OrganizationModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.organization.Organization;
import edu.colorado.cires.cruisepack.xml.organization.OrganizationData;
import edu.colorado.cires.cruisepack.xml.organization.OrganizationList;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

@Component
public class OrganizationDatastore {
    public static final DropDownItem UNSELECTED_ORGANIZATION = new DropDownItem("", "Select Organization");

    private final ServiceProperties serviceProperties;
    private List<DropDownItem> organizationDropDowns;

    @Autowired
    public OrganizationDatastore(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    @PostConstruct
    public void init() {
        organizationDropDowns = mergeDropDownItemLists(readOrganizations("data"), readOrganizations("local-data"));
        organizationDropDowns.add(UNSELECTED_ORGANIZATION);
    }

    public List<DropDownItem> getOrganizationDropDowns() {
        return organizationDropDowns;
    }

    public void save(OrganizationModel organizationModel) {
        Organization organization = organizationFromModel(organizationModel);
        DropDownItem dropDownItem = new DropDownItem(organization.getUuid(), organization.getName(), organization);
        List<DropDownItem> dropDownItems = mergeDropDownItemLists(
            readOrganizations("local-data"),
             Collections.singletonList(dropDownItem)
        );

        OrganizationData organizationData = new OrganizationData();
        organizationData.setDataVersion("1.0");
        OrganizationList organizationList = new OrganizationList();
        List<Organization> organizations = organizationList.getOrganizations();
        organizations.addAll(
            dropDownItems.stream()
            .map(i -> (Organization) i.getRecord())
            .collect(Collectors.toList())
        );
        organizationData.setOrganizations(organizationList);

        Path workDir = Paths.get(serviceProperties.getWorkDir());
        Path dataDir = workDir.resolve("local-data");
        Path organizationsFile = dataDir.resolve("organizations.xml");

        try (OutputStream outputStream = new FileOutputStream(organizationsFile.toFile())) {
            JAXB.marshal(organizationData, outputStream);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to save drop down items: ", e);
        }

        init();
    }

    private Organization organizationFromModel(OrganizationModel organizationModel) {
        Organization organization = new Organization();
        organization.setName(organizationModel.getName());
        organization.setStreet(organizationModel.getStreet()); 
        organization.setCity(organizationModel.getCity());
        organization.setState(organizationModel.getState());
        organization.setZip(organizationModel.getZip());
        organization.setCountry(organizationModel.getCountry());
        organization.setPhone(organizationModel.getPhone());
        organization.setEmail(organizationModel.getEmail());
        organization.setUuid(organizationModel.getUuid());
        organization.setUse(organizationModel.isUse());
        return organization;
    }

    private List<DropDownItem> mergeDropDownItemLists(List<DropDownItem> defaults, List<DropDownItem> overrides) {
        Map<String, DropDownItem> merged = new HashMap<>(0);
        defaults.forEach(i -> merged.put(i.getId(), i));
        overrides.forEach(i -> merged.put(i.getId(), i));

        return merged.values().stream()
            .sorted((p1, p2) -> p1.getValue().compareToIgnoreCase(p2.getValue()))
            .collect(Collectors.toList());
    }

    private List<DropDownItem> readOrganizations(String dir) {
        Path workDir = Paths.get(serviceProperties.getWorkDir());
        Path dataDir = workDir.resolve(dir);
        Path peopleFile = dataDir.resolve("organizations.xml");
        if (!Files.isRegularFile(peopleFile)) {
            throw new IllegalStateException("Unable to read " + peopleFile);
        }
        OrganizationData organizationData;
        try (Reader reader = Files.newBufferedReader(peopleFile, StandardCharsets.UTF_8)) {
            organizationData = (OrganizationData) JAXBContext.newInstance(OrganizationData.class)
                .createUnmarshaller().unmarshal(reader);
        } catch (IOException | JAXBException e) {
            throw new IllegalStateException("Unable to parse " + peopleFile, e);
        }
        List<DropDownItem> dropDowns = new ArrayList<>(organizationData.getOrganizations().getOrganizations().size() + 1);
        organizationData.getOrganizations().getOrganizations().stream()
            .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
            .map(organization -> new DropDownItem(organization.getUuid(), organization.getName(), organization))
            .forEach(dropDowns::add);
        return dropDowns;
    }
}
