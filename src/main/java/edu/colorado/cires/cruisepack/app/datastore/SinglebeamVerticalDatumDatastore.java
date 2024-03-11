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
import edu.colorado.cires.cruisepack.xml.singlebeamVerticalDatum.SinglebeamVerticalDatumData;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

@Component
public class SinglebeamVerticalDatumDatastore {

    public static final DropDownItem UNSELECTED_VERTICAL_DATUM = new DropDownItem("", "Select Vertical Datum");

    private final ServiceProperties serviceProperties;

    private List<DropDownItem> verticalDatumDropDowns;

    @Autowired
    public SinglebeamVerticalDatumDatastore(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    @PostConstruct
    public void init() {
        Path workDir = Paths.get(serviceProperties.getWorkDir());
        Path dataDir = workDir.resolve("data");
        Path verticalDatumsFile = dataDir.resolve("singlebeamVerticalDatums.xml");
        if (!Files.isRegularFile(verticalDatumsFile)) {
            throw new IllegalStateException("Unable to read " + verticalDatumsFile);
        }

        SinglebeamVerticalDatumData data;
        try (Reader reader = Files.newBufferedReader(verticalDatumsFile, StandardCharsets.UTF_8)) {
            data = (SinglebeamVerticalDatumData) JAXBContext.newInstance(SinglebeamVerticalDatumData.class).createUnmarshaller().unmarshal(reader);
        } catch (JAXBException | IOException e) {
            throw new IllegalStateException("Unable to parse " + verticalDatumsFile, e);
        }

        verticalDatumDropDowns = new ArrayList<>(data.getSinglebeamVerticalDatums().getSinglebeamVerticalData().size() + 1);
        verticalDatumDropDowns.add(UNSELECTED_VERTICAL_DATUM);
        data.getSinglebeamVerticalDatums().getSinglebeamVerticalData().stream()
            .sorted((d1, d2) -> d1.getName().compareToIgnoreCase(d2.getName()))
            .map(d -> new DropDownItem(d.getUuid(), d.getName()))
            .forEach(verticalDatumDropDowns::add);
    }

    public List<DropDownItem> getVerticalDatumDropDowns() {
        return verticalDatumDropDowns;
    }
    
}
