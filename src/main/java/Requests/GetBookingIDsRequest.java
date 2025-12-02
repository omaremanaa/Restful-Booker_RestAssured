package Requests;

import Helpers.JsonReader;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetBookingIDsRequest {
    JsonReader jsonReader = new JsonReader("api-data");
    private String pathPar = jsonReader.getJsonData("bookingPath");

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
