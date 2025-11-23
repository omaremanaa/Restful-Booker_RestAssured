package Requests;

import Helpers.JsonReader;
import Helpers.Payload;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class AuthenticationRequests {
    JsonReader jsonReader = new JsonReader("api-data");

    private String pathPar = jsonReader.getJsonData("authPath");

    public Response createToken(String username, String password) {
        RestAssured.baseURI = jsonReader.getJsonData("baseURI");
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
