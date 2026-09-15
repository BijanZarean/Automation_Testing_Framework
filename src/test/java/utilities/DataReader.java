package utilities;
/*
This class loads the env.properties (src/test/resources/test_data) and stores the key-value pairs in
a Java Properties object (in the memory):
base.url → http://localhost:4200
browser  → chrome
headless → false
Other classes can then retrieve values with:
DataReader.get("base.url");
 */
//Properties is a Java class designed to hold string-based key-value configuration data.
//its load() method understands the key=value format used in .properties files:
import java.util.Properties;
//An InputStream represents a stream of bytes being read. Here, it reads the .properties file
//from the projects test classpath:
import java.io.InputStream;
//Reading a file can fail, so java requires the code to handle IOException. Examples include a
//damaged resource or a problem while reading the stream:
import java.io.IOException;
//Used when cobverting variables such as db.password into DB_PASSWORD:
import java.util.Locale;
/*
loads configuration values from:
src/test/resources/test_data/env.properties
 */
//Final so another class cannot extend DataReader:
public final class DataReader {
//This line creates the object that holds the loaded configuration data.
//Using private, this is encapsulation, users must use the getter method .get():
    private static final Properties PROPERTIES = new Properties();
//A static initialization block runs only once when Java first loads a class. Therefore, the env.properties file
//is loaded once rather than reopened everytime a value is requested.
    static {
        String resourceName = "test_data/env.properties";
//try with resources statement; creates the InputStream, uses it inside the try block, and closes it afterward:
        try (
//We are not creating an object, instead accessing the Class<DataReader> object containing runtime information about the class:
                InputStream inputStream = DataReader.class
//We use .getClassLoader().getResourceAsStream() because Maven copies the env.properties to target/test-classes, class loaded can locate it there:
                .getClassLoader()
//Searches the classpath for the named resource and opens it as an InputStream
                .getResourceAsStream(resourceName)
                ) {
//Because .getResourceAsStream(resourceName) returns null when no matching classpath resource exists, we throw an exception if this occurs:
            if (inputStream==null) {
                throw new IllegalStateException(
                        "Could not find configuration resource: "+resourceName);
            }
//Now that we opened the env.properties file in an InputStream object, this will read the contents and add the parsed key-
//-value entries to the properties object:
            PROPERTIES.load(inputStream);
//If the class fails when executing its static initialization logic, due to an IOException, we throw an ExceptionInInitializerError
//and stop execution:
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
//The constructor is private because the class only provides static utility behavior.
//Making the constructor private prevents accidental object prevention.
//This class is intended to be used statically DataReader.get("url");
    private DataReader() {
        //private added to constructor to prevent this utility class from being instantiated.
    }
//public static method to access through the class to retrieve configuration values:
    public static String get(String key) {
        /*
        Priority 1:
        Check for a Java system property.
        Example:
        -Dbase.url=http://localhost:4200
         */
        //Asks the JVM if the configuration value was supplied as a
        // Java system property (mvn test -Pui_tests -Dheadless=true)
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty.trim();
        }
        /*
        Priority 2:
        Convert the properties style key into an
        environment variable style key.
        Example:
        db.password -> DB_PASSWORD
        export DB_PASSWORD='myLocalPassword'
         */
        //our java configration keys use db.password style while the env variables use DB_PASSWORD so we convert!
        //"Locale.ROOT" is used to convert using a language neutral uppercase conversion:
        String environmentKey = key.toUpperCase(Locale.ROOT).replace('.', '_');
        //searches to see if we set an env variable with this name (export DB_PASSWORD='myLocalPassword'), for Jenkins:
        String environmentValue = System.getenv(environmentKey);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue.trim();
        }
        /*
        Priority 3:
        Fall back to env.proprties file.
         */
        String propertyValue = PROPERTIES.getProperty(key);
        if (propertyValue == null || propertyValue.isBlank()) {
            throw new IllegalArgumentException("Missing configuration property: "+key);
        }
        return propertyValue.trim();
    }


}
