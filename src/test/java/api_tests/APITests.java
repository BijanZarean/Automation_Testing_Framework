package api_tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.DataReader;
import utilities.TestDataGenerator;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class APITests {
    /*
    reads the api.base.url variable in env.properties file and tells REST Assured that every API request in
    this class starts at this base URI:
     */
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = DataReader.get("api.base.url");
    }

    /*
    REST Assured pattern:
    given() -> configure the HTTP request.
    when() -> send the HTTP request.
    then() -> validate the HTTP response.
     */

    @Test
    public void getAllOwners() {
        /*
        inside given() we can add headers, parameters, body content, authentication, cookies, and other
        request configurations.
        log().all() logs the request before it is sent to show HTTP method, URL, headers, query parameters, path
        parameters, and request body:
         */
        given().log().all().accept("application/json")
        /*
        Sets the endpoint to be sent
         */
        .when().get("/owners")
        /*
        Verifying the response returned is successful and has content.
        log().all() logs the response returned such as the response body / headers and the status code:
         */
        .then().statusCode(200).body("size()", greaterThan(0))
                .body(matchesJsonSchemaInClasspath("schemas/all_owners_schema.json"));
    }

    @Test
    public void getOwnerByID() {
        int ownerID = 1;
        given().accept("application/json")
                .when().get("/owners/"+ownerID)
                .then().statusCode(200).body("id", equalTo(ownerID))
                .body("firstName", equalTo("George"))
                .body(matchesJsonSchemaInClasspath("schemas/owner_by_id_schema.json"));
    }

    private final String firstName = TestDataGenerator.firstName();
    private final String lastName = TestDataGenerator.lastName();
    private final String address = TestDataGenerator.address();
    private final String city = TestDataGenerator.city();
    private final String telephone = TestDataGenerator.phoneNumber();

    private int ownerID;

    @Test
    public void createOwner() {
        String requestBody = "{\n" +
                "  \"firstName\": \""+firstName+"\",\n" +
                "  \"lastName\": \""+lastName+"\",\n" +
                "  \"address\": \""+address+"\",\n" +
                "  \"city\": \""+city+"\",\n" +
                "  \"telephone\": \""+telephone+"\"\n" +
                "}";
        System.out.println(requestBody);
        Response response = given().body(requestBody).contentType("application/json")
                .when().post("/owners");
        response.then().statusCode(201).body("firstName", equalTo(firstName))
                .body("lastName", equalTo(lastName)).body("address", equalTo(address))
                .body("telephone", equalTo(telephone))
                .body(matchesJsonSchemaInClasspath("schemas/create_owner_schema.json"));
        ownerID = response.jsonPath().getInt("id");
    }

    @Test
    public void deleteOwner() {
        System.out.println(ownerID);
        given().accept("application/json")
                .when().delete("/owners/"+ownerID)
                .then().statusCode(204);
        given().accept("application/json")
                .when().get("/owners/"+ownerID)
                .then().statusCode(404);
    }
}
