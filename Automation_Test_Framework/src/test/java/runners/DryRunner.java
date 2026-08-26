package runners;
/**
 * This class connects Cucumber to TestNG.
 * Extending it allows Maven Surefire and TestNG to recognize the cucumber scenarios as executable tests
 */
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
//Telling Cucumber where to scan the feature files. It checks every scenario unless a tag filter is added:
        features = "src/test/resources/features",
//The glue setting tells Cucumber where the Java step definitions and hooks are located:
        glue = "step_definitions",
//The pretty format prints readable feature, scenario, and step results in the console:
        plugin = {
                "pretty"
        },
//This makes terminal output easier to read by suppressing some formatting and control characters:
        monochrome = true,
//Prevents Cucumber from offering to publish the execution results to its external report-sharing service:
        publish = false,
//Tells Cucumber to skip glue-code execution while checking whether feature steps can be matched to step definitions:
        dryRun = true
)
//The class body is empty because the inherited TestNG integration performs the actual Cucumber startup.
public class DryRunner extends AbstractTestNGCucumberTests {
}
