package lib;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import io.restassured.http.Headers;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {

    @Description("здесь устанавливается базовый адрес, на который будут идти запросы")
    protected static String baseUrl() {
        return "https://playground.learnqa.ru/api_dev/";
        //return "https://playground.learnqa.ru/api/";
    }
    @Description("Get header from response with filled name")
    protected String getHeader(Response Response, String name) {
        Headers headers = Response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "Response hasn't header with name \"" + name +"\"");
        return headers.getValue(name);
    }

    @Description("Get cookie from response with filled name")
    protected String getCookie(Response Responce, String name) {
        Map<String, String> cookies = Responce.getCookies();

        assertTrue(cookies.containsKey(name), "Response hasn't cookie with name \"" + name +"\"");
        return cookies.get(name);
    }

    @Description("Get int-parameter from response with filled name")
    protected int getIntFromJson(Response Response, String name) {
        Response.then().assertThat().body("$", hasKey(name));
        return Response.jsonPath().getInt(name);
    }
}