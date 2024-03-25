package edu.colorado.cires.cruisepack.app.ui.model.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.cruisepack.app.ui.model.ExpectedAnalysesModel;
import edu.colorado.cires.cruisepack.app.ui.model.validation.ValidExpectedAnalyses.ValidExpectedAnalysesValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidExpectedAnalysesValidatorTest extends ConstraintValidatorTest<ExpectedAnalysesModel, ValidExpectedAnalysesValidator> {
  
  @ParameterizedTest
  @CsvSource({
      "true,true,false,false,false,false,false,false,false,false,false,false,false,false",
      "true,false,true,false,false,false,false,false,false,false,false,false,false,false",
      "true,false,false,true,false,false,false,false,false,false,false,false,false,false",
      "true,false,false,false,true,false,false,false,false,false,false,false,false,false",
      "true,false,false,false,false,true,false,false,false,false,false,false,false,false",
      "true,false,false,false,false,false,true,false,false,false,false,false,false,false",
      "true,false,false,false,false,false,false,true,false,false,false,false,false,false",
      "true,false,false,false,false,false,false,false,true,false,false,false,false,false",
      "true,false,false,false,false,false,false,false,false,true,false,false,false,false",
      "true,false,false,false,false,false,false,false,false,false,true,false,false,false",
      "true,false,false,false,false,false,false,false,false,false,false,true,false,false",
      "true,false,false,false,false,false,false,false,false,false,false,false,true,false",
      "true,false,false,false,false,false,false,false,false,false,false,false,false,true",
      "false,false,false,false,false,false,false,false,false,false,false,false,false,false",
  })
  void testValidator(
    boolean expectedPass,
    boolean isBarcoding,
    boolean isGenomics,
    boolean isTranscriptomics,
    boolean isProteomics,
    boolean isMetabolomics,
    boolean isEpigenetics,
    boolean isOther,
    boolean isMetabarcoding,
    boolean isMetagenomics,
    boolean isMetatranscriptomics,
    boolean isMetaproteomics,
    boolean isMetametabolomics,
    boolean isMicrobiome
  ) {
    ExpectedAnalysesModel model = new ExpectedAnalysesModel();
    model.setBarcoding(isBarcoding);
    model.setGenomics(isGenomics);
    model.setTranscriptomics(isTranscriptomics);
    model.setProteomics(isProteomics);
    model.setMetabolomics(isMetabolomics);
    model.setEpigenetics(isEpigenetics);
    model.setOther(isOther);
    model.setMetabarcoding(isMetabarcoding);
    model.setMetagenomics(isMetagenomics);
    model.setMetatranscriptomics(isMetatranscriptomics);
    model.setMetaproteomics(isMetaproteomics);
    model.setMetametabolomics(isMetametabolomics);
    model.setMicrobiome(isMicrobiome);
    
    assertEquals(expectedPass, validator.isValid(model, VALIDATOR_CONTEXT));
  }

  @Override
  protected ValidExpectedAnalysesValidator createValidator() {
    return new ValidExpectedAnalysesValidator();
  }
}
