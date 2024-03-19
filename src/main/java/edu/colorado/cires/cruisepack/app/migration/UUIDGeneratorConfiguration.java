package edu.colorado.cires.cruisepack.app.migration;

import java.util.UUID;
import java.util.function.Supplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UUIDGeneratorConfiguration {
  
  @Bean
  Supplier<String> uuidGenerator() {
    return () -> UUID.randomUUID().toString();
  }

}
