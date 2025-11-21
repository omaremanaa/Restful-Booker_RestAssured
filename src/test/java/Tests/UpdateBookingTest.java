package Tests;

import Requests.AuthenticationRequests;
import Requests.UpdateBookingRequest;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import resources.AllureSoftAssert;
import resources.LogUtils;
import resources.Utils;

import java.util.Map;

public class UpdateBookingTest extends Utils {
AllureSoftAssert softAssert;
UpdateBookingRequest updateBookingRequest;
    AuthenticationRequests authenticationRequests;

@Parameters({"id","body"})
@Test
public void validUpdateBookingTest(int id, String body) {
    Allure.getLifecycle().updateTestCase(testResult -> {
        testResult.setName("Valid Update Booking Test");
        testResult.setDescription("This test verifies that a booking can be updated successfully with valid data and authentication.");
    });
    softAssert = new AllureSoftAssert();
    authenticationRequests = new AuthenticationRequests();
    updateBookingRequest = new UpdateBookingRequest();

    Response authResponse = authenticationRequests.createToken("admin", "password123");
    LogUtils.info("Auth Response: "
            + authResponse.asString());

    String token = authResponse.jsonPath().getString("token");
    Response response = updateBookingRequest.updateBooking(id, token, body);
    LogUtils.info("Valid Update Booking Response: "
            + response.asString());
    softAssert.assertEquals(response.statusCode(), 200, "Expected status code 200");
    softAssert.assertTrue(response.getTime()<2000, "Response time is less than 2000ms");

    softAssert.assertAll();
}

@Parameters({"id","body"})
@Test
public void invalidUpdateBookingByIncorrectToken(int id, String body) {
    Allure.getLifecycle().updateTestCase(testResult -> {
        testResult.setName("Invalid Update Booking Test");
        testResult.setDescription("This test verifies that updating a booking fails when an incorrect authentication token is provided.");
    });
    softAssert = new AllureSoftAssert();
    updateBookingRequest = new UpdateBookingRequest();

    String invalidToken = "invalidToken123";
    Response response = updateBookingRequest.updateBooking(id, invalidToken, body);
    LogUtils.info("Invalid Update Booking (By Incorrect Token) Response: "
            + response.asString());

    softAssert.assertEquals(response.statusCode(), 403, "Expected status code 403");
    softAssert.assertTrue(response.getTime()<2000, "Response time is less than 2000ms");

    softAssert.assertAll();
}

    @Parameters({"incorrectID","body"})
    @Test
    public void invalidUpdateBookingByIncorrectID(int incorrectID, String body) {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Update Booking Test By Incorrect ID");
            testResult.setDescription("This test verifies that updating a booking fails when an incorrect booking ID is provided.");
        });
        softAssert = new AllureSoftAssert();
        updateBookingRequest = new UpdateBookingRequest();
        authenticationRequests = new AuthenticationRequests();
        Response authResponse = authenticationRequests.createToken("admin", "password123");
        LogUtils.info("Auth Response: "
                + authResponse.asString());

        String token = authResponse.jsonPath().getString("token");
        Response response = updateBookingRequest.updateBooking(incorrectID, token, body);
        LogUtils.info("Invalid Update Booking (By Nonexistent ID) Response: "
                + response.asString());
        softAssert.assertEquals(response.statusCode(), 405, "Expected status code 405");
        softAssert.assertTrue(response.getTime()<2000, "Response time is less than 2000ms");

        softAssert.assertAll();
    }
}
