package Requests;

import Helpers.JsonReader;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetBookingByIDRequest {
    JsonReader jsonReader = new JsonReader("api-data");
    private String pathPar = jsonReader.getJsonData("bookingIdPath");

    public Response getBookingById(int id) {
        Response singleBookingResponse;
        singleBookingResponse = given().pathParam("id", id)
                .header("Content-Type", "application/json")
                .when()
                .get(pathPar)
                .then()
                .extract()
                .response();
        return singleBookingResponse;

    }


}
