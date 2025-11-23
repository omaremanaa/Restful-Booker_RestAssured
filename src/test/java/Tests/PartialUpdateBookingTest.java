package Tests;

import Requests.AuthenticationRequests;
import Requests.PartialUpdateBookingRequest;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.*;
import resources.AllureSoftAssert;
import Helpers.JsonReader;
import resources.LogUtils;
import resources.Utils;

public class PartialUpdateBookingTest extends Utils {
    PartialUpdateBookingRequest partialUpdateBookingRequest;
    AuthenticationRequests authenticationRequests;
    AllureSoftAssert softAssert;
    JsonReader jsonReader, authJsonReader;

    @BeforeClass
    public void setup(){
        partialUpdateBookingRequest = new PartialUpdateBookingRequest();
        authenticationRequests = new AuthenticationRequests();
        jsonReader = new JsonReader("booking-details-data");
        authJsonReader = new JsonReader("auth-data");
    }
    @BeforeMethod
    public void init(){
        softAssert = new AllureSoftAssert();
    }

    @Test
    public void validPartialUpdateBooking() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Valid Partial Update Booking Test");
            testResult.setDescription("This test verifies that a booking is successfully partially updated when valid data is provided.");
        });
        int id = Integer.parseInt(jsonReader.getJsonData("bookingID"));
        String firstname = jsonReader.getJsonData("firstname");
        String lastname = jsonReader.getJsonData("lastname");
        String username = authJsonReader.getJsonData("username");
        String password = authJsonReader.getJsonData("password");

        Response authResponse = authenticationRequests.createToken(username, password);
        String token = authResponse.jsonPath().getString("token");

        Response response = partialUpdateBookingRequest.partialUpdateBooking(id, token, firstname, lastname);
        System.out.println(response.asString());
        String firstnameResponse = response.jsonPath().getString("firstname");
        String lastnameResponse = response.jsonPath().getString("lastname");
        LogUtils.info("Valid Partial Update Booking Response Body: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 200, "Status code is 200");
        softAssert.assertEquals(firstnameResponse,firstname, "First name is updated correctly");
        softAssert.assertEquals(lastnameResponse, lastname, "Last name is updated correctly");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertAll();
    }

    @Test
    public void invalidPartialUpdateBookingByID() {
     Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Partial Update Booking By ID Test");
            testResult.setDescription("This test verifies that an error is returned when attempting to partially update a booking with an invalid ID.");
        });
        int incorrectBookingID = Integer.parseInt(jsonReader.getJsonData("incorrectBookingID"));
        String username = authJsonReader.getJsonData("username");
        String password = authJsonReader.getJsonData("password");

        Response authResponse = authenticationRequests.createToken(username, password);
        String token = authResponse.jsonPath().getString("token");

        Response response = partialUpdateBookingRequest.partialUpdateBooking(incorrectBookingID, token, "", "");
        LogUtils.info("Invalid Partial Update Booking (Non existing Id) Response Body: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 405, "Status code is 405");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertAll();
    }

    @Test
    public void invalidPartialUpdateBookingByToken() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Invalid Partial Update Booking By Token Test");
            testResult.setDescription("This test verifies that an error is returned when attempting to partially update a booking with an invalid token.");
        });
        int id = Integer.parseInt(jsonReader.getJsonData("bookingID"));
        String firstname = jsonReader.getJsonData("firstname");
        String lastname = jsonReader.getJsonData("lastname");

        String token = "invalidToken";

        Response response = partialUpdateBookingRequest.partialUpdateBooking(id, token, firstname, lastname);
        LogUtils.info("Invalid Partial Update Booking (Incorrect Token) Response Body: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 403, "Status code is 403");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertAll();
    }

}
