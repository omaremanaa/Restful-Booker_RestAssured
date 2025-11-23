package Requests;

import Helpers.JsonReader;
import Helpers.Payload;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CreateBookingRequests {

    JsonReader jsonReader = new JsonReader("api-data");
    private String pathPar = jsonReader.getJsonData("bookingPath");

    public Response createBooking(String firstname, String lastname,
                                  int totalprice, boolean depositpaid,
                                  String checkin, String checkout, String additionalneeds) {

        Payload payload = new Payload();

        Response response;
        response =
                given()
                        .header("Content-Type", "application/json")
                        .body(payload.createBookingPayload(firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds))
                        .when()
                        .post(pathPar)
                        .then()
                        .extract().response();
        return response;
    }

    public Response createBooking(String body) {
        Response response;
        response =
                given()
                        .header("Content-Type", "application/json")
                        .body(body)
                        .when()
                        .post(pathPar)
                        .then()
                        .extract().response();
        return response;
    }
}