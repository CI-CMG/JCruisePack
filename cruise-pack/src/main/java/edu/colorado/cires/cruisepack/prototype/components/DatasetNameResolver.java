package edu.colorado.cires.cruisepack.prototype.components;

import edu.colorado.cires.cruisepack.prototype.components.model.InstrumentDetail;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DatasetNameResolver {

  public static void setDirNamesOnInstruments(String mainName, Map<String, List<InstrumentDetail>> instruments) {
    for (Entry<String, List<InstrumentDetail>> entry : instruments.entrySet()) {
      String pkg = entry.getKey();
      List<InstrumentDetail> details = entry.getValue();
      if (details.size() == 1) {
        setSimpleDetailInfo(mainName, details.get(0), pkg);
      } else {
        setUniqueDetailInfo(mainName, details, pkg);
      }
    }
  }

  private static void setUniqueDetailInfo(String mainName, List<InstrumentDetail> details, String pkg) {
    //TODO limit to 2?
    String[] packageParts = pkg.split("_", 2);

    Set<String> instruments = new HashSet<>();
    Set<String> processed = new HashSet<>();
    Set<String> products = new HashSet<>();

    int rawCount = 0;
    int processedCount = 0;
    int productsCount = 0;
    String name = null;
    String bagName = null;

    for (InstrumentDetail dataset : details) {
      String dirName;
      if (!instruments.contains(dataset.getInstrument())) {
        instruments.add(dataset.getInstrument());

        if (packageParts.length == 2 && packageParts[0].equals(packageParts[1])){
          name = dataset.getInstrument().replaceAll(" ", "_");
          bagName = mainName + "_" + packageParts[0] + "_" + name;
        } else {
          name = dataset.getShortName();
          bagName = mainName + "_" + pkg;
        }

        switch (dataset.getStatus()){
          case RAW:
            dirName = name;
            rawCount = 1;
            break;
          case PROCESSED:
            dirName = name + "_processed";
            processed.add(dataset.getInstrument());
            processedCount = 1;
            break;
          case PRODUCTS:
            dirName = name + "_products";
            products.add(dataset.getInstrument());
            productsCount = 1;
            break;
          default:
            throw new IllegalStateException("Unsupported status: " + dataset.getStatus());
        }

      } else {
        switch (dataset.getStatus()){
          case RAW:
            if (rawCount == 0) {
              dirName = name;
            } else {
              dirName = name + "-" + rawCount;
            }
            rawCount++;
            break;
          case PROCESSED:
            if (!processed.contains(dataset.getInstrument())) {
              dirName = name + "_processed";
              processed.add(dataset.getInstrument());
            } else {
              dirName = name + "_processed-" + processedCount;
            }
            processedCount++;
            break;
          case PRODUCTS:
            if (!products.contains(dataset.getInstrument())) {
              dirName = name + "_products";
              products.add(dataset.getInstrument());
            } else {
              dirName = name + "_products-" + productsCount;
            }
            productsCount++;
            break;
          default:
            throw new IllegalStateException("Unsupported status: " + dataset.getStatus());
        }
      }

      dataset.setDirName(dirName);
      dataset.setBagName(bagName);
    }
  }

  private static void setSimpleDetailInfo(String mainName,InstrumentDetail dataset, String pkg) {
    String dirName;
    switch (dataset.getStatus()){
      case RAW:
        dirName = dataset.getShortName();
        break;
      case PROCESSED:
        dirName = dataset.getShortName() + "_processed";
        break;
      case PRODUCTS:
        dirName = dataset.getShortName() + "_products";
        break;
      default:
        throw new IllegalStateException("Unsupported status: " + dataset.getStatus());
    }

    //TODO limiting to 2.  is this correct?
    String[] packageParts = pkg.split("_", 2);

    String bagName;
    if (packageParts.length == 2 && packageParts[0].equals(packageParts[1])){
      bagName = mainName + "_" + packageParts[0];
    } else {
      bagName = mainName + "_" + pkg;
    }

    dataset.setDirName(dirName);
    dataset.setBagName(bagName);
  }
}
