package edu.colorado.cires.cruisepack.app.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;

public final class CruisePackFileUtils {

  public static boolean filterTimeSize(Path source, Path target) {
    try {
      return !Files.isRegularFile(target) ||
          Files.getLastModifiedTime(source).toMillis() != Files.getLastModifiedTime(target).toMillis() ||
          Files.size(source) != Files.size(target);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read file " + source + " or " + target, e);
    }
  }

  public static boolean filterHidden(Path path) {
    try {
      return Files.isRegularFile(path) && !Files.isHidden(path);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read file " + path, e);
    }
  }

  public static void mkDir(Path dir) {
    try {
      Files.createDirectories(dir);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to create directory " + dir, e);
    }
  }

  public static void copy(Path source, Path target) {
    try {
      FileUtils.copyFile(source.toFile(), target.toFile(), true);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to copy file " + source + " to " + target, e);
    }
  }

  private CruisePackFileUtils() {

  }
}
