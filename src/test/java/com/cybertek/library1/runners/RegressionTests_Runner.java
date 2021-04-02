package com.cybertek.library1.runners;


import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

// this is JUnit 4 runner. It needs to be correctly setup to recognize JUnit5 Tests
@RunWith(JUnitPlatform.class)
@SuiteDisplayName("Regression Tests")
@SelectPackages("com.cybertek.library1.testCases")
@IncludeTags("regression")
public class RegressionTests_Runner {
}
