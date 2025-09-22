package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.BaseTestCase;
import lib.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LessonThreeTest extends BaseTestCase {

    String baseUrl = BaseTestCase.baseUrl();


    public String[] UserAgentParameters() {

        return new String[]{"Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
                "iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.",
                "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
                "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"};
    }

    public String[][] responceParameters() {
        return new String[][] {
                {"Android", "No", "Mobile"},
                {"iOS", "Chrome", "Mobile"},
                {"Unknown", "Unknown", "Googlebot"},
                {"No", "Chrome", "Web"},
                {"iPhone", "No", "Mobile"}
        };
    }

    @Test
    public void checkPhraseLength() {
        String hello = "Hello, world";
        assertTrue(hello.length()>15, "The phrase \"" + hello + "\" is too short. Its length is " + hello.length() + " symbols");
    }

    @Test
    public void checkCookie() {
        Response response = RestAssured
                .get(baseUrl+"homework_cookie")
                .andReturn();

        Assertions.assertResponseCodeEquals(response, 200);
        String responseCookie = response.getCookie("HomeWork");

        assertEquals("hw_value", responseCookie, "cookie value is unexpected: " + responseCookie);
    }

    @Test
    public void checkHeader() {
        Response response = RestAssured
                .get(baseUrl+"homework_header")
                .andReturn();

        Assertions.assertResponseCodeEquals(response, 200);
        String responseHeader = response.getHeader("x-secret-homework-header");

        assertEquals("Some secret value", responseHeader, "header value is unexpected: " + responseHeader);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    public void checkUserAgent(int number) {
        Response response = RestAssured
                .given()
                .header("User-Agent", UserAgentParameters()[number])
                .get(baseUrl+"user_agent_check")
                .andReturn();


        Assertions.assertResponseCodeEquals(response, 200);
        String device = response.jsonPath().getString("device");
        String browser = response.jsonPath().getString("browser");
        String platform = response.jsonPath().getString("platform");

//        System.out.println("\n" + device + " " + responceParameters()[number][0]);
//        System.out.println(browser + " " + responceParameters()[number][1]);
//        System.out.println(platform + " " + responceParameters()[number][2]);



        assertEquals(responceParameters()[number][0], device, "unexpected parameter 'device' in case " + (number+1));
        assertEquals(responceParameters()[number][1], browser, "unexpected parameter 'browser' in case " + (number+1));
        assertEquals(responceParameters()[number][2], platform, "unexpected parameter 'platform' in case " + (number+1));
    }
}
