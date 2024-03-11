package edu.colorado.cires.cruisepack.bag;

import edu.colorado.cires.cruisepack.prototype.bag.Bagger;
import gov.loc.repository.bagit.domain.Bag;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class BaggerTest {

  private Path testDir = Paths.get("target/bagger-test");
  private Bagger bagger = new Bagger(
      Collections.singletonList(StandardSupportedAlgorithms.SHA256)
  );

  @BeforeEach
  public void beforeEach() throws Exception{
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
    Bag bag = bagger.bagInPlace(bagDir);

    bag = bagger.readBag(bagDir);
    System.out.println(bag);

    Files.write(bagDir.resolve("data").resolve("foo3.txt"), "foo3".getBytes(StandardCharsets.UTF_8));
    bag = bagger.reBag(bag);
    System.out.println(bag);
  }

}