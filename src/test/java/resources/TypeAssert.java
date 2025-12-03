package resources;

import io.restassured.path.json.JsonPath;

public class TypeAssert {

    public static void assertString(JsonPath path, String field, AllureSoftAssert softAssert) {
        Object value = path.get(field);
        softAssert.assertNotNull(value, field + " should not be null");
        softAssert.assertTrue(value instanceof String, field + " should be a String but was: " + typeOf(value));
    }

    public static void assertInteger(JsonPath path, String field, AllureSoftAssert softAssert) {
        Object value = path.get(field);
        softAssert.assertNotNull(value, field + " should not be null");
        softAssert.assertTrue(value instanceof Integer, field + " should be an Integer but was: " + typeOf(value));
    }

    public static void assertBoolean(JsonPath path, String field, AllureSoftAssert softAssert) {
        Object value = path.get(field);
        softAssert.assertNotNull(value, field + " should not be null");
        softAssert.assertTrue(value instanceof Boolean, field + " should be a Boolean but was: " + typeOf(value));
    }

    private static String typeOf(Object value) {
        return (value == null) ? "null" : value.getClass().getSimpleName();
    }
}
