package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
import edu.colorado.cires.cruisepack.app.ui.controller.PackageController;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.model.DatasetsModel;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.model.PeopleModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.xml.instrument.FileExtensionList;
import edu.colorado.cires.cruisepack.xml.instrument.Instrument;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
  private final DatasetsModel datasetsModel;
  private final PeopleModel peopleModel;
  private final PackageController packageController;
  private final FooterControlController footerControlController;
  private final InstrumentDatastore instrumentDatastore;

  @Autowired
  public PackagingValidationService(
      Validator validator,
      PackageModel packageModel,
      OmicsModel omicsModel,
      CruiseInformationModel cruiseInformationModel,
      DatasetsModel datasetsModel,
      PackageController packageController,
      FooterControlController footerControlController,
      InstrumentDatastore instrumentDatastore,
      CruiseInformationModel cruiseInformationModel,
      PackageController packageController,
      FooterControlController footerControlController,
      PeopleModel peopleModel) {
    this.validator = validator;
    this.packageModel = packageModel;
    this.omicsModel = omicsModel;
    this.cruiseInformationModel = cruiseInformationModel;
    this.datasetsModel = datasetsModel;
    this.packageController = packageController;
    this.footerControlController = footerControlController;
    this.instrumentDatastore = instrumentDatastore;
    this.peopleModel = peopleModel;
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

  private void updateDatasetsErrors(Set<ConstraintViolation<DatasetsModel>> datasetsViolations) {
    LOGGER.warn("datasets {}", datasetsViolations);
    // TODO
  }

  private PackJob createPackJob() {
    String packageId = resolvePackageId();
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
        .setPackageId(packageId)
        .setInstruments(getInstruments(packageId))
        .build();
  }

  private Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> getDirNames(String packageId) {
    Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> namers = new LinkedHashMap<>();
    for (BaseDatasetInstrumentModel instrumentModel : datasetsModel.getDatasets()) {
      instrumentModel.getPackageKey().ifPresent(key -> {
        instrumentModel.getInstrumentNameHolder().ifPresent(nameHolder -> {
          List<InstrumentNameHolder> holders = namers.get(key);
          if (holders == null) {
            holders = new ArrayList<>();
            namers.put(key, holders);
          }
          holders.add(nameHolder);
        });
      });
    }
    DatasetNameResolver.setDirNamesOnInstruments(packageId, namers);
    return namers;
  }

  private Map<InstrumentDetailPackageKey, List<InstrumentDetail>> getInstruments(String packageId) {
    Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> namers = getDirNames(packageId);
    Map<InstrumentDetailPackageKey, List<InstrumentDetail>> map = new LinkedHashMap<>();
    for (Entry<InstrumentDetailPackageKey, List<InstrumentNameHolder>> entry : namers.entrySet()) {
      InstrumentDetailPackageKey pkg = entry.getKey();
      List<InstrumentDetail> instrumentDetails = new ArrayList<>(entry.getValue().size());
      for (InstrumentNameHolder nameHolder : entry.getValue()) {
        Instrument instrument = instrumentDatastore.getInstrument(pkg)
            .orElseThrow(() -> new IllegalStateException("Unable to find instrument " + pkg));
        Set<String> exts = new LinkedHashSet<>();
        FileExtensionList fileExtensionList = instrument.getFileExtensions();
        if (fileExtensionList != null) {
          List<String> extList = fileExtensionList.getFileExtensions();
          if (extList != null) {
            exts.addAll(extList);
          }
        }

        instrumentDetails.add(
            InstrumentDetail.builder()
                .setStatus(nameHolder.getStatus())
                .setInstrument(nameHolder.getInstrument())
                .setShortName(nameHolder.getShortName())
                .setExtensions(exts)
                .setDataPath(nameHolder.getDataPath())
                .setFlatten(instrument.isFlatten())
                .setDirName(nameHolder.getDirName())
                .setBagName(nameHolder.getBagName())
                .build());

      }
      map.put(pkg, Collections.unmodifiableList(instrumentDetails));
    }
    return map;
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
