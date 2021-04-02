package com.cybertek.library1.runners;


import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludePackages;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuiteDisplayName("Smoke Tests")
@IncludePackages("com.cybertek.library1.testCases")
@IncludeTags("smoke")
public class SmokeTests_Runner {}
