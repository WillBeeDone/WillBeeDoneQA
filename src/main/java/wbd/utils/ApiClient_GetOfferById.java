package wbd.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiClient_GetOfferById {

    private static final String BASE_URL = "http://localhost:8080/api";

    public static Response getOfferById(int offerId) {
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/offers/" + offerId) // подставляем ID
                .then()
                .extract()
                .response();
    }
}