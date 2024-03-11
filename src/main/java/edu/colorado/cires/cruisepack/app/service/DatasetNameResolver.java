package edu.colorado.cires.cruisepack.app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DatasetNameResolver {

  public static void setDirNamesOnInstruments(String mainName, Map<InstrumentDetailPackageKey, List<InstrumentNameHolder>> instruments) {
    for (Entry<InstrumentDetailPackageKey, List<InstrumentNameHolder>> entry : instruments.entrySet()) {
      List<InstrumentNameHolder> details = entry.getValue();
      if (details.size() == 1) {
        setSimpleDetailInfo(mainName, details.get(0), entry.getKey());
      } else {
        setUniqueDetailInfo(mainName, details, entry.getKey());
      }
    }
  }

  private static void setUniqueDetailInfo(String mainName, List<InstrumentNameHolder> details, InstrumentDetailPackageKey pkg) {

    Set<String> instruments = new HashSet<>();
    Set<String> processed = new HashSet<>();
    Set<String> products = new HashSet<>();

    int rawCount = 0;
    int processedCount = 0;
    int productsCount = 0;
    String name = null;
    String bagName = null;

    for (InstrumentNameHolder dataset : details) {
      String dirName;
      if (!instruments.contains(dataset.getInstrument())) {
        instruments.add(dataset.getInstrument());

        if (pkg.getInstrumentGroupShortType().equals(pkg.getInstrumentShortCode())) {
          name = dataset.getInstrument().replaceAll(" ", "_");
          bagName = mainName + "_" + pkg.getInstrumentGroupShortType() + "_" + name;
        } else {
          name = dataset.getShortName();
          bagName = mainName + "_" + pkg;
        }

        switch (dataset.getStatus()) {
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
        switch (dataset.getStatus()) {
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

  private static void setSimpleDetailInfo(String mainName, InstrumentNameHolder dataset, InstrumentDetailPackageKey pkg) {
    String dirName;
    switch (dataset.getStatus()) {
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


    String bagName;
    if (pkg.getInstrumentGroupShortType().equals(pkg.getInstrumentGroupShortType())) {
      bagName = mainName + "_" + pkg.getInstrumentGroupShortType();
    } else {
      bagName = mainName + "_" + pkg;
    }

    dataset.setDirName(dirName);
    dataset.setBagName(bagName);
  }
}
