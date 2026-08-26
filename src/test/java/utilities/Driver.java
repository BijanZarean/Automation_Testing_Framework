package utilities;
//Represents language and regional formatting rules:
import java.util.Locale;
//Selenium's primary browser-control INTERFACE. It declares methods such as get(),findElement(), getTitle(), quit():
import org.openqa.selenium.WebDriver;
//represents a width and height of the browser size:
import org.openqa.selenium.Dimension;
//Controls how long WebDriver waits when navigating to a page (NORMAL, EAGER, NONE):
import org.openqa.selenium.PageLoadStrategy;
//The concrete WebDriver implementation that controls Google Chrome:
import org.openqa.selenium.chrome.ChromeDriver;
//Holds the configuration Selenium sends when starting a Chrome session:
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
/*
Creates, returns and closes the Selenium WebDriver instance.
Supported browsers: chrome, firefox, edge, and safari.
Headless execution is supported for chrome, firefox, and edge.
 */
//final so another class cannot extend this class. Inheritance is not necessary.
public final class Driver {
    /*
    A consistent browser size helps prevent responsive-layout
    differences between local and Jenkins executions.
     */
    private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(1920, 1080);
    /*
    WARNING, this class only stores one static WebDriver object.
    This is okay for executing tests sequentially but is not thread safe for parallel browser execution,
    because multiple test threads would share and overwrite the same driver.
    When enabling parallel execution in testNG or Jenkins, replace the single static field below with:
    ThreadLocal<WebDriver>
     */
    /*
    Stores the browser session currently used by the test.
    WebDriver is the interface type. The actual object may be a
    ChromeDriver, FirefoxDriver, EdgeDriver, or SafariDriver.
    Encapsulation; set as private so no class can overwrite it, forcing all browser access through controlled methods:
     */
    private static WebDriver driver;
    /*
    Prevents other classes from creating Driver objects.
    This class is used through static methods:
    Driver.getDriver() Driver.quitDriver()
     */
    private Driver(){
    }
    /*
    Returns the existing WebDriver session.
    If no browser has been created yet, this method creates one first.
     */
    public static WebDriver getDriver() {
        if (driver == null) {
            //starts the configured browser:
            driver = createDriver();
        }
        return driver;
    }
    /*
    Reports whether this class currently holds a WebDriver reference.
    This does not perform a browser health check. It ony determines whether the driver variable is non-null.
    Useful in the failure-screenshot hook:
     */
    public static boolean isInitialized() {
        return driver != null;
    }
    /*
    Closes every browser window in the current WebDriver session,
    ends the driver process, and resets the stored reference.
     */
    public static void quitDriver() {
        //Calling an instance method on null would cause NullPointerException:
        if (driver != null) {
            try {
                //ends the complete WebDriver session and closes every browser window opened by that session:
                driver.quit();
                //finally runs whether quit() succeeds or throws an exception:
            } finally {
                //Resetting the driver variable allows the next scenario to create a fresh browser.
                //guarantees that the framework does not retain a reference to an invalid or partially closed session:
                driver = null;
            }
        }
    }
    /*
    Looks for a setting in this order:
    1. Maven/Java system property (Maven's -D option: mvn clean test -Dbrowser=firefox -dheadless=true)
    2. env.properties file
     */
    private static String getSetting(String name) {
        String systemProperty = System.getProperty(name);
        if(systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty.trim();
        }
        return DataReader.get(name);
    }
    /*
    Reads and validates a true/false configuration value. This is for if headless mode is set to true/false
     */
    private static boolean getBooleanSetting(String name){
        String value = getSetting(name);
        if(!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            throw new IllegalArgumentException("Configuration setting '"+name+"' must be either true or false, " +
                    "but was: "+value);
        }
        return Boolean.parseBoolean(value);
    }

    //Browser options methods help keep the createDriver() method readable:
    /*
    Creates the options used when starting Chrome
     */
    private static ChromeOptions createChromeOptions(boolean headless) {
        //Chrome has not started yet, we are constructing the desired startup configuration:
        ChromeOptions options = new ChromeOptions();
        //This tells WebDriver navigation commands to wait for the normal document-ready state:
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        if (headless) {
            options.addArguments("--headless=new");
        }
        return options;
    }
    /*
    Creates the options used when starting FireFox
     */
    private static FirefoxOptions createFireFoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        if(headless) {
            options.addArguments("--headless");
        }
        return options;
    }
    /*
    Creates the options used when starting Microsoft Edge
     */
    private static EdgeOptions createEdgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        if(headless) {
            options.addArguments("--headless=new");
        }
        return options;
    }
    /*
    Creates SafariDriver.
    safari does not use the same Chromium/Forefox headless arguments,
    so this framework rejects a request for headless safari
     */
    private static WebDriver createSafariDriver(boolean headless) {
        if(headless) {
            throw new IllegalArgumentException("Headless mode is not supported by this framework" +
                    "for Safari. Set headless=false when browser=Safari.");
        }
        SafariOptions options = new SafariOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        return new SafariDriver(options);
    }
    /*
    Reads the browser configuration and creates the appropriate WebDriver implementation.
    Private because only getDriver() should control when creation occurs
     */
    private static WebDriver createDriver() {
        //'Locale.ROOT' does language-neutral conversion, avoiding unusual capitalization behavior caused by the operating system's language settings
        String browser = getSetting("browser").toLowerCase(Locale.ROOT);
        boolean headless = getBooleanSetting("headless");
        WebDriver newDriver = switch (browser) {
            case "chrome" -> new ChromeDriver(createChromeOptions(headless));
            case "firefox" -> new FirefoxDriver(createFireFoxOptions(headless));
            case "edge" -> new EdgeDriver(createEdgeOptions(headless));
            case "safari" -> createSafariDriver(headless);
            default -> throw new IllegalArgumentException("Unsupported browser: "+browser+". " +
                    "Supported values are: chrome, firefox, edge, and safari.");
        };
        //manage() provides access to browser level management functions.
        //window() selects window management operations.
        //setSize() applies the 1920x1080 dimensions.
        newDriver.manage().window().setSize(DEFAULT_WINDOW_SIZE);
        return newDriver;
    }
}