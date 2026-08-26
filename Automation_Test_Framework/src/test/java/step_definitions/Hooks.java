package step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utilities.Driver;

public class Hooks {
    @Before("@ui")
    public void setUpBrowser() {
        Driver.getDriver();
    }

    @After("@ui")
    public void tearDownBrowser(Scenario scenario) {
        try {
            if (scenario.isFailed() && Driver.isInitialized()) {
                byte[] screenshot =
                        ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure screenshot");
            }
        } finally {
            Driver.quitDriver();
        }
    }
}
