package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import edu.colorado.cires.cruisepack.app.ui.model.DropDownItemModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidDropDownItemModel.ValidDropDownItemModelValidator;
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
@Repeatable(ValidDropDownItemModel.List.class)
@Constraint(validatedBy = ValidDropDownItemModelValidator.class)
@Documented
public @interface ValidDropDownItemModel {
  
  String message() default "invalid value";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};

  @Target({ FIELD, TYPE_USE })
  @Retention(RUNTIME)
  @Documented
  @interface List {
    ValidDropDownItemModel[] value();
  }
  
  class ValidDropDownItemModelValidator implements ConstraintValidator<ValidDropDownItemModel, DropDownItemModel> {

    @Override
    public boolean isValid(DropDownItemModel value, ConstraintValidatorContext context) {
      return !Objects.equals(value.getItem().getId(), "");
    }
  }

}
