package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

public class LessonTwoTest extends BaseTestCase{

    String baseUrl = BaseTestCase.baseUrl();
    public final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testGetTextSecondMessage() {
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .get(baseUrl + "get_json_homework")
                .andReturn();

        Assertions.assertResponseCodeEquals(response, 200);

        List<java.util.Map<String, Object>> messages = response.jsonPath().getList("messages");
        Object answer = messages.get(1);
        System.out.println(answer);

    }

    @Test
    @Feature("Redirect")
    public void testRedirectAddress() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get(baseUrl+"long_redirect")
                .andReturn();


        String answer = response.getHeader("Location");
        System.out.println(answer);
    }

    @Test
    @Feature("Redirect")
    public void testCountRedirects() {

        int statusCode = 0;
        int countRedirects = 0;
        String redirectLink = baseUrl+"long_redirect";

        Response response = RestAssured
                .get(redirectLink)
                .andReturn();


        Assertions.assertResponseCodeEquals(response, 200);

        while (statusCode != 200) {

            System.out.println(response.statusCode());
            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(redirectLink)
                    .andReturn();
            statusCode = response.statusCode();
            countRedirects++;
            redirectLink = response.getHeader("Location");
        }
        System.out.println(countRedirects);
    }

    @Test
    @Feature("Delay")
    public void testDelay() throws InterruptedException {

        Response response = RestAssured
                .given()
                .get(baseUrl+"longtime_job")
                .andReturn();

        Assertions.assertResponseCodeEquals(
                response,
                200);

        int delay = apiCoreRequests.getIntFromJson(response,"seconds");
        String token = apiCoreRequests.getStringFromJson(response,"token");

        JsonPath beforeTimer = RestAssured
                .given()
                .queryParam("token", token)
                .get(baseUrl+"longtime_job")
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
                .get(baseUrl+"longtime_job")
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

    @Test
    @Feature("Authorization")
    public void testPasswordBrutforce () {
        String passwords = "111111\tqazwsx\tpassword\tloveme\t123456\twelcome\taccess\t1234\tpassword1\tflower\tiloveyou\tadobe123\t1234567\t1q2w3e4r\t123456789\taa123456\tqwerty\t555555\tpassw0rd\t000000\twhatever\t123123\tninja\t1234567\tmonkey\tadmin\tlovely\thottie\tsolo\tfootball\tFootball\tmichael\t12345\tshadow\tsunshine\tmustang\tdragon\t1234567890\t654321\tbailey\tprincess\t2345678\tjesus\tbaseball\thello\tzaq1zaq1\tletmein\tlogin\tfreedom\t!@#$%^&*\t696969\t666666\tcharlie\tdonald\tmonkey\tabc123\t12345678\tstarwars\tsuperman\t888888\tqwerty123\tazerty\t1qaz2wsx\t121212\tphotoshop\tashley\tqwertyuiop\t7777777\tbatman\tmaster\ttrustno1\t123qwe";
        String[] passwordsArray = passwords.split("\t");

        int i;
        for (i = 0; i < passwordsArray.length; i++) {
            Response response = RestAssured
                    .given()
                    .queryParam("login", "super_admin")
                    .queryParam("password", passwordsArray[i])
                    .post(baseUrl+"get_secret_password_homework")
                    .andReturn();

            Assertions.assertResponseCodeEquals(response, 200);

            String authCookie = response.getCookie("auth_cookie");

            Response responseWithCookie = RestAssured
                    .given()
                    .cookies("auth_cookie", authCookie)
                    .when()
                    .post(baseUrl+"check_auth_cookie")
                    .andReturn();

            if (responseWithCookie.getBody().asString().equals("You are authorized")) {
                System.out.println("Правильный пароль:  " + passwordsArray[i]);
                break;
            }

        }
    }
}
