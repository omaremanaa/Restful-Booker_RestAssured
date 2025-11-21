package Requests;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class GetBookingIDRequests {

    private String pathPar = "/booking/{id}";

    public Response getBookingById(int id) {
        Response singleBookingResponse;
        singleBookingResponse = given().pathParam("id",id)
                .header("Content-Type", "application/json")
                .when()
                .get(pathPar)
                .then()
                .extract()
                .response();
        return singleBookingResponse;

    }


}
