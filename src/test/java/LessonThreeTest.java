import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LessonThreeTest {

    @Test
    public void checkPhraseLength() {
        String hello = "Hello, world";
        assertTrue(hello.length()>15, "The phrase \"" + hello + "\" is too short. Its length is " + hello.length() + " symbols");
    }

    @Test
    public void checkCookie() {
        Response responce = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        String responceCookie = responce.getCookie("HomeWork");


        assertEquals("hw_value", responceCookie, "cookie value is unexpected: " + responceCookie);
    }

    @Test
    public void checkHeader() {
        Response Responce = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        String responceHeader = Responce.getHeader("x-secret-homework-header");

        assertEquals("Some secret value", responceHeader, "header value is unexpected: " + responceHeader);
    }
}
