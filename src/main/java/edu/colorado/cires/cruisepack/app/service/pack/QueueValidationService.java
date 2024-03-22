package edu.colorado.cires.cruisepack.app.service.pack;

import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.ui.model.ErrorModel;
import edu.colorado.cires.cruisepack.app.ui.model.queue.QueueModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class QueueValidationService {
  
  private final Validator validator;
  private final QueueModel queueModel;
  private final ErrorModel errorModel;

  @Autowired
  public QueueValidationService(Validator validator, QueueModel queueModel, ErrorModel errorModel) {
    this.validator = validator;
    this.queueModel = queueModel;
    this.errorModel = errorModel;
  }
  
  public Optional<Stream<PackJob>> validate() {
    Set<ConstraintViolation<QueueModel>> constraintViolations = validator.validate(
        queueModel
    );
    
    queueModel.setErrors(constraintViolations);
    
    if (constraintViolations.isEmpty()) {
      return Optional.of(
          queueModel.getQueue().stream()
      );
    }
    
    errorModel.emitErrorMessage(
        "Failed to submit queue. Please fix indicated errors and re-submit form"
    );
    return Optional.empty();
  }
}
