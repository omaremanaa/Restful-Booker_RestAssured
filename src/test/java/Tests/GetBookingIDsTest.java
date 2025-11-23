package Tests;

import Requests.GetBookingIDsRequest;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import resources.AllureSoftAssert;
import resources.LogUtils;
import resources.Utils;

public class GetBookingIDsTest extends Utils {
    GetBookingIDsRequest getBookingIDsRequest;
    AllureSoftAssert softAssert;

    @BeforeClass
    public void setup(){
        getBookingIDsRequest = new GetBookingIDsRequest();
    }
    @BeforeMethod
    public void init(){
        softAssert = new AllureSoftAssert();
    }

    @Test
    public void validGetBookingIDsTest() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Valid Get Booking IDs Test");
            testResult.setDescription("This test verifies that booking IDs can be retrieved successfully.");
        });

        Response response = getBookingIDsRequest.getBookingIDs();
        LogUtils.info("Get Booking IDs Response Body: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 200, "Status code is 200");
        softAssert.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        softAssert.assertTrue(!response.jsonPath().getList("bookingid").isEmpty(), "At least one booking ID is returned");

        softAssert.assertAll();
    }
}
