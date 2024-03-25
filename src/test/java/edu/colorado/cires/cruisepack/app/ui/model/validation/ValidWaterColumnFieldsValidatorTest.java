package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import edu.colorado.cires.cruisepack.app.ui.model.dataset.WaterColumnAdditionalFieldsModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidWaterColumnFields.ValidWaterColumnFieldsValidator;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import java.nio.file.Paths;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ValidWaterColumnFieldsValidatorTest extends ConstraintValidatorTest<WaterColumnAdditionalFieldsModel, ValidWaterColumnFieldsValidator> {
  
  @Test
  void testValidator() {
    when(VALIDATOR_CONTEXT.buildConstraintViolationWithTemplate(any())).thenReturn(
        new ConstraintViolationBuilder() {
          @Override
          public NodeBuilderDefinedContext addNode(String name) {
            return null;
          }

          @Override
          public NodeBuilderCustomizableContext addPropertyNode(String name) {
            return new NodeBuilderCustomizableContext() {
              @Override
              public NodeContextBuilder inIterable() {
                return null;
              }

              @Override
              public NodeBuilderCustomizableContext inContainer(Class<?> containerClass, Integer typeArgumentIndex) {
                return null;
              }

              @Override
              public NodeBuilderCustomizableContext addNode(String name) {
                return null;
              }

              @Override
              public NodeBuilderCustomizableContext addPropertyNode(String name) {
                return null;
              }

              @Override
              public LeafNodeBuilderCustomizableContext addBeanNode() {
                return null;
              }

              @Override
              public ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String name, Class<?> containerType,
                  Integer typeArgumentIndex) {
                return null;
              }

              @Override
              public ConstraintValidatorContext addConstraintViolation() {
                return null;
              }
            };
          }

          @Override
          public LeafNodeBuilderCustomizableContext addBeanNode() {
            return null;
          }

          @Override
          public ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String name, Class<?> containerType,
              Integer typeArgumentIndex) {
            return null;
          }

          @Override
          public NodeBuilderDefinedContext addParameterNode(int index) {
            return null;
          }

          @Override
          public ConstraintValidatorContext addConstraintViolation() {
            return null;
          }
        }
    );
    
    WaterColumnAdditionalFieldsModel model = new WaterColumnAdditionalFieldsModel();
    assertTrue(validator.isValid(model, VALIDATOR_CONTEXT));
    model.setCalibrationState(new DropDownItem("1", "Calibrated w/ calibration data"));
    assertFalse(validator.isValid(model, VALIDATOR_CONTEXT));
    model.setCalibrationReportPath(Paths.get("report path"));
    assertFalse(validator.isValid(model, VALIDATOR_CONTEXT));
    model.setCalibrationDataPath(Paths.get("data path"));
    assertFalse(validator.isValid(model, VALIDATOR_CONTEXT));
    model.setCalibrationDate(LocalDate.now());
    assertTrue(validator.isValid(model, VALIDATOR_CONTEXT));
    model.setCalibrationState(null);
    assertTrue(validator.isValid(model, VALIDATOR_CONTEXT));
  }

  @Override
  protected ValidWaterColumnFieldsValidator createValidator() {
    return new ValidWaterColumnFieldsValidator();
  }
}
