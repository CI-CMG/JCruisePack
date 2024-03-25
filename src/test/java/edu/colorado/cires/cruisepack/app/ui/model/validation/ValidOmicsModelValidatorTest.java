package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import edu.colorado.cires.cruisepack.app.ui.model.OmicsModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathExists.PathExistsValidator;
import edu.colorado.cires.cruisepack.app.ui.model.validation.PathIsFile.PathIsFileValidator;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidExpectedAnalyses.ValidExpectedAnalysesValidator;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidOmicsModel.ValidOmicsModelValidator;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidPersonDropDownItem.ValidPersonDropDownItemValidator;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidSamplingTypes.ValidSamplingTypesValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.NotNullValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidOmicsModelValidatorTest extends ConstraintValidatorTest<OmicsModel, ValidOmicsModelValidator> {

  private final ValidPersonDropDownItemValidator validPersonDropDownItemValidator = mock(ValidPersonDropDownItemValidator.class);
  private final NotNullValidator notNullValidator = mock(NotNullValidator.class);
  private final PathExistsValidator pathExistsValidator = mock(PathExistsValidator.class);
  private final PathIsFileValidator pathIsFileValidator = mock(PathIsFileValidator.class);
  private final NotBlankValidator notBlankValidator = mock(NotBlankValidator.class);
  private final ValidSamplingTypesValidator validSamplingTypesValidator = mock(ValidSamplingTypesValidator.class);
  private final ValidExpectedAnalysesValidator validExpectedAnalysesValidator = mock(ValidExpectedAnalysesValidator.class);
  
  @ParameterizedTest
  @CsvSource({
      "true,false,false,false,false,false,false,false,false",
      "false,true,false,false,false,false,false,false,false",
      "false,true,true,false,false,false,false,false,false",
      "false,true,false,true,false,false,false,false,false",
      "false,true,false,false,true,false,false,false,false",
      "false,true,false,false,false,true,false,false,false",
      "false,true,false,false,false,false,true,false,false",
      "false,true,false,false,false,false,false,true,false",
      "false,true,false,false,false,false,false,false,true",
      "false,true,true,true,true,false,true,true,true",
      "true,true,true,true,true,true,true,true,true",
  })
  void testValidator(
    boolean expectedPass,
    boolean samplingConducted,
    boolean validPersonDropDownItemValidatorPass,
    boolean notNullValidatorPass,
    boolean pathExistsValidatorPass,
    boolean pathIsFileValidatorPass,
    boolean notBlankValidatorPass,
    boolean validSamplingTypesValidatorPass,
    boolean validExpectedAnalysesValidatorPass
  ) {
    reset(
      validPersonDropDownItemValidator,
      notNullValidator,
      pathExistsValidator,
      pathIsFileValidator,
      notBlankValidator,
      validSamplingTypesValidator,
      validExpectedAnalysesValidator
    );

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
    
    OmicsModel model = new OmicsModel();
    model.setSamplingConducted(samplingConducted);

    when(validPersonDropDownItemValidator.isValid(any(), any())).thenReturn(validPersonDropDownItemValidatorPass);
    when(notNullValidator.isValid(any(), any())).thenReturn(notNullValidatorPass);
    when(pathExistsValidator.isValid(any(), any())).thenReturn(pathExistsValidatorPass);
    when(pathIsFileValidator.isValid(any(), any())).thenReturn(pathIsFileValidatorPass);
    when(notBlankValidator.isValid(any(), any())).thenReturn(notBlankValidatorPass);
    when(validSamplingTypesValidator.isValid(any(), any())).thenReturn(validSamplingTypesValidatorPass);
    when(validExpectedAnalysesValidator.isValid(any(), any())).thenReturn(validExpectedAnalysesValidatorPass);
    
    assertEquals(expectedPass, validator.isValid(model, VALIDATOR_CONTEXT));
  }

  @Override
  protected ValidOmicsModelValidator createValidator() {
    return new ValidOmicsModelValidator(
        validPersonDropDownItemValidator,
        notNullValidator,
        pathExistsValidator,
        pathIsFileValidator,
        notBlankValidator,
        validSamplingTypesValidator,
        validExpectedAnalysesValidator
    );
  }
}
