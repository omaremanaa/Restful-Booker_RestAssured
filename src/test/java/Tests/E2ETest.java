package Tests;

import Requests.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import resources.AllureSoftAssert;
import resources.LogUtils;
import resources.Utils;

public class E2ETest extends Utils {
    AllureSoftAssert softAssert;
    AuthenticationRequests authenticationRequests;
    CreateBookingRequests createBookingRequests;
    GetBookingIDsRequest getBookingIDsRequest;
    UpdateBookingRequest updateBookingRequest;
    GetBookingIDRequests getBookingIDRequests;
    DeleteRequests deleteRequests;

    @Test
    public void endToEndBookingTest() {
        softAssert = new AllureSoftAssert();
        authenticationRequests = new AuthenticationRequests();
        createBookingRequests = new CreateBookingRequests();
        getBookingIDsRequest = new GetBookingIDsRequest();
        updateBookingRequest = new UpdateBookingRequest();
        getBookingIDRequests = new GetBookingIDRequests();
        deleteRequests = new DeleteRequests();
        LogUtils.info("Step 1: Attempting authentication with credentials");

        Response authResponse = authenticationRequests.createToken("admin", "password123");
        String token = authResponse.jsonPath().getString("token");
        LogUtils.info("Authentication response - Status: " +
                authResponse.statusCode()+" , Response time: {"+authResponse.getTime()
                +"}ms, Token: {"+authResponse.getTime()+"}");

        softAssert.assertEquals(authResponse.statusCode(), 200, "Authentication status code is 200");
        softAssert.assertTrue(authResponse.getTime()<2000, "Authentication response time is less than 2000ms");
        softAssert.assertNotNull(token, "Token is not null");


        LogUtils.info("Step 2: Creating booking for Ahmed Magdy, Total Price: 300, Checkin: 2025-11-11, Checkout: 2025-12-12");
        Response createResponse = createBookingRequests.createBooking("Ahmed", "Magdy",
                300, true,
                "2025-11-11", "2025-12-12", "E2E Test");
        int bookingID = createResponse.jsonPath().getInt("bookingid");
        LogUtils.info("Booking creation response - Status: " +
                createResponse.statusCode() + " , Response time: {" + createResponse.getTime() + "}ms, Booking ID: {" + bookingID + "}");

        softAssert.assertEquals(createResponse.statusCode(), 200, "Status code is 200");
        softAssert.assertTrue(createResponse.getTime()<2000, "Response time is less than 2000ms");
        softAssert.assertTrue(bookingID>0, "Booking ID is greater than 0");
        softAssert.assertEquals(createResponse.jsonPath().getString("booking.firstname"), "Ahmed", "Firstname is correct");
        softAssert.assertEquals(createResponse.jsonPath().getString("booking.lastname"), "Magdy", "Lastname is correct");
        softAssert.assertEquals(createResponse.jsonPath().getInt("booking.totalprice"), 300, "Total price is correct");
        softAssert.assertEquals(createResponse.jsonPath().getString("booking.bookingdates.checkin"), "2025-11-11", "Checkin date is correct");
        softAssert.assertEquals(createResponse.jsonPath().getString("booking.bookingdates.checkout"), "2025-12-12", "Checkout date is correct");
        softAssert.assertEquals(createResponse.jsonPath().getString("booking.additionalneeds"), "E2E Test", "Additional needs is correct");

        LogUtils.info("Step 3: Retrieving all booking IDs");
        Response getIDsResponse = getBookingIDsRequest.getBookingIDs();
        LogUtils.info("Get booking IDs response - Status: " +
                getIDsResponse.statusCode() + " , Response time: {" + getIDsResponse.getTime() + "}ms");

        softAssert.assertEquals(getIDsResponse.statusCode(), 200, "Get booking IDs status code is 200");
        softAssert.assertTrue(getIDsResponse.jsonPath().getList("bookingid").contains(bookingID), "New booking ID is present");

        LogUtils.info("Step 4: Updating booking ID " + bookingID + " with new details");
        String updateBody = "{\n" +
                "    \"firstname\": \"Mohamed\",\n" +
                "    \"lastname\": \"Saad\",\n" +
                "    \"totalprice\": 50,\n" +
                "    \"depositpaid\": true,\n" +
                "    \"bookingdates\": {\n" +
                "        \"checkin\": \"2025-11-12\",\n" +
                "        \"checkout\": \"2025-12-12\"\n" +
                "    },\n" +
                "    \"additionalneeds\": \"Actor\"\n" +
                "}";

        Response updateResponse = updateBookingRequest.updateBooking(bookingID, token, updateBody);
        LogUtils.info("Booking update response - Status: " +
                updateResponse.statusCode() + " , Response time: {" + updateResponse.getTime() + "}ms");
        softAssert.assertEquals(updateResponse.statusCode(), 200, "Update booking status code is 200");

        LogUtils.info("Step 5: Retrieving updated booking details for ID " + bookingID);
        Response getBookingResponse = getBookingIDRequests.getBookingById(bookingID);
        LogUtils.info("Get booking response - Status: " +
                getBookingResponse.statusCode() + " , Response time: {" + getBookingResponse.getTime() + "}ms");
        LogUtils.info("Updated booking details: " + getBookingResponse.asString());
        softAssert.assertEquals(getBookingResponse.statusCode(), 200, "Get booking by ID status code is 200");
        softAssert.assertEquals(getBookingResponse.jsonPath().getString("firstname"), "Mohamed", "First name is updated");
        softAssert.assertEquals(getBookingResponse.jsonPath().getString("lastname"), "Saad", "Last name is updated");
        softAssert.assertEquals(getBookingResponse.jsonPath().getInt("totalprice"), 50, "Total price is correct");
        softAssert.assertEquals(getBookingResponse.jsonPath().getString("bookingdates.checkin"), "2025-11-12", "Checkin date is updated");
        softAssert.assertEquals(getBookingResponse.jsonPath().getString("bookingdates.checkout"), "2025-12-12", "Checkout date is updated");
        softAssert.assertEquals(getBookingResponse.jsonPath().getString("additionalneeds"), "Actor", "Additional needs is updated");

        LogUtils.info("Step 6: Deleting booking ID " + bookingID);
        Response deleteResponse = deleteRequests.deleteBooking(bookingID, token);
        LogUtils.info("Booking deletion response - Status: " +
                deleteResponse.statusCode() + " , Response time: {" + deleteResponse.getTime() + "}ms");

        softAssert.assertEquals(deleteResponse.statusCode(), 201, "Delete booking status code is 201");
        softAssert.assertAll();
    }
}
