package com.arch;

import static io.quarkus.arc.ComponentsProvider.LOG;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(RunWithStaging.class)
public class StagingTest {

  @Test
  void testStaging() {
    LOG.info("Staging test");
  }
}
