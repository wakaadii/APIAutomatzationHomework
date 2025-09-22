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
import static org.hamcrest.Matchers.hasKey;

public class ApiCoreRequests extends BaseTestCase{

    String baseUrl = BaseTestCase.baseUrl();

    @Step("Create new user")
    public String createNewUser(Map<String,String> userData) {

        JsonPath response = RestAssured
                .given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(baseUrl+"user/")
                .jsonPath();

        return response.getString("id");
    }

    @Step("Auth to user with id 2")
    public Response authToUserId2() {
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        return  makePostRequest(baseUrl+"user/login", authData);
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

    @Step("Delete request")
    public Response deleteUser (String url, String header, String cookie) {
        return given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .delete(url)
                .andReturn();
    }

    @Step("get int from json")
    public int getIntFromJson(Response Response, String name) {
        Response.then().assertThat().body("$", hasKey(name));
        return Response.jsonPath().getInt(name);
    }

    @Step("get string from json")
    public String getStringFromJson(Response Responce, String name) {
        Responce.then().assertThat().body("$", hasKey(name));
        return Responce.jsonPath().getString(name);
    }

}
