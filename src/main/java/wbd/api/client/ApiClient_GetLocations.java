package wbd.api.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiClient_GetLocations {

    public static Response getLocations() {
        return RestAssured
                .given()
                .when()
                .get("/locations")
                .then()
                .log().all()
                .extract()
                .response();
    }
}
