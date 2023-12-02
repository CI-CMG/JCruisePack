package edu.colorado.cires.cruisepack.bag;

import static org.junit.jupiter.api.Assertions.*;

import gov.loc.repository.bagit.creator.BagCreator;
import gov.loc.repository.bagit.domain.Bag;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BaggerTest {

  private Path testDir = Paths.get("target/bagger-test");

  @BeforeEach
  private void beforeEach() throws Exception{
    FileUtils.deleteQuietly(testDir.toFile());
    Files.createDirectories(testDir);
  }

//  @AfterEach
//  private void afterEach() {
//    FileUtils.deleteQuietly(testDir.toFile());
//  }

  @Test
  public void testCreateUpdateRead() throws Exception {
    FileUtils.copyDirectoryToDirectory(Paths.get("src/test/resources/test-bags/loose-bag").toFile(), testDir.toFile());

    Path bagDir = testDir.resolve("loose-bag");
    Bag bag = Bagger.bagInPlace(bagDir);

    bag = Bagger.readBag(bagDir);
    System.out.println(bag);

    Files.write(bagDir.resolve("data").resolve("foo3.txt"), "foo3".getBytes(StandardCharsets.UTF_8));
    bag = Bagger.reBag(bag);
    System.out.println(bag);
  }

}