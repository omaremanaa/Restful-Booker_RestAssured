package Tests;

import Requests.DeleteRequests;
import Helpers.JsonReader;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.*;
import resources.AllureSoftAssert;
import resources.LogUtils;
import resources.Utils;

public class DeleteTests extends Utils {
    AllureSoftAssert softAssert;
    DeleteRequests deleteRequests;
    JsonReader jsonReader;

    @BeforeClass
    public void setup(){
        deleteRequests = new DeleteRequests();
        jsonReader = new JsonReader("booking-details-data");
    }
    @BeforeMethod
    public void init(){
        softAssert = new AllureSoftAssert();
    }
    @Test
    public void validDeleteBookingTest() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Valid Delete Booking Test");
            testResult.setDescription("This test verifies that a booking can be deleted successfully with valid authentication.");
        });

        Response response = deleteRequests.deleteBooking(bookingID, token);
        LogUtils.info("Valid Delete Booking Response Body: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 201, "Status code is not 201");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");

        softAssert.assertAll();
    }

    @Test
    public void invalidDeleteBookingByIncorrectID() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Delete Booking By Incorrect ID Test");
            testResult.setDescription("This test verifies that deleting a booking with an incorrect ID returns appropriate error handling.");
        });

        int incorrectBookingID = Integer.parseInt(jsonReader.getJsonData("incorrectBookingID"));

        Response response = deleteRequests.deleteBooking(incorrectBookingID, token);
        LogUtils.info("Invalid Delete Booking (By Booking ID) Response Body: "
                + response.asString());

        softAssert.assertEquals(response.statusCode(), 405, "Status code is not 405");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");

        softAssert.assertAll();
    }

    @Test
    public void invalidDeleteBookingByIncorrectToken() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Delete Booking By Incorrect Token Test");
            testResult.setDescription("This test verifies that deleting a booking fails when an incorrect authentication token is provided.");
        });

        String token = "invalidToken123";
        Response response = deleteRequests.deleteBooking(bookingID, token);
        LogUtils.info("Invalid Delete Booking (By Token) Response Body: "
                + response.asString());

        softAssert.assertEquals(response.statusCode(), 403, "Status code is not 403");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");

        softAssert.assertAll();
    }
}
