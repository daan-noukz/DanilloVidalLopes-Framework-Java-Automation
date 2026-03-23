package com.automation.framework.infrastructure.wrappers;

import com.google.common.io.Files;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * A wrapper class for Selenium WebDriver actions.
 * Provides robust methods for interacting with web elements, incorporating explicit waits
 * to handle synchronization issues and improve test stability.
 * This class centralizes Selenium logic, making test scripts cleaner and more maintainable.
 */
public class WebActions {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;
    private final JavascriptExecutor jsExecutor;

    /**
     * Constructor for WebActions.
     *
     * @param driver The WebDriver instance to be used for all actions.
     * @param timeoutInSeconds The default timeout in seconds for all explicit waits.
     */
    public WebActions(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        this.actions = new Actions(driver);
        this.jsExecutor = (JavascriptExecutor) driver;
    }

    //region 1. Ações Básicas em Elementos (WebElement)

    /**
     * Waits for an element to be clickable and then clicks it.
     *
     * @param locator The locator used to find the element.
     */
    public void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /**
     * Clears the text from an input field and then sends the specified keys.
     *
     * @param locator The locator used to find the element.
     * @param text    The text to be sent to the element.
     */
    public void sendKeys(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Clears the text from an input field.
     *
     * @param locator The locator used to find the element.
     */
    public void clear(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).clear();
    }

    /**
     * Gets the visible inner text of an element.
     *
     * @param locator The locator used to find the element.
     * @return The inner text of the element.
     */
    public String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    /**
     * Checks if an element is displayed on the page.
     *
     * @param locator The locator used to find the element.
     * @return True if the element is displayed, false otherwise.
     */
    public boolean isDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Gets the value of a given attribute of the element.
     *
     * @param locator The locator used to find the element.
     * @param attributeName The name of the attribute.
     * @return The attribute's current value or null if the value is not set.
     */
    public String getAttribute(By locator, String attributeName) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getAttribute(attributeName);
    }

    //endregion

    //region 2. Localização de Elementos (Find)

    /**
     * Finds a single web element after waiting for its presence.
     *
     * @param locator The locator used to find the element.
     * @return The first matching WebElement.
     */
    public WebElement findElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Finds all web elements for a given locator after waiting for their presence.
     *
     * @param locator The locator used to find the elements.
     * @return A list of all matching WebElements.
     */
    public List<WebElement> findElements(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    //endregion

    //region 3. Navegação (Navigation)

    /**
     * Navigates the browser to a specific URL.
     *
     * @param url The URL to navigate to.
     */
    public void navigateToUrl(String url) {
        driver.get(url);
    }

    /**
     * Navigates back in the browser's history.
     */
    public void navigateBack() {
        driver.navigate().back();
    }

    /**
     * Navigates forward in the browser's history.
     */
    public void navigateForward() {
        driver.navigate().forward();
    }

    /**
     * Refreshes the current page.
     */
    public void refreshPage() {
        driver.navigate().refresh();
    }

    //endregion

    //region 4. Gerenciamento de Browser

    /**
     * Maximizes the current browser window.
     */
    public void maximizeWindow() {
        driver.manage().window().maximize();
    }

    //endregion

    //region 5. Frames / iFrames

    /**
     * Switches to a frame by its index.
     *
     * @param index The index of the frame to switch to.
     */
    public void switchToFrame(int index) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
    }

    /**
     * Switches to a frame by its name or ID.
     *
     * @param nameOrId The name or ID of the frame.
     */
    public void switchToFrame(String nameOrId) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(nameOrId));
    }

    /**
     * Switches back to the main document from a frame.
     */
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    //endregion

    //region 6. Alerts

    /**
     * Accepts the currently displayed JavaScript alert.
     */
    public void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    /**
     * Dismisses the currently displayed JavaScript alert.
     */
    public void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).dismiss();
    }

    /**
     * Gets the text from the currently displayed JavaScript alert.
     *
     * @return The text of the alert.
     */
    public String getAlertText() {
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }

    //endregion

    //region 7. Tabs / Janelas

    /**
     * Switches focus to a new window/tab that has opened.
     */
    public void switchToNewWindow() {
        String originalWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();

        // Wait for a new window to appear
        wait.until(ExpectedConditions.numberOfWindowsToBe(allWindows.size() + 1));

        // Find the new window handle and switch
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }

    /**
     * Closes the current browser window or tab.
     */
    public void closeWindow() {
        driver.close();
    }

    //endregion

    //region 8. Actions API (Interações Avançadas)

    /**
     * Moves the mouse to the middle of the element. Simulates a hover effect.
     *
     * @param locator The locator used to find the element to hover over.
     */
    public void hover(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        actions.moveToElement(element).perform();
    }

    /**
     * Performs a drag-and-drop operation from a source element to a target element.
     *
     * @param sourceLocator The locator of the element to drag.
     * @param targetLocator The locator of the element to drop onto.
     */
    public void dragAndDrop(By sourceLocator, By targetLocator) {
        WebElement source = findElement(sourceLocator);
        WebElement target = findElement(targetLocator);
        actions.dragAndDrop(source, target).perform();
    }

    /**
     * Sends a specific keyboard key to an element.
     *
     * @param locator The locator of the target element.
     * @param key     The keyboard key to press (e.g., Keys.ENTER).
     */
    public void sendKeys(By locator, Keys key) {
        findElement(locator).sendKeys(key);
    }

    /**
     * Simulates pressing the Enter key on a specific element.
     * Useful for submitting forms or triggering search actions.
     *
     * @param locator The locator of the element to press Enter on.
     */
    public void pressEnter(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator)).sendKeys(Keys.ENTER);
    }

    //endregion

    //region 9. JavaScript Executor

    /**
     * Scrolls the page until the specified element is in view.
     *
     * @param locator The locator of the element to scroll to.
     */
    public void scrollToElement(By locator) {
        WebElement element = findElement(locator);
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Clicks an element using JavaScript. Useful for elements that are hidden or overlapped.
     *
     * @param locator The locator of the element to click.
     */
    public void jsClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        jsExecutor.executeScript("arguments[0].click();", element);
    }

    //endregion

    //region 11. Select (Dropdown)

    /**
     * Selects an option from a dropdown by its visible text.
     *
     * @param locator The locator of the <select> element.
     * @param visibleText The visible text of the option to select.
     */
    public void selectFromDropdownByVisibleText(By locator, String visibleText) {
        Select dropdown = new Select(findElement(locator));
        dropdown.selectByVisibleText(visibleText);
    }

    /**
     * Selects an option from a dropdown by its 'value' attribute.
     *
     * @param locator The locator of the <select> element.
     * @param value The value attribute of the option to select.
     */
    public void selectFromDropdownByValue(By locator, String value) {
        Select dropdown = new Select(findElement(locator));
        dropdown.selectByValue(value);
    }

    //endregion

    //region 12. Screenshots

    /**
     * Takes a screenshot of the current page as a byte array.
     * Ideal for attaching to reports like Allure.
     *
     * @return Byte array of the screenshot image.
     */
    public byte[] takeScreenshotAsBytes() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Takes a screenshot of the current page and saves it as a file.
     *
     * @param filePath The full path where the screenshot file should be saved (e.g., "screenshots/error.png").
     * @return The saved File object.
     */
    public File takeScreenshotAsFile(String filePath) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            Files.copy(screenshot, new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save screenshot to: " + filePath, e);
        }
        return new File(filePath);
    }

    //endregion

    //region 18. Upload de Arquivos

    /**
     * Uploads a file by sending the file path to a file input element.
     * The input element should be of type="file".
     *
     * @param locator The locator of the file input element.
     * @param absoluteFilePath The absolute path of the file to be uploaded.
     */
    public void uploadFile(By locator, String absoluteFilePath) {
        // For file uploads, the element should not be clicked. We just send keys to it.
        // It must be present, but not necessarily visible.
        driver.findElement(locator).sendKeys(absoluteFilePath);
    }

    //endregion

    //region 20. Captura de URL e Título

    /**
     * Gets the URL of the current page.
     *
     * @return The current URL as a String.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Gets the title of the current page.
     *
     * @return The current title as a String.
     */
    public String getTitle() {
        return driver.getTitle();
    }

    //endregion
}