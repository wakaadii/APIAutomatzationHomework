package tests;

import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {
    String cookie;
    String header;
    int userIdOnAuth;
    public final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String baseUrl = BaseTestCase.baseUrl();

    @BeforeEach
    public void loginUser() {
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post(baseUrl+"user/login")
                .andReturn();

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    @Feature("Delete profile")
    @Feature("Negative")
    public void DeleteUserId2 () {
        Response auth = apiCoreRequests.authToUserId2();

        Response deleteUser = apiCoreRequests.deleteUser(
                baseUrl+"user/2",
                this.getHeader(auth,"x-csrf-token"),
                this.getCookie(auth, "auth_sid"));

        Assertions.assertJsonByName(deleteUser, "error", "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Feature("Delete profile")
    public void deleteNewUser() {

        //create
        Map<String,String> userData = DataGenerator.getRegistrationData();
        String userId = apiCoreRequests.createNewUser(userData);

        //auth
        Response auth = apiCoreRequests.makePostRequest(
                baseUrl+"user/login",
                userData
        );

        //delete
        Response deleteUser = apiCoreRequests.deleteUser(
                baseUrl+"user/"+ userId,
                this.getHeader(auth,"x-csrf-token"),
                this.getCookie(auth,"auth_sid"));

        Assertions.assertJsonByName(deleteUser, "success", "!");
    }

    @Test
    @Feature("Delete profile")
    @Feature("Negative")
    public void deleteAnotherUser() {

        //create
        Map<String,String> userData = DataGenerator.getRegistrationData();
        String userId = apiCoreRequests.createNewUser(userData);

        //auth
        Response auth = apiCoreRequests.makePostRequest(
                baseUrl+"user/login",
                userData
        );

        //delete
        Response deleteUser = apiCoreRequests.deleteUser(
                baseUrl+"user/1"+ userId,
                this.getHeader(auth,"x-csrf-token"),
                this.getCookie(auth,"auth_sid"));

        Assertions.assertJsonByName(deleteUser, "error", "This user can only delete their own account.");
    }
}
