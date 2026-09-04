# Automation Testing Framework
A Java based test automation framework designed for end to end, API, and database testing.  
The framework currently uses the **Spring Petclinic** application as its test application and is structured to support reusable automated testing across the UI, REST API, and My SQL database layers.

## Technology Stack
Java 21 - Framework programming Language.  
Maven - Dependency management and test execution.  
Selenium WebDriver - Browser automation.  
Cucumber - BDD feature files and step definitions.  
Gherkin - Human readable test scenarios.  
TestNG - Test execution and assertions.  
REST Assured - REST API testing.  
MySQL Connector/J - JDBC database connectivity.  
DataFaker - Dynamic test data generation.  
Maven Surefire - Maven test execution CI compatible results.  
JSON Schema Validator - API response schema validation.  

## Current Dependency Versions
Java: 21  
Selenium: 4.46.0  
Cucumber: 7.34.4  
TestNG: 7.12.0  
REST Assured: 6.0.1  
MySQL Connector/J: 9.7.0  
DataFaker: 2.7.0  
Maven Compiler Plugin: 3.15.0  
Maven Surefire Plugin: 3.5.6  

# Framework Capabilities
The framework is designed to support:  
- Selenium WebDriver UI automation.  
- Page Object Model design.  
- Cucumber BDD scenarios.  
- TestNG execution and assertions.  
- Cucumber tag based test selection.
- Cross browser execution.
- Headless browser execution.
- Automatic screenshots on failed UI scenarios.
- REST API automation with REST Assured.
- JSON schema validation.
- JDBC/MySQL database validation.
- Dynamic test data generation with DataFaker.
- Maven execution profiles.
- Cucumber HTML, JSON, and JUnit XML reports.
- CI compatible Maven/Surfire execution.
- Cucumber dry run validation for missing step definitions.

# Project Structure:
Tools Used for UI Automation Testing  
- Maven: Project, build, and run configurations as well as content, library, and dependencies management.  
- Cucumber: Is used to define feature scenarios in gherkin language and to create test suites and execution flow through tagging.  
- Selenium: Used to automate the browser for UI testing of the web application by implementing the step definitions and managing the page objects.  
- WebDriverManager: Used to manage the browser driver binary and auto downloading and setting up the browser drivers.  
- TestNG: Used to execute the Cucumber scenarios with Cucumber options and assertions within step definitions.  
- JDBC: Used to establish a connection with databases for testing activities.  
- MySQL driver: Used to allow connection to MySQL database that the application uses.  

Tools Used for API Automation Testing  
- TestNG: Used for test execution flow and assertions, manage API test suites, and reporting.  
- RestAssured: Used to define API tests.  
- JDBC: Used to establish a connection with databases for testing activities.  
- MySQL driver: Used to allow connection to MySQL database that the application uses.  

Other tools used for end 2 end testing  
- Git: Version control and source code management.  
- GitHub: Version control platform and remote source code management.  
- Jenkins: CI/CD tool used for running the test suites.  
- IntelliJ: Integrated development environment used for project development.

# Framework Architecture  
The UI test flow follows this structure:  
Feature file  
↓  
Cucumber Step Definition  
↓  
Selenium WebDriver  
↓  
Application Under Test  

For tests that validate multiple application layers:  
Cucumber Scenario  
↓  
Selenium UI Action  
↓  
Application backend  
↓  
MySQL Database  
↓  
JDBC Validation  
  
API Tests use RESTAssured to communicate directly with the application's REST endpoints without using the browser.  
# Prerequisites  
  Install the following before running the framework:  
- Java JDK 21  
- Maven  
- Git  
- A supported browser (Edge, Chrome, Safari, Firefox)  
- MySQL if database tests are being executed.  
  
Verify Java:  
java -version  

Verify Maven:  
mvn -version  
  
The Maven output should show Java 21.  

# Clone the Repository  
git clone https://github.com/BijanZarean/Automation_Testing_Framework.git  
  Navigate into the project:  
  cd Automation_Testing_Framework  
  Install and compile dependencies:  
  mvn clean test-compile  
  Maven automatically downloads the framework dependencies declared in pom.xml  
# Configuration
  Framework configuration is stored in:
  src/test/resource/test_data/env.properties  
  Current configuration structure:  
  base.url=http://localhost:4200  

  broswer=chrome  

  headless=false  

  db.url=jdbc:mysql://localhost:3306/petclinic  
  db.username=petclinic  
  db.password=password  

  DataReader.java loads this file once and makes its values available throughout the framework.  
  Example:  
  String url = DataReader.get("base.url");  
  #Supported Browsers  
  The framework currently supports:  
  - Chrome  
  - Firefox  
  - Microsoft Edge
  - Safari
  - Set the browser in env.properties:
  broswer=chrome
Valid values: chrome, firefox, edge, safari
The Driver utility creates and manages the appropriate Selenium WebDriver implementation.
A browser window size of 1920x1080 is applied for consistent local and CI execution.
# Headless Execution  
Chrome, Firefox, and Edge support headless execution.  
Configure:  
headless=true  
safari does not support headless execution in this framework and must use:  
browser=safari  
headless=false  
You can also override the browser and headless setting from Maven without editing env.properties.  
Example:  
mvn clean test -Pui_tests -Dbrowser=firefox -Dheadless=true  
System properties take precedence over the browser and headless values stored in env.properties.  
#Running UI Tests  
the default Maven profile is:  
ui_tests  
Run the complete UI suite:  
mvn clean test  
or explicitly:  
mvn clean test -Pui_tests  
The profile executes:  
runners.TestRunner  
TestRunner connects cucumber with TestNG and scans:  
src/test/resources/features for feature files and step_definitions for step definitions and hooks.  
# Running Tests by Cucumber Tag  
Feature files use tags such as:  @ui @smoke @regression @owners @vet @owner1 @owner2 @owner3  
Run only smoke tests:  
mvn clean test -Pui_tests -Dcucumber.filter.tags="@smoke"  
Run owner tests:  
mvn clean test -Pui_tests -Dcucumber.filter.tags="@owners"  
Run multiple required tags:  
mvn clean test -Pui_tests -Dcucumber.filter.tags="@ui and @smoke"  
Run either of two groups:  
mvn clean test -Pui_tests -Dcucumber.filter.tags="@owners or @vet"  
Exclude a tag:  
mvn clean test -Pui_tests -Dcucumber.filter.tags="not @vet"  
This allows suites to be changed from Maven or CI without modifying the Java Runner class.  
# Cucumber Dry Run  
The framework contains a dedicated Cucumber dry run profile.  
Dry run checks whether every Gherkin step has a matching Java step definition without executing the Selenium test logic.  
Run:  
mvn clean test -Pdry_run  
the profile executes: runners.DryRunner  
Use this after adding or editing .feature files to identify undefined steps before running the browser tests.  
Typical workflow:  
Write Cucumber Scenario Step  
↓  
Run DryRunner  
↓  
Implement Missing Step Definitions  
↓  
Run DryRunner Again To Confirm  
↓  
Run UI Suite  
#Feature Files  
Feature files are stored in: src/test/resource/features/  
Example Feature File:  
@ui @smoke
Feature: Petclinic home page  

  Scenario: Main navigation is displayed  
   Given I open the Petclinic application  
   Then the Home and Owners navigation links should be displayed  
   And the page heading should be "Welcome to Petclinic"  
# Page Object Model  
UI locators and page level actions are maintained in: src/test/java/pages/  
The Page Object Model seperates browser interaction from tets logic.  
For example:  
PetClinicHomePage contains locators and actions associated with the home page, while PetClinicOwnersPage contains owner search, owner creation, and owner table behavior.  
This prevents locators from being duplicated throughout step definition classes.  
An example of the syntax used to declare a page element object:  
private final By homeLink =  
 By.xpath("//a[@title='home page']");  
An example of a page action method:  
public void navigateToVeterinarian() {  
 wait.until(ExpectedConditions.visibilityOfElementLocated(veterinariansLink)).click();  
 wait.until(ExpectedConditions.visibilityOfElementLocated(ownersDrpdwnSearchBtn)).click();  
}  
# Step Definitions  
Cucumber step definitions are stored in: src/test/java/step_definitions/  
Feature file statements are mapped to Java methods using annotations such as: @Given, @When, @Then  
Example:  
@Given("I open the Petclinic application")  
public void i_open_thepetclinic_application() {  
 String basURL = DataReader.get("base.url");  
 Driver.getDriver().get(baseURL);  
 homePage = new PetClinicHomePage(Driver.getDriver());  
}  
Step definitions should primarily coordinate:  
- Page objects
- Test data
- Assertions
- API utilities
- Database utilities
Browser locators should remain in page object classes rather than step definition classes.  
# Selenium Driver Management  
Driver.java centrally controls WebDriver creation and cleanup.  
Typical usage: Driver.getDriver();  
The first call creates the configured browser. Additional calls during the same scenario return the existing WebDriver.  
At scenario completion: Driver.quitDriver();  
Closes the browser session and resets the stored driver reference.  
# Parallel Execution  
The current driver implementation stores a single static WebDriver instance.  
This works for sequential test execution but is not currently thread safe fir parallel Selenium execution.  
Before enabling parallel TestNG browser execution, the driver should be changed to use: ThreadLocal<WebDriver>  
# Cucumber Hooks  
Browser lifecycle management is implemented in: step_definitions/Hooks.java  
The framework initializes WebDriver for before scenarios tagged with @ui  
After each @ui scenario:  
- A screenshot is captured if the scenario failed.  
- The screenshot is attached to the Cucumber result.  
- WebDriver is closed.  
This prevents browser sessions from remaining open between scenarios.
# Failure Screenshots  
If an @ui scenario fails, the framework automatically captures a browser screenshot using 
Selenium's TakesScreenshot interface.  
The screenshot is attached directly to the Cucumber scenario result as: Failure screenshot.  
This is useful when reviewing failures in generated reports or Continuous Integration execution.  
