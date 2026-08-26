package step_definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.PetClinicHomePage;
import pages.PetClinicOwnersPage;
import utilities.DBUtils;
import utilities.DataReader;
import utilities.Driver;
import utilities.TestDataGenerator;

public class PetClinicOwnersSteps {
    private PetClinicHomePage homePage;
    private PetClinicOwnersPage ownersPage;

    @When("I open the Owners page")
    public void i_open_the_owners_page() throws InterruptedException {
        homePage = new PetClinicHomePage(Driver.getDriver());
        homePage.navigateToOwnersSearch();
        Thread.sleep(3000);
    }
    @Then("the Owners page should be displayed")
    public void the_owners_page_should_be_displayed() {
        ownersPage = new PetClinicOwnersPage(Driver.getDriver());
        Assert.assertTrue(ownersPage.isOwnersTitleDisplayed(), "The Owners page title is not displayed");
    }
    @Given("I am on the Owners page")
    public void i_am_on_the_owners_page() {
        String baseURL = DataReader.get("base.url");
        Driver.getDriver().get(baseURL);
        homePage = new PetClinicHomePage(Driver.getDriver());
        ownersPage = new PetClinicOwnersPage(Driver.getDriver());
        homePage.navigateToOwnersSearch();
        Assert.assertTrue(ownersPage.isOwnersTitleDisplayed(), "The Owners page title is not displayed");
    }
    @Then("at least one owner should be listed")
    public void at_least_one_owner_should_be_listed() throws InterruptedException {
        ownersPage.clickOnFindOwnerBtn();
        Assert.assertTrue(ownersPage.getOwnerRowCount()>0, "No owner listed in the table!");
    }
    @Then("every owner row should contain a name and address")
    public void every_owner_row_should_contain_a_name_and_address() throws InterruptedException {
//        Assert.assertTrue(ownersPage.isOwnerNameDisplayed("Bijan Z"));
        Assert.assertTrue(ownersPage.isAnyOwnerNameDisplayed());
        Thread.sleep(3000);
    }
    @Given("I am on the New Owner page")
    public void i_am_on_the_new_owner_page() {
        homePage = new PetClinicHomePage(Driver.getDriver());
        ownersPage = new PetClinicOwnersPage(Driver.getDriver());
        String baseURL = DataReader.get("base.url");
        Driver.getDriver().get(baseURL);
        homePage.navigateToOwnersAdd();
        Assert.assertTrue(ownersPage.isOwnersAddBtnDisplayed(), "The Owners Add button is not displayed.");
    }
    String firstName;
    String lastName;
    String address;
    String city;
    String phoneNumber;
    @When("I create an owner")
    public void i_create_an_owner() throws InterruptedException {
        this.firstName = TestDataGenerator.firstName();
        this.lastName = TestDataGenerator.lastName();
        this.address = TestDataGenerator.address();
        this.city = TestDataGenerator.city();
        this.phoneNumber = TestDataGenerator.phoneNumber();
        ownersPage.createNewOwner(firstName, lastName, address, city, phoneNumber);
    }
    @When("I search for the owner")
    public void i_search_for_the_owner() throws InterruptedException {
//        System.out.println(this.firstName+" "+this.lastName);
        ownersPage.searchForAnOwnerLastName(this.lastName);
        Assert.assertTrue(ownersPage.isOwnerAddressDisplayed(address));
        Assert.assertTrue(ownersPage.isOwnerCityDisplayed(city));
        Assert.assertTrue(ownersPage.isOwnerPhonedisplayed(phoneNumber));
    }
    @Then("every displayed owner should have the correct last name")
    public void every_displayed_owner_should_have_the_correct_last_name() {
        Assert.assertTrue(ownersPage.isOwnerFirstNameDisplayed(this.firstName));
        Assert.assertTrue(ownersPage.isOwnerLastNameDisplayed(this.lastName));
        Assert.assertTrue(DBUtils.ownerExists(firstName, lastName, address, city, phoneNumber));
        Assert.assertTrue(DBUtils.deleteOwner(firstName, lastName, address, city, phoneNumber));
    }
}
