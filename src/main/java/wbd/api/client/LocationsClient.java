package wbd.api.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class LocationsClient {

    public static Response getLocations() {
        return RestAssured
                .given()
                .when()
                .get("/locations")
                .then()
                .extract()
                .response();
    }
}