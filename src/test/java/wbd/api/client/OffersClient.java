package wbd.api.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class OffersClient {
    public static Response getAllOffers() {
        return RestAssured
                .given()
                .when()
                .get("/")
                .then()
                .extract()
                .response();
        }

    public static Response getFilteredOffers() {
        return RestAssured
                .given()
                .when()
                .get("/offers/filter?cityName=all&category=all&keyPhrase=all")
                .then()
                .extract()
                .response();
    }

    public static Response getOfferById(int offerId) {
        return RestAssured
                .given()
                .when()
                .get("/offers/" + offerId)
                .then()
                .extract()
                .response();
    }
}
