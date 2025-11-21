package resources;

import io.restassured.RestAssured;


public class Utils {
    private  String baseURI = "https://restful-booker.herokuapp.com";

    public Utils(){
        RestAssured.baseURI = baseURI;
    }

}
