package edu.colorado.cires.cruisepack.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(
   webEnvironment = WebEnvironment.NONE,
    classes = {
       PackerService.class,
        ObjectMapper.class
    }
)
@Disabled
public class PackerServiceTest {

  private Path mainBagRootDir = Paths.get("target/TST200400");

  @BeforeEach
  public void beforeEach() throws Exception{
    FileUtils.deleteQuietly(mainBagRootDir.toFile());
    Files.createDirectories(mainBagRootDir.resolve("data"));
  }

  @Autowired
  private PackerService packerService;

  @Test
  public void test() throws Exception {
//    PackJob packJob = new PackJob();
//    packJob.setStartTime(Instant.now());
//    packJob.setPackageId("TST200400");
//    packJob.setMasterBagName("TST200400");
//    packJob.setBagPath(mainBagRootDir);
//    packJob.setInstruments(instruments);
//    packJob.setCruisePackDataDir(Paths.get("../sample-work-dir"));
//    packJob.setCruiseMetadata(cruiseMetadata);
////    private Path docsDir;
////    private Path omicsFile;
//    packerService.startPacking(null);
  }

}