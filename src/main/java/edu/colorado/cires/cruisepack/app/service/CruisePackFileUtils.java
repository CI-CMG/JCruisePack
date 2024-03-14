package edu.colorado.cires.cruisepack.app.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.apache.commons.codec.digest.DigestUtils;
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

  public static void cleanDir(Path dir) {
    try {
      FileUtils.cleanDirectory(dir.toFile());
    } catch (IOException e) {
      throw new IllegalStateException("Unable to clean directory " + dir, e);
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

  public static void concatManifests(Path childManifest, Path mainBagPath, FileWriter parentManifestWriter) {
    try (
        FileReader reader = new FileReader(childManifest.toFile(), StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(reader)
    ) {
      String line = bufferedReader.readLine();
      while (line != null) {
        String[] lineParts = line.split(" {2}");
        Path path = mainBagPath.relativize(childManifest.getParent()).resolve(lineParts[1]);
        parentManifestWriter.write(String.format(
            "%s  %s\n",
            lineParts[0], path
        ));
        line = bufferedReader.readLine();
      }
    } catch (IOException e) {
      throw new IllegalStateException(String.format(
          "Failed to append to manifest from %s",
          childManifest
      ), e);
    }
  }

  public static void appendToManifest(Path path, Path mainBagPath, FileWriter manifestWriter) {
    try (Stream<Path> paths = Files.walk(path)) {
      paths.filter(p -> p.toFile().isFile())
          .forEach(p -> {
            try {
              manifestWriter.write(
                  String.format(
                      "%s  %s\n",
                      computeChecksum(p), mainBagPath.relativize(p)
                  )
              );
            } catch (IOException e) {
              throw new IllegalStateException(String.format(
                  "Failed to append %s to manifest",
                  p
              ), e);
            }
          });
    } catch (IOException e) {
      throw new IllegalStateException(String.format(
          "Failed to append %s to manifest",
          path
      ), e);
    }
  }

  public static String computeChecksum(Path path) {
    try (InputStream inputStream = new FileInputStream(path.toFile())) {
      return DigestUtils.sha256Hex(inputStream);
    } catch (IOException e) {
      throw new IllegalStateException(String.format(
          "Failed to compute checksum for %s",
          path
      ), e);
    }
  }

  private CruisePackFileUtils() {

  }
}
