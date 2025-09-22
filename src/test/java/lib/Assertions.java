package lib;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

    @Step("Assertion int parameter from json")
    public static void assertJsonByName(Response Response, String name, int expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    @Step("Assertion string parameter from json")
    public static void assertJsonByName(Response Response, String name, String expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    @Step("Assertion response text")
    public static void assertResponseTextEquals (Response Response, String ExpectedAnswer) {
        assertEquals(
                ExpectedAnswer,
                Response.asString(),
                "Response text is unexpected " + Response.asString()
        );
    }

    @Step("assertion status code of response")
    public static void assertResponseCodeEquals (Response Response, int ExpectedStatusCode) {
        assertEquals(
                ExpectedStatusCode,
                Response.statusCode(),
                "Response code is unexpected " + Response.statusCode()
        );
    }

    @Step("Assert count of response's elements")
    public static void assertCountOfBodyElements(Response response, int CountElements) {
        assertEquals(
                response.jsonPath().getMap("$").values().size(),
                CountElements,
                "Number of elements in json is incorrect: "+ response.jsonPath().getMap("$").values().size()
        );
    }

}
