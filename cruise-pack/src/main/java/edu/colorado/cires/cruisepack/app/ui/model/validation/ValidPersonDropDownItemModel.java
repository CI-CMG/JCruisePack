package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import edu.colorado.cires.cruisepack.app.datastore.PersonDatastore;
import edu.colorado.cires.cruisepack.app.ui.model.DropDownItemModel;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Objects;

@Target({ FIELD, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(ValidPersonDropDownItemModel.List.class)
@Constraint(validatedBy = ValidPersonDropDownItemModel.ValidPersonDropDownItemModelValidator.class)
@Documented
public @interface ValidPersonDropDownItemModel {
  
  String message() default "invalid value";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};

  @Target({ FIELD, TYPE_USE })
  @Retention(RUNTIME)
  @Documented
  @interface List {
    ValidPersonDropDownItemModel[] value();
  }
  
  class ValidPersonDropDownItemModelValidator implements ConstraintValidator<ValidPersonDropDownItemModel, DropDownItemModel> {

    @Override
    public boolean isValid(DropDownItemModel value, ConstraintValidatorContext context) {
      return !Objects.equals(value.getItem(), PersonDatastore.UNSELECTED_PERSON);
    }
  }
}
