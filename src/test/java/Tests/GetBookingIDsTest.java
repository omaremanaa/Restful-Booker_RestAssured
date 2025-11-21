package Tests;

import Requests.GetBookingIDsRequest;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import resources.AllureSoftAssert;
import resources.LogUtils;
import resources.Utils;

public class GetBookingIDsTest extends Utils {
    GetBookingIDsRequest getBookingIDsRequest;
    AllureSoftAssert soft;

    @Test
    public void validGetBookingIDsTest() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Valid Get Booking IDs Test");
            testResult.setDescription("This test verifies that booking IDs can be retrieved successfully.");
        });
        getBookingIDsRequest = new GetBookingIDsRequest();
        soft = new AllureSoftAssert();

        Response response = getBookingIDsRequest.getBookingIDs();
        LogUtils.info("Get Booking IDs Response Body: " + response.asString());

        soft.assertEquals(response.statusCode(), 200, "Status code is 200");
        soft.assertTrue(response.getTime() < 2000, "Response time is less than 2000ms");
        soft.assertTrue(!response.jsonPath().getList("bookingid").isEmpty(), "At least one booking ID is returned");

        soft.assertAll();
    }
}
