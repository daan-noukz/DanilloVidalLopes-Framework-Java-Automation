package com.automation.framework.infrastructure.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    private static final List<WebDriver> allDrivers = Collections.synchronizedList(new ArrayList<>());

    static {
        // Registers a new thread that will be executed when the JVM is about to shut down.
        // This captures abrupt terminations (e.g., Ctrl+C in the terminal).
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown Hook triggered: closing remaining drivers...");
            for (WebDriver driver : allDrivers) {
                if (driver != null) {
                    try {
                        driver.quit();
                    } catch (Exception e) {
                        System.err.println("Error closing driver in shutdown hook: " + e.getMessage());
                    }
                }
            }
        }));
    }

    /**
     * Initializes the WebDriver for the current thread based on the specified browser.
     * Maintains its original logic for Chrome and Edge.
     */
    public static void init(String browser) {
        WebDriver driver; // Local variable to create the instance

        switch (browser.toLowerCase()) {
            case "edge":
                // Maintaining the absolute path to msedgedriver
                String driverPath = System.getProperty("user.dir") + "/drivers/msedgedriver";
                System.setProperty("webdriver.edge.driver", driverPath);
                driver = new EdgeDriver();
                break;
            default: // "chrome" or any other value
                // Maintaining automatic management of chromedriver
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                driver = new ChromeDriver(options);
        }

        // Stores the driver in the current thread's ThreadLocal
        threadLocalDriver.set(driver);
        // Adds the driver to the global list for the Shutdown Hook
        allDrivers.add(driver);
    }

    /**
     * Returns the WebDriver instance associated with the current thread.
     */
    public static WebDriver get() {
        return threadLocalDriver.get();
    }

    /**
     * Closes the WebDriver for the current thread and removes it from the tracking lists.
     * MUST be called at the end of each test (e.g., in an @AfterEach).
     */
    public static void quit() {
        WebDriver driver = threadLocalDriver.get();
        if (driver != null) {
            try {
                // 1. Closes the browser and the driver process.
                driver.quit();
            } finally {
                // 2. Removes the driver from the ThreadLocal to clean up the thread.
                threadLocalDriver.remove();
                // 3. Removes the driver from the global list, as it was closed successfully.
                // This prevents the Shutdown Hook from trying to close it again.
                allDrivers.remove(driver);
            }
        }
    }
}