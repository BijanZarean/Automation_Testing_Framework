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
//getProperty(key) searches the loaded properties for the supplied key:
        String value = PROPERTIES.getProperty(key);
//If the key is not found, it returns null, does not throw an error so we throw one ourselves if this occurs:
        if (value==null || value.isBlank()) {
            throw new IllegalArgumentException(
              "Missing configuration property: "+key
            );
        }
        return value.trim();
    }
}
