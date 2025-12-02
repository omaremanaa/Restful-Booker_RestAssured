package Tests;

import Helpers.JsonReader;
import Requests.*;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import resources.AllureSoftAssert;
import resources.LogUtils;
import resources.Utils;

public class EndToEndTest extends Utils {
    AllureSoftAssert softAssert;
    PingRequests pingRequests;
    AuthenticationRequests authenticationRequests;
    CreateBookingRequests createBookingRequests;
    GetBookingIDsRequest getBookingIDsRequest;
    GetBookingByIDRequest getBookingByIDRequest;
    PartialUpdateBookingRequest partialUpdateBookingRequest;
    UpdateBookingRequest updateBookingRequest;
    DeleteRequests deleteRequests;
    JsonReader bookingDataJsonReader, authJsonReader, bookingDetailsJsonReader ;

    @BeforeClass
    public void setup(){
        pingRequests = new PingRequests();
        authenticationRequests = new AuthenticationRequests();
        createBookingRequests = new CreateBookingRequests();
        getBookingIDsRequest = new GetBookingIDsRequest();
        getBookingByIDRequest = new GetBookingByIDRequest();
        partialUpdateBookingRequest = new PartialUpdateBookingRequest();
        updateBookingRequest = new UpdateBookingRequest();
        deleteRequests = new DeleteRequests();
    }
    @BeforeMethod
    public void init(){
        softAssert = new AllureSoftAssert();
        authJsonReader = new JsonReader("auth-data");
        username = authJsonReader.getJsonData("username");
        password = authJsonReader.getJsonData("password");
    }

    @Test(priority = 1)
    public void checkServerWorking() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("E2E Step 1: API Availability Check");
            testResult.setDescription("Ensures the service is up and reachable before starting the end-to-end workflow.");
        });

        Response response = pingRequests.healthCheck();
        LogUtils.info("E2E Step 1 → Ping endpoint responded: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 201, "Status code is 201");
        softAssert.assertTrue(response.getTime() < 3000, "Response time is less than 3000ms");
        softAssert.assertAll();
    }

    @Test(priority = 2)
    public void authenticateUser() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("E2E Step 2: User Authentication");
            testResult.setDescription("Authenticates the test user and generates a token to be used throughout the end-to-end scenario.");
        });

        Response response = authenticationRequests.createToken(username, password);
        token = response.jsonPath().getString("token");
        System.out.println(token+"tooooken");
        LogUtils.info("E2E Step 2 → Authentication response: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 200, "Status code is 200");
        softAssert.assertNotNull(token, "Token is not null");
        softAssert.assertTrue(response.getTime() < 2000, " Response time is less than 2000ms");
        softAssert.assertAll();
    }

    @Test(priority = 3)
    public void createBooking() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("E2E Step 3: Create Booking");
            testResult.setDescription("Creates a new booking as part of the end-to-end workflow and stores the generated booking ID.");
        });

        bookingDataJsonReader = new JsonReader("booking-data");

        String firstname = bookingDataJsonReader.getJsonData("firstname");
        String lastname = bookingDataJsonReader.getJsonData("lastname");
        int totalprice = Integer.parseInt(bookingDataJsonReader.getJsonData("totalprice"));
        boolean depositpaid = Boolean.parseBoolean(bookingDataJsonReader.getJsonData("depositpaid"));
        String checkin = bookingDataJsonReader.getJsonData("bookingdates.checkin");
        String checkout = bookingDataJsonReader.getJsonData("bookingdates.checkout");
        String additionalneeds = bookingDataJsonReader.getJsonData("additionalneeds");

        Response response;
        response = createBookingRequests.createBooking(firstname, lastname, totalprice,
                depositpaid, checkin, checkout, additionalneeds);

        LogUtils.info("E2E Step 3 → Booking creation response: " + response.asString());

        bookingID = response.jsonPath().getInt("bookingid");

        String firstnameResponse = response.jsonPath().getString("booking.firstname");
        String lastnameResponse = response.jsonPath().getString("booking.lastname");
        int totalpriceResponse = response.jsonPath().getInt("booking.totalprice");
        boolean depositpaidResponse = response.jsonPath().getBoolean("booking.depositpaid");
        String checkinResponse = response.jsonPath().getString("booking.bookingdates.checkin");
        String checkoutResponse = response.jsonPath().getString("booking.bookingdates.checkout");
        String additionalneedsResponse = response.jsonPath().getString("booking.additionalneeds");


        softAssert.assertEquals(response.statusCode(), 200, "Status code is 200");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertTrue(bookingID>0, "Booking ID is greater than 0");
        softAssert.assertEquals(firstnameResponse, firstname, "Firstname is correct");
        softAssert.assertEquals(lastnameResponse, lastname, "Lastname is correct");
        softAssert.assertEquals(totalpriceResponse, totalprice, "Total price is correct");
        softAssert.assertEquals(depositpaidResponse, depositpaid, "Deposit paid is correct");
        softAssert.assertEquals(checkinResponse, checkin, "Checkin date is correct");
        softAssert.assertEquals(checkoutResponse, checkout, "Checkout date is correct");
        softAssert.assertEquals(additionalneedsResponse, additionalneeds, "Additional needs is correct");
        softAssert.assertAll();

    }

    @Test(priority = 4)
    public void checkBookingCreated() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("E2E Step 4: Verify Booking Appears in All Booking IDs");
            testResult.setDescription("Confirms that the newly created booking ID is present in the list of all bookings.");
        });

        Response response = getBookingIDsRequest.getBookingIDs();
        LogUtils.info("E2E Step 4 → Booking IDs response: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 200, "Status code is 200");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertTrue(response.jsonPath().getList("bookingid").contains(bookingID), "New booking ID is present");

        softAssert.assertAll();
    }

    @Test(priority = 5)
    public void updateBooking() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("E2E Step 5: Update Booking Details");
            testResult.setDescription("Updates specific fields in the existing booking as part of the end-to-end scenario.");
        });
        bookingDetailsJsonReader = new JsonReader("booking-details-data");

        String firstname = bookingDetailsJsonReader.getJsonData("firstname");
        String lastname = bookingDetailsJsonReader.getJsonData("lastname");

        Response response = partialUpdateBookingRequest.partialUpdateBooking(bookingID, token, firstname, lastname);
        String firstnameResponse = response.jsonPath().getString("firstname");
        String lastnameResponse = response.jsonPath().getString("lastname");
        LogUtils.info("E2E Step 5 → Partial update response: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 200, "Status code is 200");
        softAssert.assertEquals(firstnameResponse,firstname, "First name is updated correctly");
        softAssert.assertEquals(lastnameResponse, lastname, "Last name is updated correctly");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertAll();
    }

    @Test(priority = 6)
    public void getBookingDetails() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("E2E Step 6: Retrieve Updated Booking");
            testResult.setDescription("Fetches the latest details of the booking after updates to confirm the changes.");
        });

        Response response = getBookingByIDRequest.getBookingById(bookingID);
        LogUtils.info("E2E Step 6 → Booking details response: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 200, "Status code is not 200");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");

        softAssert.assertAll();
    }

    @Test(priority = 7)
    public void deleteBooking() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("E2E Step 7: Delete Booking (Cleanup)");
            testResult.setDescription("Deletes the booking created during this end-to-end scenario and validates successful cleanup.");
        });

        Response response = deleteRequests.deleteBooking(bookingID, token);
        LogUtils.info("E2E Step 7 → Delete response: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 201, "Status code is not 201");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");

        softAssert.assertAll();
    }


    @Test(priority = 8)
    public void checkBookingDeleted() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("E2E Step 8: Verify Booking Removed in All Booking IDs");
            testResult.setDescription("Confirms that the deleted booking ID is no longer in the list of all bookings.");
        });

        Response response = getBookingIDsRequest.getBookingIDs();
        LogUtils.info("E2E Step 8 → Booking IDs response: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 200, "Status code is 200");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertFalse(response.jsonPath().getList("bookingid").contains(bookingID), "Deleted booking ID should NOT be present");

        softAssert.assertAll();
    }

}
