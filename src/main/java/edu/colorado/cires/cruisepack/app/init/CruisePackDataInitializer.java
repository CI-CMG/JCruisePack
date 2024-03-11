package edu.colorado.cires.cruisepack.app.init;

import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public final class CruisePackDataInitializer {


  public static Resource[] getPackagedData() {
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(CruisePackDataInitializer.class.getClassLoader());
    try {
      return resolver.getResources("classpath:/edu/colorado/cires/cruisepack/data/*.xml");
    } catch (IOException e) {
      throw new RuntimeException("Unable to resolve data files", e);
    }
  }

  private CruisePackDataInitializer() {

  }
}
