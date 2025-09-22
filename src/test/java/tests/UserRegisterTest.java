package tests;


import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static lib.Assertions.assertResponseCodeEquals;
import static lib.Assertions.assertResponseTextEquals;
import static lib.DataGenerator.getRegistrationData;
import static lib.DataGenerator.getRegistrationDataWithoutParameter;

public class UserRegisterTest extends BaseTestCase {


    public final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String baseUrl = BaseTestCase.baseUrl();

    @Test
    public void testCreateUserWithIncorrectEmail() {
        String email = "vinkotovexample.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = getRegistrationData(userData);

        Response response = apiCoreRequests.makePostRequest(
                baseUrl+"user/",
                userData
        );

        assertResponseTextEquals(response, "Invalid email format");
        assertResponseCodeEquals(response, 400);
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    public void testCreateUserWithoutFields (String param) {
        Map<String, String> userData = getRegistrationDataWithoutParameter(param);

        Response response = apiCoreRequests.makePostRequest(
                baseUrl+"user/",
                userData
        );

        assertResponseTextEquals(response, "The following required params are missed: " + param);
        assertResponseCodeEquals(response, 400);
    }
    @ParameterizedTest
    @ValueSource(strings =  {"a", "Aa1_Bb2-Cc3+Dd4=Ee5|Ff6@Gg7#Hh8$Ii9%Jj0^Kk1&Ll2*Mm3(Nn4)Oo5{Qq6}Rr7[Ss8]Tt9~Uu0`Vv1!Ww2?Xx3.Yy4,Zz5;Aa6_Bb7-Cc8+Dd9=Ee0|Ff1@Gg2#Hh3$Ii4%Jj5^Kk6&Ll7*Mm8(Nn9)Oo0{Qq1}Rr2[Ss3]Tt4~Uu5`Vv6!Ww7?Xx8.Yy9,Zz0Aa1_Bb2-Cc3+Dd4=Ee5|Ff6@Gg7#Hh8$Ii9%Jj0^Kk1&Ll2*Mm3(Nn4)Oo5{Qq6}Rr7[Ss8]Tt9~Uu0`Vv1!Ww2?Xx3.Yy4,Zz5;Aa6_Bb7-Cc8+Dd9=Ee0|Ff1@Gg2#Hh3$Ii4%Jj5^Kk6&Ll7*Mm8(Nn9)Oo0{Qq1}Rr2[Ss3]Tt4~Uu5`Vv6!Ww7?Xx8.Yy9,Zz0"})
    public void testCreateUserWithSomeNames(String name) {

        String error;
        if (name.length() < 2){
            error = "short";
        } else if (name.length() > 250) {
            error = "long";
        } else {
            error = null;
        }


        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", name);
        userData = getRegistrationData(userData);

        Response response = apiCoreRequests.makePostRequest(
                baseUrl+"user/",
                userData
        );

        assertResponseTextEquals(response, "The value of 'firstName' field is too " + error);
        assertResponseCodeEquals(response, 400);
    }


}
