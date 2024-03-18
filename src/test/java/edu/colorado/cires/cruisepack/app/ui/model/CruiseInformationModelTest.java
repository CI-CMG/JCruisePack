package edu.colorado.cires.cruisepack.app.ui.model;

import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.mkDir;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseData;
import edu.colorado.cires.cruisepack.app.ui.controller.Events;
import edu.colorado.cires.cruisepack.app.ui.model.validation.DocumentsUnderMaxAllowed.DocumentsUnderMaxAllowedValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CruiseInformationModelTest {

    private CruiseInformationModel cruiseInformationModel;
    private List<PropertyChangeEvent> propertyChangeEvents;

    private static final String CRUISE_TITLE = "test-cruise-title";
    private static final String CRUISE_TITLE_ERROR = "test-cruise-title-error";
    private static final String CRUISE_PURPOSE = "test-cruise-purpose-error";
    private static final String CRUISE_PURPOSE_ERROR = "test-cruise-purpose-error";
    private static final String CRUISE_DESCRIPTION = "test-cruise-description";
    private static final String CRUISE_DESCRIPTION_ERROR = "test-cruise-description-error";
    private static final Path DOCUMENTS_PATH = Paths.get("target").resolve("test-documents-path");
    private static final String DOCUMENTS_PATH_ERROR = "test-documents-path-error";

    @BeforeEach
    public void beforeEach() {
        propertyChangeEvents = new ArrayList<>(0);
        initializeModel();
        cruiseInformationModel.addChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                propertyChangeEvents.add(evt);
            }
            
        });

        FileUtils.deleteQuietly(DOCUMENTS_PATH.toFile());
    }
    
    @AfterEach
    public void afterEach() {
        FileUtils.deleteQuietly(DOCUMENTS_PATH.toFile());
    }

    private void initializeModel() {
        cruiseInformationModel = new CruiseInformationModel();
        cruiseInformationModel.setCruiseTitle(CRUISE_TITLE);
        cruiseInformationModel.setCruiseTitleError(CRUISE_TITLE_ERROR);
        cruiseInformationModel.setCruisePurpose(CRUISE_PURPOSE);
        cruiseInformationModel.setCruisePurposeError(CRUISE_PURPOSE_ERROR);
        cruiseInformationModel.setCruiseDescription(CRUISE_DESCRIPTION);
        cruiseInformationModel.setCruiseDescriptionError(CRUISE_DESCRIPTION_ERROR);
        cruiseInformationModel.setDocumentsPath(DOCUMENTS_PATH);
        cruiseInformationModel.setDocumentsPathError(DOCUMENTS_PATH_ERROR);

        propertyChangeEvents.clear();
    }

    private void assertPropertChangeEvent(String eventName, Object oldValue, Object newValue, Supplier<Object> fieldGetter) {
        PropertyChangeEvent event = propertyChangeEvents.stream()
            .filter(evt -> evt.getPropertyName().equals(eventName))
            .findFirst()
            .orElseThrow(
                () -> new IllegalStateException("Unable to find event: " + eventName)
            );

        assertEquals(newValue, event.getNewValue());
        assertEquals(oldValue, event.getOldValue());
        assertEquals(newValue, fieldGetter.get());
    }

    @Test
    public void testRestoreDefaults() {
        cruiseInformationModel.restoreDefaults();

        assertPropertChangeEvent(Events.UPDATE_CRUISE_TITLE, CRUISE_TITLE, null, cruiseInformationModel::getCruiseTitle);
        assertPropertChangeEvent(Events.UPDATE_CRUISE_TITLE_ERROR, CRUISE_TITLE_ERROR, null, cruiseInformationModel::getCruiseTitleError);
        assertPropertChangeEvent(Events.UPDATE_CRUISE_PURPOSE, CRUISE_PURPOSE, null, cruiseInformationModel::getCruisePurpose);
        assertPropertChangeEvent(Events.UPDATE_CRUISE_PURPOSE_ERROR, CRUISE_PURPOSE_ERROR, null, cruiseInformationModel::getCruisePurposeError);
        assertPropertChangeEvent(Events.UPDATE_CRUISE_DESCRIPTION, CRUISE_DESCRIPTION, null, cruiseInformationModel::getCruiseDescription);
        assertPropertChangeEvent(Events.UPDATE_CRUISE_DESCRIPTION_ERROR, CRUISE_DESCRIPTION_ERROR, null, cruiseInformationModel::getCruiseDescriptionError);
        assertPropertChangeEvent(Events.UPDATE_DOCS_DIRECTORY, DOCUMENTS_PATH, null, cruiseInformationModel::getDocumentsPath);
        assertPropertChangeEvent(Events.UPDATE_DOCS_DIRECTORY_ERROR, DOCUMENTS_PATH_ERROR, null, cruiseInformationModel::getDocumentsPathError);
    }

    @Test
    public void testUpdateFormState() {
        String newCruiseTitle = "NEW-CRUISE-TITLE";
        String newCruisePurpose = "NEW-CRUISE-PURPOSE";
        String newCruiseDescription = "NEW-CRUISE-DESCRIPTION";
        Path newDocumentsPath = Paths.get("NEW-DOCUMENTS-PATH");

        cruiseInformationModel.updateFormState(CruiseData.builder()
            .withCruiseTitle(newCruiseTitle)
            .withCruisePurpose(newCruisePurpose)
            .withCruiseDescription(newCruiseDescription)
            .withDocumentsPath(newDocumentsPath.toString())
        .build());

        assertPropertChangeEvent(Events.UPDATE_CRUISE_TITLE, CRUISE_TITLE, newCruiseTitle, cruiseInformationModel::getCruiseTitle);
        assertPropertChangeEvent(Events.UPDATE_CRUISE_TITLE_ERROR, CRUISE_TITLE_ERROR, null, cruiseInformationModel::getCruiseTitleError);
        assertPropertChangeEvent(Events.UPDATE_CRUISE_PURPOSE, CRUISE_PURPOSE, newCruisePurpose, cruiseInformationModel::getCruisePurpose);
        assertPropertChangeEvent(Events.UPDATE_CRUISE_PURPOSE_ERROR, CRUISE_PURPOSE_ERROR, null, cruiseInformationModel::getCruisePurposeError);
        assertPropertChangeEvent(Events.UPDATE_CRUISE_DESCRIPTION, CRUISE_DESCRIPTION, newCruiseDescription, cruiseInformationModel::getCruiseDescription);
        assertPropertChangeEvent(Events.UPDATE_CRUISE_DESCRIPTION_ERROR, CRUISE_DESCRIPTION_ERROR, null, cruiseInformationModel::getCruiseDescriptionError);
        assertPropertChangeEvent(Events.UPDATE_DOCS_DIRECTORY, DOCUMENTS_PATH, newDocumentsPath, cruiseInformationModel::getDocumentsPath);
        assertPropertChangeEvent(Events.UPDATE_DOCS_DIRECTORY_ERROR, DOCUMENTS_PATH_ERROR, null, cruiseInformationModel::getDocumentsPathError);
    }

    @Test
    public void testSetCruiseTitle() {
        String newCruiseTitle = "new-cruise-title";
        cruiseInformationModel.setCruiseTitle(newCruiseTitle);

        assertPropertChangeEvent(Events.UPDATE_CRUISE_TITLE, CRUISE_TITLE, newCruiseTitle, cruiseInformationModel::getCruiseTitle);

        propertyChangeEvents.clear();

        cruiseInformationModel.setCruiseTitle(newCruiseTitle);
        assertThrows(IllegalStateException.class, () -> assertPropertChangeEvent(Events.UPDATE_CRUISE_TITLE, CRUISE_TITLE, newCruiseTitle, cruiseInformationModel::getCruiseTitle));
    }

    @Test void testSetCruiseTitleError() {
        String newCruiseTitleError = "new-cruise-title-error";
        cruiseInformationModel.setCruiseTitleError(newCruiseTitleError);

        assertPropertChangeEvent(Events.UPDATE_CRUISE_TITLE_ERROR, CRUISE_TITLE_ERROR, newCruiseTitleError, cruiseInformationModel::getCruiseTitleError);

        propertyChangeEvents.clear();

        cruiseInformationModel.setCruiseTitleError(newCruiseTitleError);
        assertThrows(IllegalStateException.class, () -> assertPropertChangeEvent(Events.UPDATE_CRUISE_TITLE_ERROR, CRUISE_TITLE_ERROR, newCruiseTitleError, cruiseInformationModel::getCruiseTitleError));
    }

    @Test void testSetCruisePurpose() {
        String newCruisePurpose = "new-cruise-purpose";
        cruiseInformationModel.setCruisePurpose(newCruisePurpose);

        assertPropertChangeEvent(Events.UPDATE_CRUISE_PURPOSE, CRUISE_PURPOSE, newCruisePurpose, cruiseInformationModel::getCruisePurpose);

        propertyChangeEvents.clear();

        cruiseInformationModel.setCruisePurpose(newCruisePurpose);
        assertThrows(IllegalStateException.class, () -> assertPropertChangeEvent(Events.UPDATE_CRUISE_PURPOSE, CRUISE_PURPOSE, newCruisePurpose, cruiseInformationModel::getCruisePurpose));
    }

    @Test
    public void testSetCruisePurposeError() {
        String newCruisePurposeError = "new-cruise-purpose-error";
        cruiseInformationModel.setCruisePurposeError(newCruisePurposeError);

        assertPropertChangeEvent(Events.UPDATE_CRUISE_PURPOSE_ERROR, CRUISE_PURPOSE_ERROR, newCruisePurposeError, cruiseInformationModel::getCruisePurposeError);

        propertyChangeEvents.clear();

        cruiseInformationModel.setCruisePurposeError(newCruisePurposeError);
        assertThrows(IllegalStateException.class, () -> assertPropertChangeEvent(Events.UPDATE_CRUISE_PURPOSE_ERROR, CRUISE_PURPOSE_ERROR, newCruisePurposeError, cruiseInformationModel::getCruisePurposeError));
    }
    
    @Test
    public void testSetCruiseDescription() {
        String newCruiseDescription = "new-cruise-description";
        cruiseInformationModel.setCruiseDescription(newCruiseDescription);
        
        assertPropertChangeEvent(Events.UPDATE_CRUISE_DESCRIPTION, CRUISE_DESCRIPTION, newCruiseDescription, cruiseInformationModel::getCruiseDescription);
        
        propertyChangeEvents.clear();
        
        cruiseInformationModel.setCruiseDescription(newCruiseDescription);
        assertThrows(IllegalStateException.class, () -> assertPropertChangeEvent(Events.UPDATE_CRUISE_DESCRIPTION, CRUISE_DESCRIPTION, newCruiseDescription, cruiseInformationModel::getCruiseDescription));
    }
    
    @Test
    public void testSetCruiseDescriptionError() {
        String newCruiseDescriptionError = "new-cruise-description-error";
        cruiseInformationModel.setCruiseDescriptionError(newCruiseDescriptionError);
        
        assertPropertChangeEvent(Events.UPDATE_CRUISE_DESCRIPTION_ERROR, CRUISE_DESCRIPTION_ERROR, newCruiseDescriptionError, cruiseInformationModel::getCruiseDescriptionError);
        
        propertyChangeEvents.clear();
        
        cruiseInformationModel.setCruiseDescriptionError(newCruiseDescriptionError);
        assertThrows(IllegalStateException.class, () -> assertPropertChangeEvent(Events.UPDATE_CRUISE_DESCRIPTION_ERROR, CRUISE_DESCRIPTION_ERROR, newCruiseDescriptionError, cruiseInformationModel::getCruiseDescriptionError));
    }
    
    @Test
    public void testSetDocumentsPath() {
        Path newDocumentsPath = Paths.get("new-documents-path");
        cruiseInformationModel.setDocumentsPath(newDocumentsPath);
        
        assertPropertChangeEvent(Events.UPDATE_DOCS_DIRECTORY, DOCUMENTS_PATH, newDocumentsPath, cruiseInformationModel::getDocumentsPath);
        
        propertyChangeEvents.clear();
        
        cruiseInformationModel.setDocumentsPath(newDocumentsPath);
        assertThrows(IllegalStateException.class, () -> assertPropertChangeEvent(Events.UPDATE_DOCS_DIRECTORY, DOCUMENTS_PATH, newDocumentsPath, cruiseInformationModel::getDocumentsPath));
    }
    
    @Test
    public void testSetDocumentsPathError() {
        String newDocumentsPathError = "new-documents-path-error";
        cruiseInformationModel.setDocumentsPathError(newDocumentsPathError);
        
        assertPropertChangeEvent(Events.UPDATE_DOCS_DIRECTORY_ERROR, DOCUMENTS_PATH_ERROR, newDocumentsPathError, cruiseInformationModel::getDocumentsPathError);
        
        propertyChangeEvents.clear();
        
        cruiseInformationModel.setDocumentsPathError(newDocumentsPathError);
        assertThrows(IllegalStateException.class, () -> assertPropertChangeEvent(Events.UPDATE_DOCS_DIRECTORY_ERROR, DOCUMENTS_PATH_ERROR, newDocumentsPathError, cruiseInformationModel::getDocumentsPathError));
    }
    
    @Test
    public void testMaxDocumentsEqualThreshold() {
        createDocumentsDir(DOCUMENTS_PATH, 10);

        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setDocumentFilesErrorThreshold(10);

        assertTrue(new DocumentsUnderMaxAllowedValidator(serviceProperties).isValid(
            DOCUMENTS_PATH, mock(ConstraintValidatorContext.class)
        ));
    }
    
    @Test
    public void testMaxDocumentsViolation() {
        createDocumentsDir(DOCUMENTS_PATH, 11);
        
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setDocumentFilesErrorThreshold(10);
        assertFalse(new DocumentsUnderMaxAllowedValidator(serviceProperties).isValid(
            DOCUMENTS_PATH, mock(ConstraintValidatorContext.class)
        ));
    }
    
    private void createDocumentsDir(Path path, int nFiles) {
        mkDir(path);
        
        for (int i = 0; i < nFiles; i++) {
            try (FileWriter fileWriter = new FileWriter(path.resolve(String.format(
                "test-%s.txt", i
            )).toFile(), StandardCharsets.UTF_8)) {
                fileWriter.write(String.format(
                    "test-file-context\t\t%s", i
                ));
            } catch (IOException e) {
              throw new IllegalStateException("Unable to write file", e);
            }
        }
    }
    
}
