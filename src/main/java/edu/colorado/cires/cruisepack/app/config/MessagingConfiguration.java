package edu.colorado.cires.cruisepack.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class MessagingConfiguration {
  
  @Bean
  public ApplicationEventMulticaster applicationEventMulticaster() {
    SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
    
    eventMulticaster.setTaskExecutor(
        new SimpleAsyncTaskExecutor()
    );
    
    return eventMulticaster;
  }

}
