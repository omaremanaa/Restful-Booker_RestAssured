package resources;

import Helpers.JsonReader;
import io.restassured.RestAssured;


public class Utils {
    JsonReader apiJsonReader = new JsonReader("api-data");
    private String baseURI = apiJsonReader.getJsonData("baseURI");
    public static String username;
    public static String password;
    public static int bookingID;
    public static String token;

    public Utils() {
        RestAssured.baseURI = baseURI;
    }

}
