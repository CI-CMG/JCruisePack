package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import edu.colorado.cires.cruisepack.app.datastore.OrganizationDatastore;
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
@Repeatable(ValidOrganizationDropDownItemModel.List.class)
@Constraint(validatedBy = ValidOrganizationDropDownItemModel.ValidOrganizationDropDownItemModelValidator.class)
@Documented
public @interface ValidOrganizationDropDownItemModel {
  
  String message() default "invalid value";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};

  @Target({ FIELD, TYPE_USE })
  @Retention(RUNTIME)
  @Documented
  @interface List {
    ValidOrganizationDropDownItemModel[] value();
  }
  
  class ValidOrganizationDropDownItemModelValidator implements ConstraintValidator<ValidOrganizationDropDownItemModel, DropDownItemModel> {

    @Override
    public boolean isValid(DropDownItemModel value, ConstraintValidatorContext context) {
      return !Objects.equals(value.getItem(), OrganizationDatastore.UNSELECTED_ORGANIZATION);
    }
  }

}
