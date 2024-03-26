package edu.colorado.cires.cruisepack.app.config;

import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.NotNullValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfiguration {
  
  @Bean
  public NotNullValidator notNullValidator() {
    return new NotNullValidator();
  }
  
  @Bean
  public NotBlankValidator notBlankValidator() {
    return new NotBlankValidator();
  }

}
