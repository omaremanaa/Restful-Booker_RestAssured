package Requests;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetBookingIDsRequest {
    private String pathPar = "/booking";

    public Response getBookingIDs() {
        Response bookingIDsResponse;
        bookingIDsResponse = given()
                .header("Content-Type", "application/json")
                .when()
                .get(pathPar)
                .then()
                .extract()
                .response();
        return bookingIDsResponse;
    }
}
