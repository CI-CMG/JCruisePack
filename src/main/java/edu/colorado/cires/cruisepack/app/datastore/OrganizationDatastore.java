package edu.colorado.cires.cruisepack.app.datastore;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import edu.colorado.cires.cruisepack.app.ui.model.OrganizationModel;
import edu.colorado.cires.cruisepack.app.ui.model.PropertyChangeModel;
import edu.colorado.cires.cruisepack.app.ui.view.ReactiveViewRegistry;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.organization.Organization;
import edu.colorado.cires.cruisepack.xml.organization.OrganizationData;
import edu.colorado.cires.cruisepack.xml.organization.OrganizationList;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizationDatastore extends PropertyChangeModel implements PropertyChangeListener {
    public static final DropDownItem UNSELECTED_ORGANIZATION = new DropDownItem("", "Select Organization");

    private final ServiceProperties serviceProperties;
    private List<DropDownItem> organizationDropDowns;
    private final ReactiveViewRegistry reactiveViewRegistry;
    private  List<Organization> organizations;

    @Autowired
    public OrganizationDatastore(ServiceProperties serviceProperties, ReactiveViewRegistry reactiveViewRegistry) {
        this.serviceProperties = serviceProperties;
        this.reactiveViewRegistry = reactiveViewRegistry;
    }

    @PostConstruct
    public void init() {
        addChangeListener(this);
        load();
    }

    
    private void load() {
        organizations = mergeOrganizations(readOrganizations("data"), readOrganizations("local-data"));
        List<DropDownItem> items = organizations.stream()
            .map(o -> new DropDownItem(o.getUuid(), o.getName()))
            .collect(Collectors.toList());
        items.add(0, UNSELECTED_ORGANIZATION);

        setOrganizationDropDowns(items);
    }

    private void setOrganizationDropDowns(List<DropDownItem> items) {
        setIfChanged(Events.UPDATE_ORGANIZATION_DATA_STORE, items, () -> new ArrayList<DropDownItem>(), (i) -> this.organizationDropDowns = i);
    }

    public List<DropDownItem> getAllOrganizationDropDowns() {
        return organizationDropDowns;
    }

    public List<DropDownItem> getEnabledOrganizationDropDowns() {
        return organizationDropDowns.stream()
        .filter(dd -> 
            getByUUID(dd.getId())
                .map(Organization::isUse)
                .orElse(dd.equals(UNSELECTED_ORGANIZATION))
        )
        .collect(Collectors.toList());
    }

    public void save(Organization organization) {
        OrganizationData newOrganizationData = new OrganizationData();
        OrganizationList newOrganizationList = new OrganizationList();
        List<Organization> listWithNewOrganization = newOrganizationList.getOrganizations();
        listWithNewOrganization.add(organization);
        newOrganizationData.setOrganizations(newOrganizationList);
        List<Organization> mergedOrganizations = mergeOrganizations(
            readOrganizations("local-data"),
             Optional.of(newOrganizationData)
        );

        OrganizationData organizationData = new OrganizationData();
        organizationData.setDataVersion("1.0");
        OrganizationList organizationList = new OrganizationList();
        List<Organization> organizations = organizationList.getOrganizations();
        organizations.addAll(
            mergedOrganizations
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

        load();
    }

    public void save(OrganizationModel organizationModel) {
        save(organizationFromModel(organizationModel));
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

    private List<Organization> mergeOrganizations(Optional<OrganizationData> defaults, Optional<OrganizationData> overrides) {
        Map<String, Organization> merged = new HashMap<>(0);
        defaults.map(od -> od.getOrganizations().getOrganizations()).ifPresent(o1 -> o1.forEach(o -> merged.put(o.getUuid(), o)));
        overrides.map(od -> od.getOrganizations().getOrganizations()).ifPresent(o1 -> o1.forEach(o -> merged.put(o.getUuid(), o)));

        return merged.values().stream()
            .sorted((o1, o2) -> o1.getUuid().compareToIgnoreCase(o2.getUuid()))
            .collect(Collectors.toList());
    }

    private Optional<OrganizationData> readOrganizations(String dir) {
        Path workDir = Paths.get(serviceProperties.getWorkDir());
        Path dataDir = workDir.resolve(dir);
        Path peopleFile = dataDir.resolve("organizations.xml");
        if (!Files.isRegularFile(peopleFile)) {
            return Optional.empty();
        }
        OrganizationData organizationData;
        try (Reader reader = Files.newBufferedReader(peopleFile, StandardCharsets.UTF_8)) {
            organizationData = (OrganizationData) JAXBContext.newInstance(OrganizationData.class)
                .createUnmarshaller().unmarshal(reader);
        } catch (IOException | JAXBException e) {
            throw new IllegalStateException("Unable to parse " + peopleFile, e);
        }
        return Optional.of(organizationData);
    }
    
    public Optional<Organization> getByUUID(String uuid) {
        return organizations.stream()
            .filter(o -> o.getUuid().equals(uuid))
            .findFirst();
    }
    
    public Optional<Organization> findByName(String name) {
        return organizations.stream()
            .filter(o -> o.getName().equals(name))
            .findFirst();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (ReactiveView view : reactiveViewRegistry.getViews()) {
            view.onChange(evt);
        }
    }
}
