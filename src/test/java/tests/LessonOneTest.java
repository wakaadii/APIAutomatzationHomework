package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

public class LessonOneTest extends BaseTestCase {

    String baseUrl = BaseTestCase.baseUrl();
    @Test
    public void helloFrom() {
        System.out.println("Hello from Ilya");
    }

    @Test
    public void testGetRequest() {
        Response response = RestAssured
                .get(baseUrl+"get_text")
                .andReturn();
        response.prettyPrint();
    }

}
