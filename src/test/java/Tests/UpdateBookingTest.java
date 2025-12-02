package Tests;

import Requests.AuthenticationRequests;
import Requests.UpdateBookingRequest;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.*;
import resources.AllureSoftAssert;
import Helpers.JsonReader;
import resources.LogUtils;
import resources.Utils;

public class UpdateBookingTest extends Utils {
    AllureSoftAssert softAssert;
    UpdateBookingRequest updateBookingRequest;
    JsonReader jsonReader, authJsonReader, bookingDetailsJsonReader;

    @BeforeClass
    public void setup(){
        updateBookingRequest = new UpdateBookingRequest();
        jsonReader = new JsonReader("booking-data");
        authJsonReader = new JsonReader("auth-data");
        bookingDetailsJsonReader = new JsonReader("booking-details-data");
    }
    @BeforeMethod
    public void init(){
        softAssert = new AllureSoftAssert();
    }

    @Test
    public void validUpdateBookingTest() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Valid Update Booking Test");
            testResult.setDescription("This test verifies that a booking can be updated successfully with valid data and authentication.");
        });
        String body = jsonReader.getJsonData("booking-data");

        Response response = updateBookingRequest.updateBooking(bookingID, token, body);
        LogUtils.info("Valid Update Booking Response: "
                + response.asString());
        softAssert.assertEquals(response.statusCode(), 200, "Expected status code 200");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertAll();

    }

    @Test
    public void invalidUpdateBookingByIncorrectToken() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Update Booking Test");
            testResult.setDescription("This test verifies that updating a booking fails when an incorrect authentication token is provided.");
        });
        String body = jsonReader.getJsonData("booking-data");
        String invalidToken = "invalidToken123";

        Response response = updateBookingRequest.updateBooking(2, invalidToken, body);
        LogUtils.info("Invalid Update Booking (By Incorrect Token) Response: "
                + response.asString());

        softAssert.assertEquals(response.statusCode(), 403, "Expected status code 403");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertAll();

    }

    @Test
    public void invalidUpdateBookingByIncorrectID() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Update Booking Test By Incorrect ID");
            testResult.setDescription("This test verifies that updating a booking fails when an incorrect booking ID is provided.");
        });
        int incorrectID = Integer.parseInt(bookingDetailsJsonReader.getJsonData("incorrectBookingID"));
        String body = jsonReader.getJsonData("booking-data");

        Response response = updateBookingRequest.updateBooking(incorrectID, token, body);
        LogUtils.info("Invalid Update Booking (By Nonexistent ID) Response: "
                + response.asString());
        softAssert.assertEquals(response.statusCode(), 405, "Expected status code 405");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertAll();

    }

}
