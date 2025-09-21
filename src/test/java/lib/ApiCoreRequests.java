package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static lib.DataGenerator.getRegistrationData;

public class ApiCoreRequests {

    @Step("Create new user")
    public String createNewUser(Map<String,String> userData) {

        JsonPath response = RestAssured
                .given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        return response.getString("id");
    }

    @Step("Auth to user with id 2")
    public Response authToUserId2() {
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        return  makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
    }

    @Step("\"Get\" without token and cookie")
    public Response makeGetRequests(String url) {
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }
    @Step("\"Get\" with token and cookie")
    public Response makeGetRequests(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("\"Get\" with cookie")
    public Response makeGetRequestsWithCookie (String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("\"Get\" with token")
    public Response makeGetRequestsWithToken (String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("\"Post\" request")
    public Response makePostRequest(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Edit user profile")
    public Response changeProfileData(String url, String header, String cookie, String changedParam, String changedValue) {

        Map<String, String> editData = new HashMap<>();
        editData.put(changedParam, changedValue);
        return given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .body(editData)
                .put(url)
                .andReturn();

    }

    @Step("Edit user profile without authorization")
    public Response changeProfileData(String url, String changedParam, String changedValue) {

        Map<String, String> editData = new HashMap<>();
        editData.put(changedParam, changedValue);
        return given()
                .body(editData)
                .put(url)
                .andReturn();

    }
}
