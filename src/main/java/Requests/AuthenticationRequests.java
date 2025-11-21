package Requests;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class AuthenticationRequests {
    private String pathPar = "/auth";

    public Response createToken(String username, String password) {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        Payload payload = new Payload();
        Response response;

        response = given()
                .header("Content-Type", "application/json")
                .body(payload.authorizePayload(username, password))
                .when()
                .post(pathPar)
                .then()
                .extract()
                .response();
        return response;

    }
}
