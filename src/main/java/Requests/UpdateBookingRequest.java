package Requests;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UpdateBookingRequest {
    private String pathPar = "/booking/{id}";

    public Response updateBooking(int id, String token, String updateBody) {
        Response updateBookingResponse;
        updateBookingResponse = given()
                .pathParam("id", id)
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body(updateBody)
                .when()
                .put(pathPar)
                .then()
                .extract()
                .response();
        return updateBookingResponse;
    }
}
