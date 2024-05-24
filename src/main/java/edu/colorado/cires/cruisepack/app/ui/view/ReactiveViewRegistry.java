package edu.colorado.cires.cruisepack.app.ui.view;

import edu.colorado.cires.cruisepack.app.ui.controller.ReactiveView;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class ReactiveViewRegistry {

  private final Set<ReactiveView> reactiveViews = new HashSet<>();

  public void register(ReactiveView view) {
    reactiveViews.add(view);
  }
  
  public void deRegister(ReactiveView view) {
    reactiveViews.remove(view);
  }

  public Collection<ReactiveView> getViews() {
    return reactiveViews;
  }

}
