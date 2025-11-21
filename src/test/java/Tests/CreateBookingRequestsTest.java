package Tests;

import Requests.CreateBookingRequests;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import resources.AllureSoftAssert;
import resources.LogUtils;
import resources.Utils;

public class CreateBookingRequestsTest extends Utils {
    CreateBookingRequests createBookingRequests;
    AllureSoftAssert softAssert;

    @Parameters({"firstname", "lastname",
            "totalprice", "depositpaid",
            "checkin", "checkout", "additionalneeds"})
    @Test
    public void validCreateBookingTest(String firstname, String lastname, int totalprice,
                                       boolean depositpaid, String checkin,
                                       String checkout, String additionalneeds) {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Valid Create Booking Test");
            testResult.setDescription("This test verifies that a booking is successfully created when valid data is provided.");
        });

        Response response;
        softAssert = new AllureSoftAssert();
        createBookingRequests = new CreateBookingRequests();
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
        softAssert.assertEquals(additionalneedsResponse, additionalneedsResponse, "Additional needs is correct");

    }

    @Parameters({"body"})
    @Test
    public void invalidCreateBookingTest(String body) {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Create Booking Test");
            testResult.setDescription("This test verifies that booking creation fails when invalid data is provided.");
        });

        Response response;
        softAssert = new AllureSoftAssert();
        createBookingRequests = new CreateBookingRequests();
        response = createBookingRequests.createBookings(body);
        LogUtils.info("Invalid Create Booking Response Body: " + response.asString());

        softAssert.assertNotEquals(response.statusCode(), 200, "Status code is not 200");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
    }
}
