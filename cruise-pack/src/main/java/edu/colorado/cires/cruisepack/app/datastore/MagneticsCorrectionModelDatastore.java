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
import edu.colorado.cires.cruisepack.xml.magneticsCorrectionModel.MagneticsCorrectionModelData;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

@Component
public class MagneticsCorrectionModelDatastore {

    public static final DropDownItem UNSELECTED_CORRECTION_MODEL = new DropDownItem("", "Select Correction Model");

    private final ServiceProperties serviceProperties;
    private List<DropDownItem> correctionModelDropDowns;

    @Autowired
    public MagneticsCorrectionModelDatastore(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    @PostConstruct
    public void init() {
        Path workDir = Paths.get(serviceProperties.getWorkDir());
        Path dataDir = workDir.resolve("data");
        Path correctionModelsFile = dataDir.resolve("magneticsCorrectionModels.xml");

        if (!Files.isRegularFile(correctionModelsFile)) {
            throw new IllegalStateException("Unable to read " + correctionModelsFile);
        }

        MagneticsCorrectionModelData data;
        try (Reader reader = Files.newBufferedReader(correctionModelsFile, StandardCharsets.UTF_8)) {
            data = (MagneticsCorrectionModelData) JAXBContext.newInstance(MagneticsCorrectionModelData.class).createUnmarshaller().unmarshal(reader);
        } catch (JAXBException | IOException e) {
            throw new IllegalStateException("Unable to parse " + correctionModelsFile, e);
        }

        correctionModelDropDowns = new ArrayList<>(data.getMagneticsCorrectionModels().getMagneticsCorrectionModels().size() + 1);
        correctionModelDropDowns.add(UNSELECTED_CORRECTION_MODEL);
        data.getMagneticsCorrectionModels().getMagneticsCorrectionModels().stream()
            .sorted((m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()))
            .map(m -> new DropDownItem(m.getUuid(), m.getName()))
            .forEach(correctionModelDropDowns::add);
    }

    public List<DropDownItem> getCorrectionModelDropDowns() {
        return correctionModelDropDowns;
    }
    
}
