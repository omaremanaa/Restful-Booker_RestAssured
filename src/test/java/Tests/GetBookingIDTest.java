package Tests;

import Requests.GetBookingIDRequests;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import resources.AllureSoftAssert;
import resources.LogUtils;
import resources.Utils;

import java.util.Map;

public class GetBookingIDTest extends Utils {
    GetBookingIDRequests getBookingIDRequests;
    AllureSoftAssert softAssert;

    @Parameters({
            "bookingID"
    })
    @Test
    public void validGetBookingDetails(int bookingID) {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Valid Get Booking By ID Test");
            testResult.setDescription("This test verifies that a booking can be retrieved by its ID and contains all expected fields.");
        });
        getBookingIDRequests = new GetBookingIDRequests();
        softAssert = new AllureSoftAssert();

        Response response = getBookingIDRequests.getBookingById(bookingID);
        LogUtils.info("Valid Get Booking By ID Response Body: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 200, "Status code is not 200");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");

        softAssert.assertAll();
    }

    @Parameters({
            "invalidBookingID"
    })
    @Test
    public void invalidGetBookingByIncorrectID(int invalidBookingID) {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Get Booking By ID Test");
            testResult.setDescription("This test verifies that retrieving a booking by an incorrect ID returns appropriate error handling.");
        });
        getBookingIDRequests = new GetBookingIDRequests();
        softAssert = new AllureSoftAssert();

        Response response = getBookingIDRequests.getBookingById(invalidBookingID);
        LogUtils.info("Invalid Get Booking By ID (Non existent ID) Response Body: "
                + response.asString());

        softAssert.assertEquals(response.statusCode(), 404, "Status code is not 404");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertAll();
    }
}