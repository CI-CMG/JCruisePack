package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.model.DatasetsModel;
import edu.colorado.cires.cruisepack.app.ui.model.FooterControlModel;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path.Node;
import jakarta.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackagingValidationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PackagingValidationService.class);

  private final Validator validator;
  private final PackageModel packageModel;
  private final OmicsModel omicsModel;
  private final CruiseInformationModel cruiseInformationModel;
  private final DatasetsModel datasetsModel;
  private final PeopleModel peopleModel;
  private final InstrumentDatastore instrumentDatastore;
  private final PersonDatastore personDatastore;
  private final FooterControlModel footerControlModel;
  private final ServiceProperties serviceProperties;

  @Autowired
  public PackagingValidationService(
      Validator validator,
      PackageModel packageModel,
      OmicsModel omicsModel,
      CruiseInformationModel cruiseInformationModel,
      DatasetsModel datasetsModel,
      InstrumentDatastore instrumentDatastore,
      PeopleModel peopleModel,
      PersonDatastore personDatastore, FooterControlModel footerControlModel, ServiceProperties serviceProperties
  ) {
    this.validator = validator;
    this.packageModel = packageModel;
    this.omicsModel = omicsModel;
    this.cruiseInformationModel = cruiseInformationModel;
    this.datasetsModel = datasetsModel;
    this.instrumentDatastore = instrumentDatastore;
    this.peopleModel = peopleModel;
    this.personDatastore = personDatastore;
    this.footerControlModel = footerControlModel;
    this.serviceProperties = serviceProperties;
  }

  public Optional<PackJob> validate() {
    Set<ConstraintViolation<PackageModel>> packageViolations = validator.validate(packageModel);
    Set<ConstraintViolation<CruiseInformationModel>> cruiseInformationViolations = validator.validate(cruiseInformationModel);
    Set<ConstraintViolation<OmicsModel>> omicsViolations = validator.validate(omicsModel);
    Set<ConstraintViolation<DatasetsModel>> datasetsViolations = validator.validate(datasetsModel);
    Set<ConstraintViolation<PeopleModel>> peopleViolations = validator.validate(peopleModel);

    updatePackageErrors(packageViolations);
    updateCruiseInformationErrors(cruiseInformationViolations);
    updateOmicsErrors(omicsViolations);
    updateDatasetsErrors(datasetsViolations);
    updatePeopleErrors(peopleViolations);
    
    if (
        packageViolations.isEmpty() &&
            cruiseInformationViolations.isEmpty() &&
            omicsViolations.isEmpty() &&
            datasetsViolations.isEmpty() &&
            peopleViolations.isEmpty()) {
      footerControlModel.setJobErrors(null);
      
      PackJob packJob = PackJobUtils.create(packageModel, peopleModel, omicsModel, cruiseInformationModel, datasetsModel, instrumentDatastore, personDatastore);

      Set<ConstraintViolation<PackJob>> constraintViolations = validator.validate(packJob);
      
      String errorMessages = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
      footerControlModel.setJobErrors(errorMessages);
      
      if (errorMessages.isEmpty()) {
        
        List<String> warningMessages = checkWarnings(packJob);
        warningMessages.removeAll(footerControlModel.getIgnoredWarningMessages());
        footerControlModel.clearIgnoreWarningMessage();
        
        if (warningMessages.isEmpty()) {
          return Optional.of(packJob);
        } else {
          footerControlModel.setWarningMessages(warningMessages);
        }
        
      }
      
      return Optional.empty();
    }
    return Optional.empty();
  }
  
  private List<String> checkWarnings(PackJob packJob) {
    List<String> warningMessages = new ArrayList<>(0);
    
    java.nio.file.Path docsPath = packJob.getDocumentsPath();
    if (docsPath != null) {
      try (Stream<java.nio.file.Path> paths = Files.walk(docsPath)) {
        long fileCount = paths
            .filter(p -> !Files.isDirectory(p))
            .filter(CruisePackFileUtils::filterHidden)
            .count();
        if (fileCount > serviceProperties.getDocumentFilesWarningThreshold()) {
          String warningMessage = String.format(
              "More than %s files located in documents directory: %s",
              serviceProperties.getDocumentFilesWarningThreshold(), docsPath
          );
          warningMessages.add(warningMessage);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    
    return warningMessages;
  }

  private void updatePeopleErrors(Set<ConstraintViolation<PeopleModel>> peopleViolations) {
    LOGGER.warn("people {}", peopleViolations);

    String scientistError = null;
    String[] scientistErrors = new String[peopleModel.getScientists().size()];
    Arrays.fill(scientistErrors, null);
    String sourceOrganizationError = null;
    String[] sourceOrganizationErrors = new String[peopleModel.getSourceOrganizations().size()];
    Arrays.fill(sourceOrganizationErrors, null);
    String fundingOrganizationError = null;
    String[] fundingOrganizationErrors = new String[peopleModel.getFundingOrganizations().size()];
    String metadataAuthorError = null;

    for (ConstraintViolation<PeopleModel> constraintViolation : peopleViolations) {
      String propertyPath = constraintViolation.getPropertyPath().toString();
      String message = constraintViolation.getMessage();
      if (propertyPath.startsWith("scientists")) {
        if (propertyPath.equals("scientists")) {
          scientistError = message;
        } else {
          for (Node node : constraintViolation.getPropertyPath()) {
            Integer index = node.getIndex();
            if (index != null) {
              scientistErrors[index] = message;
            }
          }
        }
      } else if (propertyPath.startsWith("sourceOrganizations")) {
        if (propertyPath.equals("sourceOrganizations")) {
          sourceOrganizationError = message;
        } else {
          for (Node node : constraintViolation.getPropertyPath()) {
            Integer index = node.getIndex();
            if (index != null) {
              sourceOrganizationErrors[index] = message;
            }
          }
        }
      } else if (propertyPath.startsWith("fundingOrganizations")) {
        if (propertyPath.equals("fundingOrganizations")) {
          fundingOrganizationError = message;
        } else {
          for (Node node : constraintViolation.getPropertyPath()) {
            Integer index = node.getIndex();
            if (index != null) {
              fundingOrganizationErrors[index] = message;
            }
          }
        }
      } else if (propertyPath.startsWith("metadataAuthor")) {
        metadataAuthorError = message;
      }
    }

    peopleModel.setScientistError(scientistError);
    peopleModel.setScientistErrors(Arrays.stream(scientistErrors).collect(Collectors.toList()));
    peopleModel.setSourceOrganizationError(sourceOrganizationError);
    peopleModel.setSourceOrganizationErrors(Arrays.stream(sourceOrganizationErrors).collect(Collectors.toList()));
    peopleModel.setFundingOrganizationError(fundingOrganizationError);
    peopleModel.setFundingOrganizationErrors(Arrays.stream(fundingOrganizationErrors).collect(Collectors.toList()));
    peopleModel.setMetadataAuthorError(metadataAuthorError);
  }

  private void updateOmicsErrors(Set<ConstraintViolation<OmicsModel>> omicsViolations) {
    LOGGER.warn("omics {}", omicsViolations);

    String additionalSamplingInformationError = null;
    String contactError = null;
    String sampleTrackingSheetError = null;
    String bioProjectAccessionError = null;
    String samplingTypesError = null;
    String expectedAnalysesError = null;

    for (ConstraintViolation<OmicsModel> violation : omicsViolations) {
      String propertyPath = violation.getPropertyPath().toString();
      String message = violation.getMessage();
      if (propertyPath.startsWith("additionalSamplingInformation")) {
        additionalSamplingInformationError = message;
      } else if (propertyPath.startsWith("contact")) {
        contactError = message;
      } else if (propertyPath.startsWith("sampleTrackingSheet")) {
        sampleTrackingSheetError = message;
      } else if (propertyPath.startsWith("bioProjectAccession")) {
        bioProjectAccessionError = message;
      } else if (propertyPath.startsWith("expectedAnalyses")) {
        expectedAnalysesError = message;
      } else if (propertyPath.startsWith("samplingTypes")) {
        samplingTypesError = message;
      }
    }

    omicsModel.setAdditionalSamplingInformationError(additionalSamplingInformationError);
    omicsModel.setContactError(contactError);
    omicsModel.setSampleTrackingSheetError(sampleTrackingSheetError);
    omicsModel.setBioProjectAcessionError(bioProjectAccessionError);
    omicsModel.setExpectedAnalysesError(expectedAnalysesError);
    omicsModel.setSamplingTypesError(samplingTypesError);
  }

  private void updateCruiseInformationErrors(Set<ConstraintViolation<CruiseInformationModel>> cruiseInformationViolations) {
    LOGGER.warn("cruise information {}", cruiseInformationViolations);
    String cruiseTitleError = null;
    String cruisePurposeError = null;
    String cruiseDescriptionError = null;

    for (ConstraintViolation<CruiseInformationModel> violation : cruiseInformationViolations) {
      String propertyPath = violation.getPropertyPath().toString();
      String message = violation.getMessage();
      if (propertyPath.startsWith("cruiseTitle")) {
        cruiseTitleError = message;
      } else if (propertyPath.startsWith("cruisePurpose")) {
        cruisePurposeError = message;
      } else if (propertyPath.startsWith("cruiseDescription")) {
        cruiseDescriptionError = message;
      }
    }

    cruiseInformationModel.setCruiseTitleError(cruiseTitleError);
    cruiseInformationModel.setCruisePurposeError(cruisePurposeError);
    cruiseInformationModel.setCruiseDescriptionError(cruiseDescriptionError);
  }

  private void updatePackageErrors(Set<ConstraintViolation<PackageModel>> packageViolations) {
    LOGGER.warn("package {}", packageViolations);

    String cruiseIDError = null;
    String segmentError = null;
    String packageDirectoryError = null;
    String shipError = null;
    String arrivalPortError = null;
    String departurePortError = null;
    String seaError = null;
    String arrivalDateError = null;
    String departureDateError = null;
    String releaseDateError = null;
    String projectsError = null;
    String[] projectErrors = new String[packageModel.getProjects().size()];
    Arrays.fill(projectErrors, null);

    for (ConstraintViolation<PackageModel> violation : packageViolations) {
      String propertyPath = violation.getPropertyPath().toString();
      String message = violation.getMessage();

      if (propertyPath.startsWith("cruiseId")) {
        cruiseIDError = message;
      } else if (propertyPath.startsWith("segment")) {
        segmentError = message;
      } else if (propertyPath.startsWith("packageDirectory")) {
        packageDirectoryError = message;
      } else if (propertyPath.startsWith("ship")) {
        shipError = message;
      } else if (propertyPath.startsWith("arrivalPort")) {
        arrivalPortError = message;
      } else if (propertyPath.startsWith("departurePort")) {
        departurePortError = message;
      } else if (propertyPath.startsWith("sea")) {
        seaError = message;
      } else if (propertyPath.startsWith("arrivalDate")) {
        arrivalDateError = message;
      } else if (propertyPath.startsWith("departureDate")) {
        departureDateError = message;
      } else if (propertyPath.startsWith("releaseDate")) {
        releaseDateError = message;
      } else if (propertyPath.startsWith("projects")) {
        if (propertyPath.equals("projects")) {
          projectsError = message;
        } else {
          for (Node node : violation.getPropertyPath()) {
            Integer index = node.getIndex();
            if (index != null) {
              projectErrors[index] = message;
            }
          }
        }
      }
    }

    packageModel.setCruiseIdError(cruiseIDError);
    packageModel.setSegmentError(segmentError);
    packageModel.setPackageDirectoryError(packageDirectoryError);
    packageModel.setShipError(shipError);
    packageModel.setArrivalPortError(arrivalPortError);
    packageModel.setDeparturePortError(departurePortError);
    packageModel.setSeaError(seaError);
    packageModel.setArrivalDateError(arrivalDateError);
    packageModel.setDepartureDateError(departureDateError);
    packageModel.setReleaseDateError(releaseDateError);
    packageModel.setProjectsError(projectsError);
    packageModel.setProjectErrors(Arrays.stream(projectErrors).collect(Collectors.toList()));
  }

  private void updateDatasetsErrors(Set<ConstraintViolation<DatasetsModel>> datasetsViolations) {
    LOGGER.warn("datasets {}", datasetsViolations);
    String datasetsError = null;
    String documentsPathError = null;

    datasetsModel.getDatasets().forEach(BaseDatasetInstrumentModel::clearErrors);

    for (ConstraintViolation<DatasetsModel> violation : datasetsViolations) {
      String propertyPath = violation.getPropertyPath().toString();
      String message = violation.getMessage();

      if (propertyPath.equals("datasets")) {
        datasetsError = message;
      } else if (propertyPath.equals("documentsPath")) {
        documentsPathError = message;
      } else {
        for (Node node : violation.getPropertyPath()) {
          Integer index = node.getIndex();
          if (index != null) {
            datasetsModel.getDatasets().get(index).setModelErrors(propertyPath, message);
          }
        }
      }
    }
    
    datasetsModel.setDatasetsError(datasetsError);
    datasetsModel.setDocumentsPathError(documentsPathError);
  }
}
