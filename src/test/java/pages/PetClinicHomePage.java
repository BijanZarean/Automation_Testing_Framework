package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Driver;
import utilities.ElementActions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the services and elements available on the Petclinic home page
 */
public class PetClinicHomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public PetClinicHomePage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private final By homePageTitle =
            By.className("title");

    private final By ownersLink =
            By.xpath("//a[text()=' Owners']");

    private final By homeLink =
            By.xpath("//a[@title='home page']");

    private final By ownersDrpdwnSearchBtn =
            By.xpath("//a[@routerlink='/owners']");

    private final By ownersDrpdwnAddNewBtn =
            By.xpath("//a[@routerlink='/owners/add']");

    private final By veterinariansLink =
            By.xpath("//a[text()=' Veterinarians']");

    private final By veterinariansFindAllLink =
            By.xpath("//a[@routerlink='/vets']");

    private final By petTypesLink =
            By.xpath("//a[@routerlink='/pettypes']");

    private final By specialitiesLink =
            By.xpath("//a[@routerlink='/specialties']");

    /*
    Methods for performing action on page
     */
    //getting text
    public String getPageHeading() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(homePageTitle)).getText();
    }
    public List<String> getHomeLinks(){
        List <String> homeLinks = new ArrayList<>();
        homeLinks.add(wait.until(ExpectedConditions.visibilityOfElementLocated(homeLink)).getText().trim().toLowerCase());
        homeLinks.add(wait.until(ExpectedConditions.visibilityOfElementLocated(ownersLink)).getText().trim().toLowerCase());
        homeLinks.add(wait.until(ExpectedConditions.visibilityOfElementLocated(veterinariansLink)).getText().trim().toLowerCase());
        homeLinks.add(wait.until(ExpectedConditions.visibilityOfElementLocated(petTypesLink)).getText().trim().toLowerCase());
        homeLinks.add(wait.until(ExpectedConditions.visibilityOfElementLocated(specialitiesLink)).getText().trim().toLowerCase());
        return homeLinks;
    }

    //is displayed?
    public boolean isOwnersDrpdwnDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(ownersLink)).isDisplayed();
    }
    public boolean isHomeLinkDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(homeLink)).isDisplayed();
    }

    //Navigation
    public void navigateToVeterinarian() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(veterinariansLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(veterinariansFindAllLink)).click();
    }
    public void navigateToOwnersSearch() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(ownersLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(ownersDrpdwnSearchBtn)).click();
    }
    public void navigateToOwnersAdd() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(ownersLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(ownersDrpdwnAddNewBtn)).click();
    }
}
