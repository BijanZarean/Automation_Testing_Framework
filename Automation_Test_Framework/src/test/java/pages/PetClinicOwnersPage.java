package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.Driver;
import utilities.ElementActions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class PetClinicOwnersPage {
    private final WebDriverWait wait;
    private final WebDriver driver;
    private final ElementActions action;

    public PetClinicOwnersPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        action = new ElementActions(Driver.getDriver());
    }

    //Owners find page
    private final By ownersTitle =
            By.xpath("//h2");

    private final By findOwnerBtn =
            By.xpath("//button[@type='submit']");

    private final By ownersTable =
            By.xpath("//table");

    private final By nameColumnHeader =
            By.xpath("//th[text()='Name']");

    private final By findOwnerLastNameField =
            By.xpath("//input[@id='lastName']");

    //Owners add page
    private final By addOwnerBtn =
            By.xpath("//button[text()='Add Owner']");

    private final By addOwnerFirstNameField =
            By.xpath("//input[@id='firstName']");

    private final By addOwnerLastNameField =
            By.xpath("//input[@id='lastName']");

    private final By addOwnerAddressField =
            By.xpath("//input[@id='address']");

    private final By addOwnerCityField =
            By.xpath("//input[@id='city']");

    private final By addOwnerPhoneField =
            By.xpath("//input[@id='telephone']");

    //Get table count
    public int getOwnerRowCount() {
        return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(ownersTable, 0)).size();
    }

    //Add table elements into a list
    public List<WebElement> ownersTableRowsFullName() {
        WebElement tableElement = wait.until(ExpectedConditions.visibilityOfElementLocated(ownersTable));
        List<WebElement> tableRowNameElements = new ArrayList<>(tableElement.findElements(By.cssSelector("td.ownerFullName")));
        return tableRowNameElements;
    }
    public List<WebElement> ownersTableRowsAddress() {
        WebElement tableElement = wait.until(ExpectedConditions.visibilityOfElementLocated(ownersTable));
        List<WebElement> tableRowAddressElements = new ArrayList<>(tableElement.findElements(By.cssSelector("tr td:nth-child(2)")));
        return tableRowAddressElements;
    }
    public List<WebElement> ownersTableRowsCity() {
        WebElement tableElement = wait.until(ExpectedConditions.visibilityOfElementLocated(ownersTable));
        List<WebElement> tableRowCityElements = new ArrayList<>(tableElement.findElements(By.cssSelector("tr td:nth-child(3)")));
        return tableRowCityElements;
    }
    public List<WebElement> ownersTableRowsPhoneNumber() {
        WebElement tableElements = wait.until(ExpectedConditions.visibilityOfElementLocated(ownersTable));
        List<WebElement> tableRowPhoneElements = new ArrayList<>(tableElements.findElements(By.cssSelector("tr td:nth-child(4)")));
        return  tableRowPhoneElements;
    }
    //is displayed?
    public boolean isOwnersTitleDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(ownersTitle)).isDisplayed();
    }
    public boolean isOwnersAddBtnDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(addOwnerBtn)).isDisplayed();
    }
    public boolean isOwnerFirstNameDisplayed(String firstName) {
        firstName = firstName.toLowerCase();
        List<WebElement> rowsList = ownersTableRowsFullName();
        for (WebElement element : rowsList) {
            if (element.getText().toLowerCase().trim().contains(firstName)) {
                return true;
            }
        }
        System.out.println(firstName+" does not appear in the Owners table!");
        return false;
    }
    public boolean isOwnerLastNameDisplayed(String lastName) {
        lastName = lastName.toLowerCase();
        List<WebElement> rowsList = ownersTableRowsFullName();
        for (WebElement element : rowsList) {
            if(element.getText().toLowerCase().trim().contains(lastName)) {
                return true;
            }
        }
        System.out.println(lastName+" does not appear in the Owners table!");
        return false;
    }
    public boolean isOwnerAddressDisplayed(String address) {
        address = address.toLowerCase();
        List<WebElement> rowsList = ownersTableRowsAddress();
        for(WebElement element : rowsList) {
//            System.out.println(element.getText().toLowerCase());
            if(element.getText().toLowerCase().contains(address)) {
                return true;
            }
        }
        System.out.println(address+" does not appear in the Owners table!");
        return false;
    }
    public boolean isOwnerCityDisplayed(String city) {
        city = city.toLowerCase();
        List<WebElement> rowsList = ownersTableRowsCity();
        for(WebElement element : rowsList) {
            if(element.getText().toLowerCase().trim().contains(city)) {
                return true;
            }
        }
        System.out.println(city+" does not appear in the Owners table!");
        return false;
    }
    public boolean isOwnerPhonedisplayed(String phone) {
        List<WebElement> rowsList = ownersTableRowsPhoneNumber();
        for(WebElement element : rowsList) {
            if(element.getText().trim().contains(phone)) {
                return true;
            }
        }
        System.out.println(phone+" does not appear in the Owners table!");
        return false;
    }
    public boolean isAnyOwnerNameDisplayed() {
        List<WebElement> rowsList = ownersTableRowsFullName();
        for (WebElement element : rowsList) {
            String name = element.getText();
            if (!name.isEmpty()) {
                System.out.println(name);
            } else {
                System.out.println("Owner row is empty");
                return false;
            }
        } return true;

    }

    //Click on element
    public void clickOnFindOwnerBtn() throws InterruptedException {
        Thread.sleep(1000);
        for(int i=0; i<5; i++) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(findOwnerBtn)).click();
            try{ wait.until(ExpectedConditions.visibilityOfElementLocated(nameColumnHeader));
                return;} catch (TimeoutException e) {
            }} throw new RuntimeException("Owner table never became visible after clicking Find Owner");}
    public void clickOnAddOwnerBtn() throws InterruptedException {
        Thread.sleep(1000);
        for(int i=0; i<5; i++) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(addOwnerBtn)).click();
            try{ wait.until(ExpectedConditions.visibilityOfElementLocated(findOwnerBtn));
                return;} catch (TimeoutException e) {
            }} throw new RuntimeException("Search owner button never became visible after clicking Add Owner button");}

    //Search for an owner
    public void searchForAnOwnerLastName(String lastName) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(findOwnerLastNameField)).sendKeys(lastName);
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(findOwnerBtn)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(findOwnerBtn)).click();
    }

    //Add new owner
    public void createNewOwner(String firstName, String lastName, String address, String city, String phone) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addOwnerFirstNameField)).sendKeys(firstName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(addOwnerLastNameField)).sendKeys(lastName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(addOwnerAddressField)).sendKeys(address);
        wait.until(ExpectedConditions.visibilityOfElementLocated(addOwnerCityField)).sendKeys(city);
        wait.until(ExpectedConditions.visibilityOfElementLocated(addOwnerPhoneField)).sendKeys(phone);
        clickOnFindOwnerBtn();
    }























}
