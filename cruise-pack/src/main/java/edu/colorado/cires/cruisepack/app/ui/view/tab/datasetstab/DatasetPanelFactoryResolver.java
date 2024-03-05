package edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab;

import edu.colorado.cires.cruisepack.app.service.metadata.Instrument;
import edu.colorado.cires.cruisepack.app.ui.model.BaseDatasetInstrumentModel;
import edu.colorado.cires.cruisepack.app.ui.view.common.DropDownItem;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.ancillary.AncillaryDatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.gravity.GravityDatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.magnetics.MagneticsDatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.multibeam.BaseDatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.navigation.NavigationDatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.singebeam.SinglebeamDatasetPanelFactory;
import edu.colorado.cires.cruisepack.app.ui.view.tab.datasetstab.wcsd.WaterColumnSonarDatasetPanelFactory;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
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
    shortCodes = Arrays.stream(InstrumentGroupName.values()).map(InstrumentGroupName::getShortName).collect(Collectors.toSet());
  }

  public Set<String> getImplementedShortCodes() {
    return shortCodes;
  }

  public DatasetPanel createDatasetPanel(DropDownItem dataType) {
    InstrumentGroupName groupName = InstrumentGroupName.fromShortName(
        dataType.getId()
    );
    return getFactory(
      groupName  
    ).createPanel(groupName.getShortName());
  }
  
  private DatasetPanelFactory getFactory(InstrumentGroupName groupName) {
    Class<?> clazz = switch (groupName) {
      case SUB_BOTTOM, SIDE_SCAN, MULTIBEAM, OTHER, ADCP, XBT, CTD -> BaseDatasetPanelFactory.class;
      case NAVIGATION -> NavigationDatasetPanelFactory.class;
      case MAGNETICS -> MagneticsDatasetPanelFactory.class;
      case ANCILLARY -> AncillaryDatasetPanelFactory.class;
      case GRAVITY -> GravityDatasetPanelFactory.class;
      case WATER_COLUMN -> WaterColumnSonarDatasetPanelFactory.class;
      case SINGLE_BEAM -> SinglebeamDatasetPanelFactory.class;
    };

    return factories.stream()
        .filter(f -> f.getClass().isAssignableFrom(clazz))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(
                String.format(
                    "No factory found of type: %s",
                    clazz.getName()
                )
            )
        );
  }
  
  public DatasetPanel createDatasetPanel(Instrument instrument) {
    BaseDatasetInstrumentModel model = createDatasetModel(instrument);
    return getFactory(
        InstrumentGroupName.fromLongName(
            instrument.getType()
        )
    ).createPanel(model);
  }
  
  private BaseDatasetInstrumentModel createDatasetModel(Instrument instrument) {
    return getFactory(
        InstrumentGroupName.fromLongName(
            instrument.getType()
        )
    ).createModel(instrument);
  }
}
