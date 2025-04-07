package edu.colorado.cires.cruisepack.app.service.pack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
class PackingJobEventListener implements ApplicationListener<PackingJobEvent> {
  
  private final PackingScheduler packingScheduler;

  @Autowired
  PackingJobEventListener(PackingScheduler packingScheduler) {
    this.packingScheduler = packingScheduler;
  }

  @Override
  public void onApplicationEvent(@NonNull PackingJobEvent event) {
    try {
      packingScheduler.scheduleJob(event);
    } catch (PackerExecutorException e) {
      //TODO should this be handled better?
      throw new RuntimeException("An error occurred while packing", e);
    }
  }
}
