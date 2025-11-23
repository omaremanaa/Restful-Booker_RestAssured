package Requests;

import Helpers.JsonReader;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class PingRequests {
    JsonReader jsonReader = new JsonReader("api-data");
    private String pathPar = jsonReader.getJsonData("pingPath");

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
