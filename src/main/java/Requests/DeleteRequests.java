package Requests;

import Helpers.JsonReader;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteRequests {
    JsonReader jsonReader = new JsonReader("api-data");
    private String pathPar = jsonReader.getJsonData("bookingIdPath");

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
