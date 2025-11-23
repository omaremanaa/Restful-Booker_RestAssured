package Helpers;

public class Payload {

    public String authorizePayload(String username, String password) {
        return "{\n" +
                "    \"username\" :" + "\"" + username + "\",\n" +
                "    \"password\" :" + "\"" + password + "\"\n" +
                "}";
    }

    public String createBookingPayload(String firstname, String lastname, int totalprice,
                                       boolean depositpaid, String checkin,
                                       String checkout, String additionalneeds) {
        return "{\n" +
                "    \"firstname\" : \"" + firstname + "\",\n" +
                "    \"lastname\" : \"" + lastname + "\",\n" +
                "    \"totalprice\" : " + totalprice + ",\n" +
                "    \"depositpaid\" : " + depositpaid + ",\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"" + checkin + "\",\n" +
                "        \"checkout\" : \"" + checkout + "\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"" + additionalneeds + "\"\n" +
                "}";
    }

    public String partialUpdateBookingPayload(String firstname, String lastname) {
        return "{\n" +
                "    \"firstname\" : \"" + firstname + "\",\n" +
                "    \"lastname\" : \"" + lastname + "\"\n" +
                "}";
    }
}
