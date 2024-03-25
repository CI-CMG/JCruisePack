package edu.colorado.cires.cruisepack.app.ui.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.cruisepack.app.service.PackJob;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class PackStateModelTest extends PropertyChangeModelTest<PackStateModel> {
  
  private static final String PROCESS_ID = "TEST-PROCESS-ID";
  private static final PackJob PACK_JOB = PackJob.builder()
      .setCruiseId("CRUISE-ID")
      .build();

  @Override
  protected PackStateModel createModel() {
    return new PackStateModel(PROCESS_ID, PACK_JOB);
  }
  
  @Test
  void incrementProgress() {
    model.setProgressIncrement((float) 100 / 200);
    
    model.incrementProgress();
    float currentProgress = model.getProgress();
    float previousProgress = 0;
    while (currentProgress != 100) {
      assertEquals(previousProgress, currentProgress - 0.5);
      model.incrementProgress();
      previousProgress = currentProgress;
      currentProgress = model.getProgress();
    }

    assertChangeEvents( // should only emit whole numbers
        "UPDATE_PROGRESS_TEST-PROCESS-ID",
        IntStream.range(0, 100).boxed().toList(),
        IntStream.range(1, 101).boxed().toList(),
        (v) -> v
    );
  }

  @Test
  void setProgressIncrement() {
    model.setProgressIncrement(10);
    assertEquals(10f, model.getProgressIncrement());
  }

  @Test
  void setProcessing() {
    model.setProcessing(true);
    assertTrue(model.isProcessing());
    
    model.setProgressIncrement(10);
    model.incrementProgress();
    
    model.setProcessing(false);
    assertFalse(model.isProcessing());
    assertEquals(0f, model.getProgress());
    assertEquals(0f, model.getProgressIncrement());
  }
}