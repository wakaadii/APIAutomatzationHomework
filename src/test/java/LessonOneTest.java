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

    @Test
    public void testPasswordBrutforce () {
        String passwords = "111111\tqazwsx\tpassword\tloveme\t123456\twelcome\taccess\t1234\tpassword1\tflower\tiloveyou\tadobe123\t1234567\t1q2w3e4r\t123456789\taa123456\tqwerty\t555555\tpassw0rd\t000000\twhatever\t123123\tninja\t1234567\tmonkey\tadmin\tlovely\thottie\tsolo\tfootball\tFootball\tmichael\t12345\tshadow\tsunshine\tmustang\tdragon\t1234567890\t654321\tbailey\tprincess\t2345678\tjesus\tbaseball\thello\tzaq1zaq1\tletmein\tlogin\tfreedom\t!@#$%^&*\t696969\t666666\tcharlie\tdonald\tmonkey\tabc123\t12345678\tstarwars\tsuperman\t888888\tqwerty123\tazerty\t1qaz2wsx\t121212\tphotoshop\tashley\tqwertyuiop\t7777777\tbatman\tmaster\ttrustno1\t123qwe";
        String[] passwordsArray = passwords.split("\t");

        int i;
        for (i = 0; i < passwordsArray.length; i++) {
            Response response = RestAssured
                    .given()
                    .queryParam("login", "super_admin")
                    .queryParam("password", passwordsArray[i])
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String authCookie = response.getCookie("auth_cookie");

            Response responseWithCookie = RestAssured
                    .given()
                    .cookies("auth_cookie", authCookie)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            if (responseWithCookie.getBody().asString().equals("You are authorized")) {
                System.out.println("Правильный пароль:  " + passwordsArray[i]);
                break;
            }

        }
    }

}
