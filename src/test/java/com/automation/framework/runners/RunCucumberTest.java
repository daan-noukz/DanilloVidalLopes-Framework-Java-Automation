// Arquivo: src/test/java/com/automation/framework/runners/RunCucumberTest.java

package com.automation.framework.runners;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

// A anotação @ConfigurationParameter foi removida.
// O JUnit agora lerá a configuração do arquivo junit-platform.properties.
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class RunCucumberTest {
}