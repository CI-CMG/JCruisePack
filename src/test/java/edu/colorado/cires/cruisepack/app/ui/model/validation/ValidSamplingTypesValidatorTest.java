package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.cruisepack.app.ui.model.SamplingTypesModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidSamplingTypes.ValidSamplingTypesValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidSamplingTypesValidatorTest extends ConstraintValidatorTest<SamplingTypesModel, ValidSamplingTypesValidator> {
  
  @ParameterizedTest
  @CsvSource({
      "true,true,false,false",
      "true,false,true,false",
      "true,false,false,true",
      "false,false,false,false"
  })
  public void samplingTypesValidator(boolean expectedPass, boolean isWaterSamplingType, boolean isOrganicTissueSamplingType, boolean isSoilSedimentSamplingType) {
    SamplingTypesModel model = new SamplingTypesModel();
    model.setOrganicTissue(isOrganicTissueSamplingType);
    model.setWater(isWaterSamplingType);
    model.setSoilSediment(isSoilSedimentSamplingType);
    assertEquals(
        expectedPass,
        validator.isValid(model, VALIDATOR_CONTEXT)
    );
  }


  @Override
  protected ValidSamplingTypesValidator createValidator() {
    return new ValidSamplingTypesValidator();
  }
}
