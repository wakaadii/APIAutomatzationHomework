package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

    public static void assertJsonByName(Response Response, String name, int expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertJsonByName(Response Response, String name, String expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertResponseTextEquals (Response Response, String ExpectedAnswer) {
        assertEquals(
                ExpectedAnswer,
                Response.asString(),
                "Responce text is unexpected " + Response.asString()
        );
    }

    public static void assertResponseCodeEquals (Response Response, int ExpectedStatusCode) {
        assertEquals(
                ExpectedStatusCode,
                Response.statusCode(),
                "Responce code is unexpected " + Response.statusCode()
        );
    }

}
