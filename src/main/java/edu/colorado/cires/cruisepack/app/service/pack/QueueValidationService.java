package edu.colorado.cires.cruisepack.app.service.pack;

import edu.colorado.cires.cruisepack.app.datastore.CruiseDataDatastore;
import edu.colorado.cires.cruisepack.app.datastore.InstrumentDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.datastore.PortDatastore;
import edu.colorado.cires.cruisepack.app.datastore.SeaDatastore;
import edu.colorado.cires.cruisepack.app.service.PackJob;
import edu.colorado.cires.cruisepack.app.service.PackJobUtils;
import edu.colorado.cires.cruisepack.app.ui.model.DropDownItemModel;
import edu.colorado.cires.cruisepack.app.ui.model.ErrorModel;
import edu.colorado.cires.cruisepack.app.ui.model.queue.QueueModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueueValidationService {
  
  private final Validator validator;
  private final QueueModel queueModel;
  private final ErrorModel errorModel;
  private final CruiseDataDatastore cruiseDataDatastore;
  private final SeaDatastore seaDatastore;
  private final PortDatastore portDatastore;
  private final PersonDatastore personDatastore;
  private final InstrumentDatastore instrumentDatastore;

  @Autowired
  public QueueValidationService(Validator validator, QueueModel queueModel, ErrorModel errorModel, CruiseDataDatastore cruiseDataDatastore,
      SeaDatastore seaDatastore, PortDatastore portDatastore, PersonDatastore personDatastore, InstrumentDatastore instrumentDatastore) {
    this.validator = validator;
    this.queueModel = queueModel;
    this.errorModel = errorModel;
    this.cruiseDataDatastore = cruiseDataDatastore;
    this.seaDatastore = seaDatastore;
    this.portDatastore = portDatastore;
    this.personDatastore = personDatastore;
    this.instrumentDatastore = instrumentDatastore;
  }
  
  public Optional<Stream<PackJob>> validate() {
    Set<ConstraintViolation<QueueModel>> constraintViolations = validator.validate(
        queueModel
    );
    
    queueModel.setErrors(constraintViolations);
    
    if (constraintViolations.isEmpty()) {
      return Optional.of(
          queueModel.getQueue().stream()
              .map(DropDownItemModel::getItem)
              .map(DropDownItem::getId)
              .map(cruiseDataDatastore::getByPackageId)
              .filter(Optional::isPresent)
              .map(Optional::get)
              .map(cruiseData -> PackJobUtils.create(
                  cruiseData, seaDatastore, portDatastore, personDatastore, instrumentDatastore
              ))
      );
    }
    
    errorModel.emitErrorMessage(
        "Failed to submit queue. Please fix indicated errors and re-submit form"
    );
    return Optional.empty();
  }
}
