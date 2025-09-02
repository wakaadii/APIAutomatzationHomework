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

    @Test
    public void testDelay() throws InterruptedException {
        JsonPath response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        int delay = response.get("seconds");
        String token = response.get("token");
        JsonPath beforeTimer = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String responceCode = beforeTimer.get("status");
        if (responceCode.equals("Job is NOT ready")) {
            System.out.println("До истечения таймера код корректен");
        } else {
            System.out.println("ошибка в статусе ответа до истечения таймера");
            return;
        }

        Thread.sleep(delay*1000);

        JsonPath afterTimer = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        responceCode = afterTimer.get("status");
        String result = afterTimer.get("result");

        if (responceCode.equals("Job is ready") & (result != null)) {
            System.out.println("статус после истечения таймера " + responceCode + "\nзначение поля 'result' = " + result );
        } else {
            if (!responceCode.equals("Job is ready")) {
                System.out.println("ошибка в статусе ответа до истечения таймера: " + responceCode);
            } else {
                System.out.println("Поле result не получено");
            }
        }
    }
}
