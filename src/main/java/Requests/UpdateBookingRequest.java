package Requests;

import Helpers.JsonReader;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UpdateBookingRequest {
    JsonReader jsonReader = new JsonReader("api-data");
    private String pathPar = jsonReader.getJsonData("bookingIdPath");

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
