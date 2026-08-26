package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PetClinicVetPage {
private final WebDriverWait wait;

public PetClinicVetPage(WebDriver driver) {
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
}

private final By veterinariansHomePageTitle =
        By.xpath("//h2");

private final By vetTable =
        By.xpath("//Doesnotexist");

//isDisplayed?
    public boolean isVetHomePageTitleDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(veterinariansHomePageTitle)).isDisplayed();
    }

    public boolean isVetTableDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(vetTable)).isDisplayed();
    }
}
