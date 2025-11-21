package Tests;

import Requests.PingRequests;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import resources.AllureSoftAssert;
import resources.LogUtils;
import resources.Utils;

public class PingRequestsTest extends Utils {
    PingRequests pingRequestsEndpoint;
    AllureSoftAssert softAssert;
    @Test
    public void validateOnline() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName("Ping Health Check Test");
            testResult.setDescription("This test verifies that the ping endpoint is online and responsive.");
        });
        softAssert = new AllureSoftAssert();
        pingRequestsEndpoint = new PingRequests();
        Response response = pingRequestsEndpoint.healthCheck();
        LogUtils.info("Ping Response Body: " + response.asString());

        softAssert.assertEquals(response.statusCode(), 201, "Status code is 201");
        softAssert.assertTrue(response.getTime() < 3000, "Response time is less than 3000ms");
        softAssert.assertAll();
    }

}