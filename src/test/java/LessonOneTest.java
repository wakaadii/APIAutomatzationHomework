import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.*;

public class LessonOneTest {

    @Test
    public void helloFrom() {
        System.out.println("Hello from Ilya");
    }

    @Test
    public void testGetRequest() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }

    @Test
    public void testGetTextSecondMessage() {
        JsonPath response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        List<Object> messages = response.get("messages");
        Object answer = messages.get(1);
        System.out.println(answer);

    }

    @Test
    public void testRedirectAddress() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();


        String answer = response.getHeader("Location");
        System.out.println(answer);
    }

    @Test
    public void testCountRedirects() {

        int statusCode = 0;
        int countRedirects = 0;
        String redirectLink = "https://playground.learnqa.ru/api/long_redirect";

        while (statusCode != 200) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(redirectLink)
                    .andReturn();
            statusCode = response.statusCode();
            countRedirects = countRedirects + 1;
            redirectLink = response.getHeader("Location");
        }
        System.out.println(countRedirects);
    }
}
