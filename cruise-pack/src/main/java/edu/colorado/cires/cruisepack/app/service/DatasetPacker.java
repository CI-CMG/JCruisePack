package edu.colorado.cires.cruisepack.app.service;

import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.copy;
import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.filterHidden;
import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.filterTimeSize;
import static edu.colorado.cires.cruisepack.app.service.CruisePackFileUtils.mkDir;

import edu.colorado.cires.cruisepack.app.config.ServiceProperties;
import edu.colorado.cires.cruisepack.app.service.metadata.CruiseMetadata;
import gov.loc.repository.bagit.creator.BagCreator;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

public class DatasetPacker {






}
