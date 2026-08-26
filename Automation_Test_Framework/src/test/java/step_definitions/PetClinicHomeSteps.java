/**
 * Cucumber reads steps from the .feature file, finds matching annotated Java methods, extracts any parameters, and
 * invokes those methods.
 */
package step_definitions;
//Cucumber annotations that connect the Gherkin scenario statements from the .feature file to the step definition methods:
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
//Importing the page class to interact with or inspect the Petclinic home page:
import pages.PetClinicHomePage;
//Allows the step to retrieve the application URL without hardcoding it directly into the step definition class:
import utilities.DataReader;
//This gives the step access to the framework managed WebDriver session:
import utilities.Driver;
//These import the static assertTrue() and assertEquals() methods directly:
import static org.testng.Assert.*;


public class PetClinicHomeSteps {
/*
Making the homePage object an instance field allows multiple step methods in the same scenario to share it.
The Given method creates the page object, and the Then method uses it.
 */
    private PetClinicHomePage homePage;

    @Given("I open the Petclinic application")
    public void i_open_the_petclinic_application() throws InterruptedException {
        String baseURL = DataReader.get("base.url");
        Driver.getDriver().get(baseURL);
//We create the page object after opening the browser and navigating to the loaded page represented by the object:
        homePage = new PetClinicHomePage(Driver.getDriver());
        System.out.println("Petclinic application opened");
        Thread.sleep(3000);
    }
    @Then("the Home and Owners navigation links should be displayed")
    public void the_home_and_owners_navigation_links_should_be_displayed() {
        assertTrue(homePage.isHomeLinkDisplayed(), "The home navigation link was not displayed!");
        assertTrue(homePage.isOwnersDrpdwnDisplayed(), "the owners dropdown link was not displayed!");
//        assertFalse(homePage.isOwnersDrpdwnDisplayed(), "the owners dropdown link was not displayed!");
    }
    @Then("the page heading should be {string}")
    public void the_page_heading_should_be(String expectedHeading) {
        assertEquals(homePage.getPageHeading(), expectedHeading);
    }
}
