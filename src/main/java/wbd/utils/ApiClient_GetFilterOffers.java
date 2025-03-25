package wbd.utils;


import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiClient_GetFilterOffers {

    private static final String BASE_URL = "http://localhost:8080/api";
    public static Response getFilteredOffers() {
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/offers/filter?cityName=all&category=all&keyPhrase=all")
                .then()
                .extract()
                .response();
    }
}
