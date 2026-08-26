package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ElementActions {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void hover(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        new Actions(driver).moveToElement(element).perform();
    }

    public void doubleClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        new Actions(driver).doubleClick(element).perform();
    }

    public void rightClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        new Actions(driver).contextClick().perform();
    }

    public void dragAndDrop(WebElement elementSource, WebElement elementTarget) {
        wait.until(ExpectedConditions.elementToBeClickable(elementSource));
        wait.until(ExpectedConditions.elementToBeClickable(elementTarget));
        new Actions(driver).dragAndDrop(elementSource, elementTarget).perform();
    }
}
