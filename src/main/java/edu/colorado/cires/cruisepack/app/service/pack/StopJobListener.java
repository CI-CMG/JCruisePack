package edu.colorado.cires.cruisepack.app.service.pack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
class StopJobListener implements ApplicationListener<StopJobEvent> {
  
  private final PackingScheduler packingScheduler;

  @Autowired
  StopJobListener(PackingScheduler packingScheduler) {
    this.packingScheduler = packingScheduler;
  }

  @Override
  public void onApplicationEvent(@NonNull StopJobEvent event) {
    packingScheduler.stopJob(event.getPackageId());
  }
}
