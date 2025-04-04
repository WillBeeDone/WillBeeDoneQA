package wbd.api.client.get;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiClient_GetAllOffers {

    public static Response getAllOffers() {
        return RestAssured
                .given()
                .when()
                .get("/offers/all")
                .then()
                .log().all()
                .extract()
                .response();
    }
}