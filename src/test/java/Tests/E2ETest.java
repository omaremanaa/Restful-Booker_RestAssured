package Tests;

import Requests.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import resources.AllureSoftAssert;
import Helpers.JsonReader;
import resources.LogUtils;
import resources.Utils;

public class E2ETest extends Utils {
    AllureSoftAssert softAssert;
    AuthenticationRequests authenticationRequests;
    CreateBookingRequests createBookingRequests;
    GetBookingIDsRequest getBookingIDsRequest;
    PartialUpdateBookingRequest partialUpdateBookingRequest;
    GetBookingByIDRequest getBookingByIDRequest;
    DeleteRequests deleteRequests;
    JsonReader authReader, bookingDataReader, bookingDetailsReader;

    @BeforeClass
    public void setup(){
        authenticationRequests = new AuthenticationRequests();
        createBookingRequests = new CreateBookingRequests();
        getBookingIDsRequest = new GetBookingIDsRequest();
        partialUpdateBookingRequest = new PartialUpdateBookingRequest();
        getBookingByIDRequest = new GetBookingByIDRequest();
        deleteRequests = new DeleteRequests();
        authReader = new JsonReader("auth-data");
        bookingDataReader = new JsonReader("booking-data");
        bookingDetailsReader = new JsonReader("booking-details-data");
    }
    @BeforeMethod
    public void init(){
        softAssert = new AllureSoftAssert();
    }
    @Test
    public void endToEndBookingTest() {
        LogUtils.info("Step 1: Attempting authentication with credentials");
        String username = authReader.getJsonData("username");
        String password = authReader.getJsonData("password");

        Response authResponse = authenticationRequests.createToken(username, password);
        String token = authResponse.jsonPath().getString("token");
        LogUtils.info("Authentication response - Status: " +
                authResponse.statusCode() + " , Response time: {" + authResponse.getTime()
                + "}ms, Token: {" + authResponse.getTime() + "}");

        softAssert.assertEquals(authResponse.statusCode(), 200, "Authentication status code is 200");
        softAssert.assertTrue(authResponse.getTime() < 2000, "Authentication response time is less than 2000ms");
        softAssert.assertNotNull(token, "Token is not null");

        System.out.println("Reader: "+ bookingDataReader.getJsonData("booking-data"));
        LogUtils.info("Step 2: Creating booking for Ahmed Magdy, Total Price: 300, Checkin: 2025-11-11, Checkout: 2025-12-12");
        Response createResponse = createBookingRequests.createBooking(bookingDataReader.getJsonData("booking-data"));
        int bookingID = createResponse.jsonPath().getInt("bookingid");
        LogUtils.info("Booking creation response - Status: " +
                createResponse.statusCode() + " , Response time: {" + createResponse.getTime() + "}ms, Booking ID: {" + bookingID + "}");

        softAssert.assertEquals(createResponse.statusCode(), 200, "Status code is 200");
        softAssert.assertTrue(createResponse.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertTrue(bookingID > 0, "Booking ID is greater than 0");
//        softAssert.assertEquals(createResponse.jsonPath().getString("booking.firstname"), "Ahmed", "Firstname is correct");
//        softAssert.assertEquals(createResponse.jsonPath().getString("booking.lastname"), "Magdy", "Lastname is correct");
//        softAssert.assertEquals(createResponse.jsonPath().getInt("booking.totalprice"), 300, "Total price is correct");
//        softAssert.assertEquals(createResponse.jsonPath().getString("booking.bookingdates.checkin"), "2025-11-11", "Checkin date is correct");
//        softAssert.assertEquals(createResponse.jsonPath().getString("booking.bookingdates.checkout"), "2025-12-12", "Checkout date is correct");
//        softAssert.assertEquals(createResponse.jsonPath().getString("booking.additionalneeds"), "E2E Test", "Additional needs is correct");

        LogUtils.info("Step 3: Retrieving all booking IDs");
        Response getIDsResponse = getBookingIDsRequest.getBookingIDs();
        LogUtils.info("Get booking IDs response - Status: " +
                getIDsResponse.statusCode() + " , Response time: {" + getIDsResponse.getTime() + "}ms");

        softAssert.assertEquals(getIDsResponse.statusCode(), 200, "Get booking IDs status code is 200");
        softAssert.assertTrue(getIDsResponse.jsonPath().getList("bookingid").contains(bookingID), "New booking ID is present");

        LogUtils.info("Step 4: Updating booking ID " + bookingID + " with new details");

        String firstname = bookingDetailsReader.getJsonData("firstname");
        String lastname = bookingDetailsReader.getJsonData("lastname");

        Response updateResponse = partialUpdateBookingRequest.partialUpdateBooking(bookingID, token, firstname, lastname);

        LogUtils.info("Booking update response - Status: " +
                updateResponse.statusCode() + " , Response time: {" + updateResponse.getTime() + "}ms");
        softAssert.assertEquals(updateResponse.statusCode(), 200, "Update booking status code is 200");

        LogUtils.info("Step 5: Retrieving updated booking details for ID " + bookingID);
        Response getBookingResponse = getBookingByIDRequest.getBookingById(bookingID);
        LogUtils.info("Get booking response - Status: " +
                getBookingResponse.statusCode() + " , Response time: {" + getBookingResponse.getTime() + "}ms");
        LogUtils.info("Updated booking details: " + getBookingResponse.asString());
        softAssert.assertEquals(getBookingResponse.statusCode(), 200, "Get booking by ID status code is 200");
        softAssert.assertEquals(getBookingResponse.jsonPath().getString("firstname"), firstname, "First name is updated");
        softAssert.assertEquals(getBookingResponse.jsonPath().getString("lastname"), lastname, "Last name is updated");

        LogUtils.info("Step 6: Deleting booking ID " + bookingID);
        Response deleteResponse = deleteRequests.deleteBooking(bookingID, token);
        LogUtils.info("Booking deletion response - Status: " +
                deleteResponse.statusCode() + " , Response time: {" + deleteResponse.getTime() + "}ms");

        softAssert.assertEquals(deleteResponse.statusCode(), 201, "Delete booking status code is 201");
        softAssert.assertAll();
    }
}
