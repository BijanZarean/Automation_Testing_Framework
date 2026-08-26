package step_definitions;

import io.cucumber.java.en.Then;
import org.testng.Assert;
import pages.PetClinicHomePage;
import utilities.Driver;

import java.util.ArrayList;
import java.util.List;


public class PetClinicNavigationSteps {
    private PetClinicHomePage homePage;

    @Then("the main navigation should contain:")
    public void the_main_navigation_should_contain(io.cucumber.datatable.DataTable dataTable) throws InterruptedException {
        homePage = new PetClinicHomePage(Driver.getDriver());
        //Cucumber's dataTable.asList is immutable so we copy it into an arrayList:
        List<String> expectedLinks = new ArrayList<>(dataTable.asList());
        expectedLinks.replaceAll(String::toLowerCase);
//        System.out.println("Getting navigation links!");
//        for (String link : expectedLinks) {
//            System.out.println(link);
//        }
//        for (String link : homePage.getHomeLinks()) {
//            System.out.println(link);
//        }
//        Thread.sleep(3000);
        Assert.assertEquals(homePage.getHomeLinks(), expectedLinks);
    }
}
