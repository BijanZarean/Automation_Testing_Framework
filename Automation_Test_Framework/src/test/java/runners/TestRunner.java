package runners;
//Configures where cucumber finds test and reports results:
import io.cucumber.testng.CucumberOptions;
//This base class connects Cucumber scenarios to TestNG. Surfire launches the TestNG runner, and the inherited
//functionality turns Cucumber scenarios into TestNG executable tests.
import io.cucumber.testng.AbstractTestNGCucumberTests;
@CucumberOptions(
        //telling Cucumber where the .feature files are located:
        features = "src/test/resources/features",
        //Telling Cucumber where the Step Definitions, Hooks, and other Cucumber glue code are:
        glue = "step_definitions",

        plugin = {
                //Prints readable scenario execution output to the console:
                "pretty",
                //Creates a human-readable HTML report:
                "html:target/cucumber-reports/cucumber.html",
                //Creates machine-readable JSON results that can be consumed by reporting tools:
                "json:target/cucumber-reports/cucumber.json",
                //Creates JUnit style XML output. Jenkins understands this widely used test results format:
                "junit:target/cucumber-reports/cucumber.xml"
        },
        //Makes console output cleaner by reducing unreadable formatting or control characters:
        monochrome = true,
        //Prevents Cucumber from publishing results to its external report sharing service so results remain local:
        publish = false
)
/**
 * We are using TestNG (not J-Unit) so we will need to extend AbstractTestNGCucumberTests.
 * We are not hardcoding a tag in this runner because that would mean changing Java code every time we want
 * to run a different suite.
 * Instead, we will use a cli command such as:
 * -Dcucumber.filter.tags="@smoke"
 */
public class TestRunner extends AbstractTestNGCucumberTests {

}
