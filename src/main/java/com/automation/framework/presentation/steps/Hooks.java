package com.automation.framework.presentation.steps;

import com.automation.framework.infrastructure.driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.ByteArrayInputStream;

public class Hooks {

    @Before
    public void beforeScenario() {
        String browser = System.getProperty("browser", "chrome");
       // String browser = System.getProperty("browser", "edge");
        DriverFactory.init(browser);
    }

    @After
    public void afterScenario(Scenario scenario) {
        WebDriver driver = DriverFactory.get();
        if (scenario.isFailed() && driver != null) {
            Allure.addAttachment("screenshot_falha_allure", "image/png",
                    new ByteArrayInputStream(
                            ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)
                    ), ".png");
        }
        DriverFactory.quit();
    }
}