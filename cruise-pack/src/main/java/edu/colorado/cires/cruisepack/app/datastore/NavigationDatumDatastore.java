package edu.colorado.cires.cruisepack.app.datastore;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.navigationDatum.NavigationDatumData;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

@Component
public class NavigationDatumDatastore {

    public static final DropDownItem UNSELECTED_NAVIGATION_DATUM = new DropDownItem("", "Select Navigation Datum");

    private final ServiceProperties serviceProperties;
    private List<DropDownItem> navigationDatumDropDowns;

    @Autowired
    public NavigationDatumDatastore(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    @PostConstruct
    public void init() {
        Path workDir = Paths.get(serviceProperties.getWorkDir());
        Path dataDir = workDir.resolve("data");
        Path navigationDatumsFile = dataDir.resolve("navigationDatums.xml");
        if (!Files.isRegularFile(navigationDatumsFile)) {
            throw new IllegalStateException("Unable to read " + navigationDatumsFile);
        }

        NavigationDatumData data;
        try (Reader reader = Files.newBufferedReader(navigationDatumsFile, StandardCharsets.UTF_8)) {
            data = (NavigationDatumData) JAXBContext.newInstance(NavigationDatumData.class).createUnmarshaller().unmarshal(reader);
        } catch (JAXBException | IOException e) {
            throw new IllegalStateException("Unable to parse " + navigationDatumsFile, e);
        }

        navigationDatumDropDowns = new ArrayList<>(data.getNavigationDatums().getNavigationData().size() + 1);
        navigationDatumDropDowns.add(UNSELECTED_NAVIGATION_DATUM);
        data.getNavigationDatums().getNavigationData().stream()
            .sorted((d1, d2) -> d1.getName().compareToIgnoreCase(d2.getName()))
            .map(d -> new DropDownItem(d.getUuid(), d.getName()))
            .forEach(navigationDatumDropDowns::add);
    }

    public List<DropDownItem> getNavigationDatumDropDowns() {
        return navigationDatumDropDowns;
    }
    
}
