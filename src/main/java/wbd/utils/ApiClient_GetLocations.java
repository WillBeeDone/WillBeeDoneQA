package wbd.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static wbd.core.ApplicationManager.BASE_URL;

public class ApiClient_GetLocations {

    public static Response getLocations() {
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/locations")
                .then()
                .extract()
                .response();
    }
}
