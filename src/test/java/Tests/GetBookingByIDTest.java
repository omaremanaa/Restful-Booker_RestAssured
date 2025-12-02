package Tests;

import Requests.GetBookingByIDRequest;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.*;
import resources.AllureSoftAssert;
import Helpers.JsonReader;
import resources.LogUtils;
import resources.Utils;

public class GetBookingByIDTest extends Utils {
    GetBookingByIDRequest getBookingByIDRequest;
    AllureSoftAssert softAssert;
    JsonReader jsonReader;

    @BeforeClass
    public void setup(){
        getBookingByIDRequest = new GetBookingByIDRequest();
        jsonReader = new JsonReader("booking-details-data");
    }
    @BeforeMethod
    public void init(){
        softAssert = new AllureSoftAssert();
    }

    @Test
    public void validGetBookingDetails() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Valid Get Booking By ID Test");
            testResult.setDescription("This test verifies that a booking can be retrieved by its ID and contains all expected fields.");
        });

        Response response = getBookingByIDRequest.getBookingById(bookingID);
        LogUtils.info("Valid Get Booking By ID Response Body: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 200, "Status code is not 200");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");

        softAssert.assertAll();
    }


    @Test
    public void invalidGetBookingByIncorrectID() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Get Booking By ID Test");
            testResult.setDescription("This test verifies that retrieving a booking by an incorrect ID returns appropriate error handling.");
        });
        int incorrectBookingID = Integer.parseInt(jsonReader.getJsonData("incorrectBookingID"));

        Response response = getBookingByIDRequest.getBookingById(incorrectBookingID);
        LogUtils.info("Invalid Get Booking By ID (Non existent ID) Response Body: "
                + response.asString());

        softAssert.assertEquals(response.statusCode(), 404, "Status code is not 404");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertAll();
    }
}