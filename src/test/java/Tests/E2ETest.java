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

    String token;
    int bookingID;

    AuthenticationRequests authenticationRequests;
    CreateBookingRequests createBookingRequests;
    GetBookingIDsRequest getBookingIDsRequest;
    PartialUpdateBookingRequest partialUpdateBookingRequest;
    GetBookingByIDRequest getBookingByIDRequest;
    DeleteRequests deleteRequests;

    JsonReader authReader, bookingDataReader, bookingDetailsReader;

    @BeforeClass
    public void setup() {
        authenticationRequests = new AuthenticationRequests();
        createBookingRequests = new CreateBookingRequests();
        getBookingIDsRequest = new GetBookingIDsRequest();
        partialUpdateBookingRequest = new PartialUpdateBookingRequest();
        getBookingByIDRequest = new GetBookingByIDRequest();
        deleteRequests = new DeleteRequests();

        authReader = new JsonReader("auth-data");
        bookingDataReader = new JsonReader("booking-data");
        bookingDetailsReader = new JsonReader("booking-details-data");

        LogUtils.info("Generating Token (BeforeClass)...");
        String username = authReader.getJsonData("username");
        String password = authReader.getJsonData("password");

        Response authResponse = authenticationRequests.createToken(username, password);
        token = authResponse.jsonPath().getString("token");

        LogUtils.info("Token generated: " + token);

        LogUtils.info("Creating Booking (BeforeClass)...");
        Response createResponse = createBookingRequests.createBooking(
                bookingDataReader.getJsonData("booking-data")
        );
        bookingID = createResponse.jsonPath().getInt("bookingid");

        LogUtils.info("Booking created with ID: " + bookingID);
    }

    @BeforeMethod
    public void init() {
        softAssert = new AllureSoftAssert();
    }

    @Test(priority = 1)
    public void verifyAuthentication() {
        LogUtils.info("Verifying that token is valid...");
        softAssert.assertNotNull(token, "Token must not be null.");
        softAssert.assertAll();
    }

    @Test(priority = 2)
    public void verifyCreatedBookingInIDs() {
        LogUtils.info("Retrieving all booking IDs...");
        Response getIDsResponse = getBookingIDsRequest.getBookingIDs();

        softAssert.assertEquals(getIDsResponse.statusCode(), 200);
        softAssert.assertTrue(
                getIDsResponse.jsonPath().getList("bookingid").contains(bookingID),
                "Booking ID should appear in list"
        );
        softAssert.assertAll();
    }

    @Test(priority = 3)
    public void updateBooking() {
        LogUtils.info("Updating booking ID " + bookingID);

        String firstname = bookingDetailsReader.getJsonData("firstname");
        String lastname = bookingDetailsReader.getJsonData("lastname");

        Response updateResponse = partialUpdateBookingRequest.partialUpdateBooking(
                bookingID, token, firstname, lastname
        );

        softAssert.assertEquals(updateResponse.statusCode(), 200, "Update should return 200");
        softAssert.assertAll();
    }

    @Test(priority = 4)
    public void retrieveUpdatedBooking() {

        LogUtils.info("Retrieving updated booking ID " + bookingID);

        String expectedFirst = bookingDetailsReader.getJsonData("firstname");
        String expectedLast = bookingDetailsReader.getJsonData("lastname");

        Response getBookingResponse = getBookingByIDRequest.getBookingById(bookingID);

        softAssert.assertEquals(getBookingResponse.statusCode(), 200);
        softAssert.assertEquals(getBookingResponse.jsonPath().getString("firstname"), expectedFirst);
        softAssert.assertEquals(getBookingResponse.jsonPath().getString("lastname"), expectedLast);
        softAssert.assertAll();
    }

    @Test(priority = 5)
    public void deleteBooking() {

        LogUtils.info("Deleting booking ID " + bookingID);
        Response deleteResponse = deleteRequests.deleteBooking(bookingID, token);

        softAssert.assertEquals(deleteResponse.statusCode(), 201);
        softAssert.assertAll();
    }
}
