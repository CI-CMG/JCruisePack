package edu.colorado.cires.cruisepack.app.service;

import edu.colorado.cires.cruisepack.app.ui.controller.FooterControlController;
import edu.colorado.cires.cruisepack.app.ui.controller.PackageController;
import edu.colorado.cires.cruisepack.app.ui.model.PackageModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackagingValidationService {

  private final Validator validator;
  private final PackageModel packageModel;
  private final PackageController packageController;
  private final FooterControlController footerControlController;

  @Autowired
  public PackagingValidationService(Validator validator, PackageModel packageModel, PackageController packageController,
      FooterControlController footerControlController) {
    this.validator = validator;
    this.packageModel = packageModel;
    this.packageController = packageController;
    this.footerControlController = footerControlController;
  }

  public Optional<PackJob> validate() {
    Set<ConstraintViolation<PackageModel>> packageViolations = validator.validate(packageModel);
    updatePackageErrors(packageViolations);
    if (!packageViolations.isEmpty()) {
      return Optional.of(createPackJob());
    }
    return Optional.empty();
  }

  private void updatePackageErrors(Set<ConstraintViolation<PackageModel>> packageViolations) {
    // TODO
  }

  private PackJob createPackJob() {
    return new PackJob();
  }
}
