package wbd.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static wbd.core.ApplicationManager.BASE_URL;

public class ApiClient_GetOfferById {

  //  private static final String BASE_URL = "http://localhost:8080/api";

    public static Response getOfferById(int offerId) {
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/offers/" + offerId)
                .then()
                .extract()
                .response();
    }
}
