package edu.colorado.cires.cruisepack.app.service.pack;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
class ClearJobEventListener implements ApplicationListener<ClearJobsEvent> {
  
  private final PackingScheduler packingScheduler;

  public ClearJobEventListener(PackingScheduler packingScheduler) {
    this.packingScheduler = packingScheduler;
  }

  @Override
  public void onApplicationEvent(@NonNull ClearJobsEvent event) {
    packingScheduler.clearJobs();
    event.getExecuteAfter().run();
  }
}
