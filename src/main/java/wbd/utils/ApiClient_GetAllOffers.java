package wbd.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static wbd.core.ApplicationManager.BASE_URL;

public class ApiClient_GetAllOffers {

    public static Response getAllOffers() {
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/")
                .then()
                .extract()
                .response();
    }
}

