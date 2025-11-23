package Tests;

import Requests.CreateBookingRequests;
import Helpers.JsonReader;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.*;
import resources.AllureSoftAssert;
import resources.LogUtils;
import resources.Utils;

public class CreateBookingRequestsTest extends Utils {
    CreateBookingRequests createBookingRequests;
    AllureSoftAssert softAssert;
    JsonReader jsonReader;

    @BeforeClass
    public void setup(){
        createBookingRequests = new CreateBookingRequests();
    }
    @BeforeMethod
    public void init(){
        softAssert = new AllureSoftAssert();
    }
    @Test
    public void validCreateBookingTest() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Valid Create Booking Test");
            testResult.setDescription("This test verifies that a booking is successfully created when valid data is provided.");
        });
        jsonReader = new JsonReader("booking-data");

        String firstname = jsonReader.getJsonData("firstname");
        String lastname = jsonReader.getJsonData("lastname");
        int totalprice = Integer.parseInt(jsonReader.getJsonData("totalprice"));
        boolean depositpaid = Boolean.parseBoolean(jsonReader.getJsonData("depositpaid"));
        String checkin = jsonReader.getJsonData("bookingdates.checkin");
        String checkout = jsonReader.getJsonData("bookingdates.checkout");
        String additionalneeds = jsonReader.getJsonData("additionalneeds");

        Response response;
        response = createBookingRequests.createBooking(firstname, lastname, totalprice,
                depositpaid, checkin, checkout, additionalneeds);

        LogUtils.info("Valid Create Booking Response Body: " + response.asString());

        int bookingid = response.jsonPath().getInt("bookingid");
        String firstnameResponse = response.jsonPath().getString("booking.firstname");
        String lastnameResponse = response.jsonPath().getString("booking.lastname");
        int totalpriceResponse = response.jsonPath().getInt("booking.totalprice");
        boolean depositpaidResponse = response.jsonPath().getBoolean("booking.depositpaid");
        String checkinResponse = response.jsonPath().getString("booking.bookingdates.checkin");
        String checkoutResponse = response.jsonPath().getString("booking.bookingdates.checkout");
        String additionalneedsResponse = response.jsonPath().getString("booking.additionalneeds");


        softAssert.assertEquals(response.statusCode(), 200, "Status code is 200");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertTrue(bookingid > 0, "Booking ID is greater than 0");
        softAssert.assertEquals(firstnameResponse, firstname, "Firstname is correct");
        softAssert.assertEquals(lastnameResponse, lastname, "Lastname is correct");
        softAssert.assertEquals(totalpriceResponse, totalprice, "Total price is correct");
        softAssert.assertEquals(depositpaidResponse, depositpaid, "Deposit paid is correct");
        softAssert.assertEquals(checkinResponse, checkin, "Checkin date is correct");
        softAssert.assertEquals(checkoutResponse, checkout, "Checkout date is correct");
        softAssert.assertEquals(additionalneedsResponse, additionalneeds, "Additional needs is correct");
        softAssert.assertAll();

    }

    @Test
    public void invalidCreateBookingTest() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Create Booking Test");
            testResult.setDescription("This test verifies that booking creation fails when invalid data is provided.");
        });
        jsonReader = new JsonReader("invalid-body-data");
        String body = jsonReader.getJsonData("body");
        Response response;
        response = createBookingRequests.createBooking(body);
        LogUtils.info("Invalid Create Booking Response Body: " + response.asString());

        softAssert.assertNotEquals(response.statusCode(), 200, "Status code is not 200");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertAll();

    }

}
