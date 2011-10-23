package de.fnordeingang.fnordapp.test;

import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * User: vileda
 * Date: 22.10.11
 * Time: 18:10
 */
public class AllTests extends TestSuite {
  public static Test suite() {
      return new TestSuiteBuilder(AllTests.class).includeAllPackagesUnderHere().build();
  }
}
