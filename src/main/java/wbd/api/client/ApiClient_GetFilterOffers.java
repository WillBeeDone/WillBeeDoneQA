package wbd.api.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiClient_GetFilterOffers {

    public static Response getFilteredOffers() {
        return RestAssured
                .given()
                .when()
                .get("/offers/filter?cityName=all&category=all&keyPhrase=all")
                .then()
                .log().all()
                .extract()
                .response();
    }
}
