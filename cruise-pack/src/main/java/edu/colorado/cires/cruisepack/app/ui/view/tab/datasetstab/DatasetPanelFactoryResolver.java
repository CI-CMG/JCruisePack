package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatasetPanelFactoryResolver {

  private final Collection<DatasetPanelFactory> factories;
  private Set<String> shortCodes;

  @Autowired
  public DatasetPanelFactoryResolver(Collection<DatasetPanelFactory> factories) {
    this.factories = factories;
  }

  @PostConstruct
  public void init() {
    shortCodes = factories.stream().map(DatasetPanelFactory::getInstrumentGroupShortCode).collect(Collectors.toSet());
  }

  public Set<String> getImplementedShortCodes() {
    return shortCodes;
  }

  public DatasetPanel createDatasetPanel(DropDownItem dataType) {
    return factories.stream()
        .filter(dpf -> dpf.getInstrumentGroupShortCode().equals(dataType.getId()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Unable to find panel factory for type: " + dataType))
        .createPanel(dataType);
  }
}
