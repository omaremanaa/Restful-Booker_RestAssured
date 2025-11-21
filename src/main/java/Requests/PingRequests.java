package Requests;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class PingRequests {
    private String pathPar = "/ping";

    public Response healthCheck() {
        Response response = given().header("Content-Type", "application/json")
                .when()
                .get(pathPar)
                .then()
                .extract()
                .response();
        return response;
    }
}
