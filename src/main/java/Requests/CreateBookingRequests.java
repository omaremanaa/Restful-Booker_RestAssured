package Requests;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CreateBookingRequests {

    private String pathPar = "/booking" ;

    public Response createBooking(String firstname, String lastname,
                                  int totalprice, boolean depositpaid,
                                  String checkin, String checkout, String additionalneeds){

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

    public Response createBookings(String body){
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