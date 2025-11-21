package Tests;

import Requests.AuthenticationRequests;
import Requests.DeleteRequests;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import resources.AllureSoftAssert;
import resources.LogUtils;
import resources.Utils;

public class DeleteTests extends Utils {
    AllureSoftAssert softAssert;
    DeleteRequests deleteRequests;
    AuthenticationRequests authenticationRequests;

    @Parameters({"bookingID"})
    @Test
    public void validDeleteBookingTest(@Optional("1") int bookingID) {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Valid Delete Booking Test");
            testResult.setDescription("This test verifies that a booking can be deleted successfully with valid authentication.");
        });
        softAssert = new AllureSoftAssert();
        deleteRequests = new DeleteRequests();
        authenticationRequests = new AuthenticationRequests();
        Response authResponse = authenticationRequests.createToken("admin", "password123");
        String token = authResponse.jsonPath().getString("token");
        LogUtils.info("Auth Response: "
                + authResponse.asString());

        Response response = deleteRequests.deleteBooking(bookingID, token);
        LogUtils.info("Valid Delete Booking Response Body: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 201, "Status code is not 201");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");

        softAssert.assertAll();
    }

    @Parameters({"incorrectBookingID"})
    @Test
    public void invalidDeleteBookingByIncorrectID(@Optional("-1") int incorrectBookingID) {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Delete Booking By Incorrect ID Test");
            testResult.setDescription("This test verifies that deleting a booking with an incorrect ID returns appropriate error handling.");
        });
        softAssert = new AllureSoftAssert();
        deleteRequests = new DeleteRequests();
        authenticationRequests = new AuthenticationRequests();
        Response authResponse = authenticationRequests.createToken("admin", "password123");
        LogUtils.info("Auth Response: "
                + authResponse.asString());

        String token = authResponse.jsonPath().getString("token");
        Response response = deleteRequests.deleteBooking(incorrectBookingID, token);
        LogUtils.info("Invalid Delete Booking (By Booking ID) Response Body: "
                + response.asString());

        softAssert.assertEquals(response.statusCode(), 405, "Status code is not 405");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");

        softAssert.assertAll();
    }

    @Parameters({"bookingID"})
    @Test
    public void invalidDeleteBookingByIncorrectToken(@Optional("1") int bookingID) {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Delete Booking By Incorrect Token Test");
            testResult.setDescription("This test verifies that deleting a booking fails when an incorrect authentication token is provided.");
        });

        softAssert = new AllureSoftAssert();
        deleteRequests = new DeleteRequests();
        String token = "invalidToken123";
        Response response = deleteRequests.deleteBooking(bookingID, token);
        LogUtils.info("Invalid Delete Booking (By Token) Response Body: "
                + response.asString());

        softAssert.assertEquals(response.statusCode(), 403, "Status code is not 403");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");

        softAssert.assertAll();
    }
}
