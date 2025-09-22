package lib;

import io.qameta.allure.Step;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {

    public static String getRandomEmail() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return ("learnqa" + timestamp + "@example.com");
    }

    @Step("Default user's creator ")
    public static Map<String,String> getRegistrationData() {
        Map<String, String> registrationData = new HashMap<>();
        registrationData.put("email", DataGenerator.getRandomEmail());
        registrationData.put("password", "123");
        registrationData.put("username", "learnQA");
        registrationData.put("firstName", "learnQA");
        registrationData.put("lastName", "learnQA");

        return registrationData;
    }

    @Step("User's creator with some filled parameters")
    public static Map<String,String> getRegistrationData(Map<String, String> nonDefaultValues){
        Map<String, String> defaultValues = DataGenerator.getRegistrationData();
        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for (String key : keys ) {
            if (nonDefaultValues.containsKey(key)) {
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }

    @Step("Fill default parameters")
    public static Map<String,String> getRegistrationDataWithoutParameter(String excludeValue) {
        Map<String, String> registrationData = new HashMap<>();
        registrationData.put("email", DataGenerator.getRandomEmail());
        registrationData.put("password", "123");
        registrationData.put("username", "learnQA");
        registrationData.put("firstName", "learnQA");
        registrationData.put("lastName", "learnQA");
        registrationData.remove(excludeValue);

        return registrationData;
    }
}
