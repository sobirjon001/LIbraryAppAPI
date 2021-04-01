package com.cybertek.library1.runners;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;
import static org.junit.platform.launcher.TagFilter.includeTags;

public class RegressionTests_Launcher {

  public static void main(String[] args) {
    LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
            .selectors(
                    selectPackage("com.cybertek.library1.testCases")
            )
            .filters(
                    includeClassNamePatterns(".*Tests"),
                    includeTags("regression")
            )
            .build();
    Launcher launcher = LauncherFactory.create();

    SummaryGeneratingListener listener = new SummaryGeneratingListener();
    launcher.registerTestExecutionListeners(listener);

    launcher.execute(request);

    TestExecutionSummary summary = listener.getSummary();

    System.out.println("summary.getTestsFoundCount() = " + summary.getTestsFoundCount());
    System.out.println("summary.getTestsSucceededCount() = " + summary.getTestsSucceededCount());
  }
}
