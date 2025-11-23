package Requests;

import Helpers.JsonReader;
import Helpers.Payload;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PartialUpdateBookingRequest {
    JsonReader jsonReader = new JsonReader("api-data");
    private String pathPar = jsonReader.getJsonData("bookingIdPath");

    public Response partialUpdateBooking(int id, String token, String firstname, String lastname) {
        Payload payload = new Payload();
        Response response;

        response = given()
                .pathParam("id", id)
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .body(payload.partialUpdateBookingPayload(firstname, lastname))
                .when()
                .patch(pathPar)
                .then()
                .extract()
                .response();
        return response;
    }
}
