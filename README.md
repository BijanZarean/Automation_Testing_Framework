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

