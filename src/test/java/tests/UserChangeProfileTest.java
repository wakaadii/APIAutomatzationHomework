package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static lib.DataGenerator.getRegistrationData;

public class UserChangeProfileTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    public final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @BeforeEach
    public void loginUser() {
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }


    @Test
    public void unauthorizedChangeProfileTest() {

        Map<String, String> userData = getRegistrationData();
        String userID = apiCoreRequests.createNewUser(userData);

        Response changeProfile = apiCoreRequests.changeProfileData(
                "https://playground.learnqa.ru/api/user/"+ userID,
                "username",
                "aez");

        Assertions.assertJsonByName(changeProfile, "error", "Auth token not supplied");

    }

    @Test
    public void anotherAuthorizedUserChangeProfileTest() {

        //create
        Map<String, String> userData = getRegistrationData();
        String userID = apiCoreRequests.createNewUser(userData);

        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));



        //auth
        Response auth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData
        );

        //change
        Response changeProfile = apiCoreRequests.changeProfileData(
                "https://playground.learnqa.ru/api/user/"+ userID + "1",
                this.getHeader(auth,"x-csrf-token"),
                this.getCookie(auth,"auth_sid"),
                "username",
                "aez");

        //check
        Assertions.assertJsonByName(changeProfile, "error", "This user can only edit their own data.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "first name"})
    public void ChangeEmailToIncorrectTest(String param) {
        //create
        Map<String, String> userData = getRegistrationData();
        String userID = apiCoreRequests.createNewUser(userData);

        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));



        //auth
        Response auth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData
        );

        //change

        if (param.equals("email")) {
            Response changeProfile = apiCoreRequests.changeProfileData(
                    "https://playground.learnqa.ru/api/user/"+ userID,
                    this.getHeader(auth,"x-csrf-token"),
                    this.getCookie(auth,"auth_sid"),
                    "email",
                    "aez.com");

            //check
            Assertions.assertJsonByName(changeProfile, "error", "Invalid email format");
        } else if (param.equals("first name")) {
            Response changeProfile = apiCoreRequests.changeProfileData(
                    "https://playground.learnqa.ru/api/user/"+ userID,
                    this.getHeader(auth,"x-csrf-token"),
                    this.getCookie(auth,"auth_sid"),
                    "firstName",
                    "a");

            //check
            Assertions.assertJsonByName(changeProfile, "error", "The value for field `firstName` is too short");
        }

    }
}
