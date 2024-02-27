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
import edu.colorado.cires.cruisepack.xml.waterColumnCalibrationState.WaterColumnCalibrationStateData;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

@Component
public class WaterColumnCalibrationStateDatastore {

    public static final DropDownItem UNSELECTED_CALIBRATION_STATE = new DropDownItem("", "Select Calibration State");

    private final ServiceProperties serviceProperties;
    private List<DropDownItem> calibrationStateDropDowns;
    
    @Autowired
    public WaterColumnCalibrationStateDatastore(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    @PostConstruct
    public void init() {
        Path workDir = Paths.get(serviceProperties.getWorkDir());
        Path dataDir = workDir.resolve("data");
        Path calibrationStatesFile = dataDir.resolve("waterColumnCalibrationStates.xml");
        if (!Files.isRegularFile(calibrationStatesFile)) {
            throw new IllegalStateException("Unable to read " + calibrationStatesFile);
        }

        WaterColumnCalibrationStateData data;
        try (Reader reader = Files.newBufferedReader(calibrationStatesFile, StandardCharsets.UTF_8)) {
            data = (WaterColumnCalibrationStateData) JAXBContext.newInstance(WaterColumnCalibrationStateData.class).createUnmarshaller().unmarshal(reader);
        } catch (IOException | JAXBException e) {
            throw new IllegalStateException("Unable to parse " + calibrationStatesFile, e);
        }

        calibrationStateDropDowns = new ArrayList<>(data.getWaterColumnCalibrationStates().getWaterColumnCalibrationStates().size() + 1);
        calibrationStateDropDowns.add(UNSELECTED_CALIBRATION_STATE);
        data.getWaterColumnCalibrationStates().getWaterColumnCalibrationStates().stream()
            .sorted((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()))
            .map(c -> new DropDownItem(c.getUuid(), c.getName()))
            .forEach(calibrationStateDropDowns::add);
    }

    public List<DropDownItem> getCalibrationStateDropDowns() {
        return calibrationStateDropDowns;
    }
    
}
