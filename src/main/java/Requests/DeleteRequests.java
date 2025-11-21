package Requests;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteRequests {
    private String pathPar = "/booking/{id}";

    public Response deleteBooking(int id, String token) {
        Response deleteBookingResponse;
        deleteBookingResponse = given()
                .pathParam("id", id)
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .when()
                .delete(pathPar)
                .then()
                .extract()
                .response();
        return deleteBookingResponse;
    }
}
