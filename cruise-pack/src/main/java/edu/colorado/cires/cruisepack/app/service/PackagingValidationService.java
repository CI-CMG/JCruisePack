package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
import edu.colorado.cires.cruisepack.app.ui.controller.PackageController;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Optional;
import java.util.Set;
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
  private final PeopleModel peopleModel;
  private final PackageController packageController;
  private final FooterControlController footerControlController;

  @Autowired
  public PackagingValidationService(Validator validator, PackageModel packageModel, OmicsModel omicsModel,
      CruiseInformationModel cruiseInformationModel, PackageController packageController,
      FooterControlController footerControlController, PeopleModel peopleModel) {
    this.validator = validator;
    this.packageModel = packageModel;
    this.omicsModel = omicsModel;
    this.cruiseInformationModel = cruiseInformationModel;
    this.packageController = packageController;
    this.footerControlController = footerControlController;
    this.peopleModel = peopleModel;
  }

  public Optional<PackJob> validate() {
    Set<ConstraintViolation<PackageModel>> packageViolations = validator.validate(packageModel);
    Set<ConstraintViolation<CruiseInformationModel>> cruiseInformationViolations = validator.validate(cruiseInformationModel);
    Set<ConstraintViolation<OmicsModel>> omicsViolations = validator.validate(omicsModel);
    Set<ConstraintViolation<PeopleModel>> peopleViolations = validator.validate(peopleModel);

    updatePackageErrors(packageViolations);
    updateCruiseInformationErrors(cruiseInformationViolations);
    updateOmicsErrors(omicsViolations);
    updatePeopleErrors(peopleViolations);

    if (packageViolations.isEmpty() && cruiseInformationViolations.isEmpty() && omicsViolations.isEmpty() && peopleViolations.isEmpty()) {
      return Optional.of(createPackJob());
    }
    return Optional.empty();
  }

  private void updatePeopleErrors(Set<ConstraintViolation<PeopleModel>> peopleViolations) {
    LOGGER.warn("people {}", peopleViolations);

    String scientistErrors = null;
    String sourceOrganizationErrors = null;
    String fundingOrganizationErrors = null;
    String metadataAuthorErrors = null;

    for (ConstraintViolation<PeopleModel> constraintViolation : peopleViolations) {
      String propertyPath = constraintViolation.getPropertyPath().toString();
      String message = constraintViolation.getMessage();
        if (propertyPath.startsWith("scientists")) {
          scientistErrors = message;
        } else if (propertyPath.startsWith("sourceOrganizations")) {
          sourceOrganizationErrors = message;
        } else if (propertyPath.startsWith("fundingOrganizations")) {
          fundingOrganizationErrors = message;
        } else if (propertyPath.startsWith("metadataAuthor")) {
          metadataAuthorErrors = message;
        }
    }

    peopleModel.setScientistError(scientistErrors);
    peopleModel.setSourceOrganizationError(sourceOrganizationErrors);
    peopleModel.setFundingOrganizationError(fundingOrganizationErrors);
    peopleModel.setMetadataAuthorError(metadataAuthorErrors);
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
    String documentsPathError = null;
    
    for (ConstraintViolation<CruiseInformationModel> violation : cruiseInformationViolations) {
      String propertyPath = violation.getPropertyPath().toString();
      String message = violation.getMessage();
      if (propertyPath.startsWith("cruiseTitle")) {
        cruiseTitleError = message;
      } else if (propertyPath.startsWith("cruisePurpose")) {
        cruisePurposeError = message;
      } else if (propertyPath.startsWith("cruiseDescription")) {
        cruiseDescriptionError = message;
      } else if (propertyPath.startsWith("documentsPath")) {
        documentsPathError = message;
      }
    }

    cruiseInformationModel.setCruiseTitleError(cruiseTitleError);
    cruiseInformationModel.setCruisePurposeError(cruisePurposeError);
    cruiseInformationModel.setCruiseDescriptionError(cruiseDescriptionError);
    cruiseInformationModel.setDocumentsPathError(documentsPathError);
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
  }

  private PackJob createPackJob() {
    return PackJob.builder()
        .setCruiseId(packageModel.getCruiseId())
        .setSegment(packageModel.getSegment())
        .setSeaUuid(resolveDropDownItemUuid(packageModel.getSea()))
        .setArrivalPortUuid(resolveDropDownItemUuid(packageModel.getArrivalPort()))
        .setDeparturePortUuid(resolveDropDownItemUuid(packageModel.getDeparturePort()))
        .setShipUuid(resolveDropDownItemUuid(packageModel.getShip()))
        .setDepartureDate(packageModel.getDepartureDate())
        .setArrivalDate(packageModel.getArrivalDate())
        .setReleaseDate(packageModel.getReleaseDate())
        .setPackageDirectory(packageModel.getPackageDirectory())
        .setCruiseTitle(cruiseInformationModel.getCruiseTitle())
        .setCruisePurpose(cruiseInformationModel.getCruisePurpose())
        .setCruiseDescription(cruiseInformationModel.getCruiseDescription())
        .setDocumentsPath(cruiseInformationModel.getDocumentsPath())
        .setOmicsSamplingConducted(omicsModel.isSamplingConducted())
        .setOmicsContactUuid(resolveDropDownItemUuid(omicsModel.getContact()))
        .setOmicsSampleTrackingSheetPath(omicsModel.getSampleTrackingSheet())
        .setOmicsBioProjectAccession(omicsModel.getBioProjectAccession())
        //TODO
//        .setOmicsSamplingTypes(omicsModel.getSamplingTypes())
//        .setOmicsExpectedAnalyses(omicsModel.getExpectedAnalyses())
        .setOmicsAdditionalSamplingInformation(omicsModel.getAdditionalSamplingInformation())
        .setPackageId(resolvePackageId())
        .build();
  }

  private static String resolveDropDownItemUuid(DropDownItem ddi) {
    if (ddi == null || ddi.getId().isEmpty()) {
      return null;
    }
    return ddi.getId();
  }

  private String resolvePackageId() {
    String packageId = packageModel.getCruiseId();
    if (packageModel.getSegment() != null) {
      packageId = packageId + "_" + packageModel.getSegment();
    }
    return packageId;
  }
}
