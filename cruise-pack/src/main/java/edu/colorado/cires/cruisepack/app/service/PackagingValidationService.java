package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
import edu.colorado.cires.cruisepack.app.ui.controller.PackageController;
import edu.colorado.cires.cruisepack.app.ui.model.CruiseInformationModel;
import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackagingValidationService {

  private final Validator validator;
  private final PackageModel packageModel;
  private final OmicsModel omicsModel;
  private final CruiseInformationModel cruiseInformationModel;
  private final PackageController packageController;
  private final FooterControlController footerControlController;

  @Autowired
  public PackagingValidationService(Validator validator, PackageModel packageModel, OmicsModel omicsModel, CruiseInformationModel cruiseInformationModel, PackageController packageController,
      FooterControlController footerControlController) {
    this.validator = validator;
    this.packageModel = packageModel;
    this.omicsModel = omicsModel;
    this.cruiseInformationModel = cruiseInformationModel;
    this.packageController = packageController;
    this.footerControlController = footerControlController;
  }

  public Optional<PackJob> validate() {
    Set<ConstraintViolation<PackageModel>> packageViolations = validator.validate(packageModel);
    Set<ConstraintViolation<CruiseInformationModel>> cruiseInformationViolations = validator.validate(cruiseInformationModel);
    Set<ConstraintViolation<OmicsModel>> omicsViolations = validator.validate(omicsModel);

    updatePackageErrors(packageViolations);
    updateCruiseInformationErrors(cruiseInformationViolations);
    updateOmicsErrors(omicsViolations);

    if (packageViolations.isEmpty() && cruiseInformationViolations.isEmpty() && omicsViolations.isEmpty()) {
      return Optional.of(createPackJob());
    }
    return Optional.empty();
  }

  private void updateOmicsErrors(Set<ConstraintViolation<OmicsModel>> omicsViolations) {
  }

  private void updateCruiseInformationErrors(Set<ConstraintViolation<CruiseInformationModel>> cruiseInformationViolations) {
  }

  private void updatePackageErrors(Set<ConstraintViolation<PackageModel>> packageViolations) {
    // TODO
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
        .setOmicsSamplingTypes(omicsModel.getSamplingTypes())
        .setOmicsExpectedAnalyses(omicsModel.getExpectedAnalyses())
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
