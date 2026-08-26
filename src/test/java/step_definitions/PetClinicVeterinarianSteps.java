package step_definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.PetClinicHomePage;
import pages.PetClinicVetPage;
import utilities.Driver;

public class PetClinicVeterinarianSteps {
    private PetClinicHomePage homePage;
    private PetClinicVetPage vetPage;

    @When("I open the Veterinarians page")
    public void i_open_the_veterinarians_page() throws InterruptedException {
        homePage = new PetClinicHomePage(Driver.getDriver());
        homePage.navigateToVeterinarian();
        vetPage = new PetClinicVetPage(Driver.getDriver());
    }
    @Then("the Veterinarians page should be displayed")
    public void the_veterinarians_page_should_be_displayed() {
        Assert.assertTrue(vetPage.isVetHomePageTitleDisplayed());
    }
    @Then("at least one veterinarian should be listed")
    public void at_least_one_veterinarian_should_be_listed() {
        Assert.assertTrue(vetPage.isVetTableDisplayed());
    }
}
