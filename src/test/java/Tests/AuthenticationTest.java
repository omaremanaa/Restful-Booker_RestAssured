package Tests;

import Requests.AuthenticationRequests;
import Helpers.JsonReader;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.*;
import resources.AllureSoftAssert;
import resources.LogUtils;
import resources.Utils;

public class AuthenticationTest extends Utils {
    AuthenticationRequests authenticationRequests;
    AllureSoftAssert softAssert;
    JsonReader jsonReader;



    @BeforeClass
    public void setup(){
        authenticationRequests = new AuthenticationRequests();
    }
    @BeforeMethod
    public void init(){
        softAssert = new AllureSoftAssert();
        jsonReader = new JsonReader("auth-data");
    }
    @Test
    public void validAuthenticationTest() {
        String username = jsonReader.getJsonData("username");
        String password = jsonReader.getJsonData("password");
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Valid Authentication Test");
            testResult.setDescription("This test verifies that a valid token is generated when correct credentials are provided.");
        });

        Response response = authenticationRequests.createToken(username, password);
        String token = response.jsonPath().getString("token");
        LogUtils.info("Authentication Response Body: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 200, "Status code is 200");
        softAssert.assertNotNull(token, " Token is not null");
        softAssert.assertTrue(response.getTime() < 2000, " Response time is less than 2000ms");
        softAssert.assertAll();
    }

    @Test
    public void invalidPasswordTest() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Password Test");
            testResult.setDescription("This test verifies that authentication fails when an incorrect password is provided.");
        });
        String username = jsonReader.getJsonData("username");
        String invalidPassword = jsonReader.getJsonData("invalidPassword");

        Response response = authenticationRequests.createToken(username, invalidPassword);
        LogUtils.info("Response for Invalid Password Authentication"
                + response.asString());

        softAssert.assertNotEquals(response.statusCode(), 200, "Status code is not 200");
        softAssert.assertTrue(response.asString().contains("Bad credentials"), "Response contains 'Bad credentials'");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertAll();
    }

    @Test
    public void invalidUsernameTest() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Username Test");
            testResult.setDescription("This test verifies that authentication fails when an incorrect username is provided.");
        });
        String invalidUsername = jsonReader.getJsonData("invalidUsername");
        String password = jsonReader.getJsonData("password");
        Response response = authenticationRequests.createToken(invalidUsername, password);
        LogUtils.info("Response for Invalid Username Authentication"
                + response.asString());

        softAssert.assertNotEquals(response.statusCode(), 200, " Status code is not 200");
        softAssert.assertTrue(response.asString().contains("Bad credentials"), " Response contains 'Bad credentials'");
        softAssert.assertTrue(response.getTime() < 2000, " Response time is less than 2000ms");
        softAssert.assertAll();
    }


}
