package wbd.api.сlient.get;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiClient_GetAllOffers {

    public static Response getAllOffers() {
        return RestAssured
                .given()
                .when()
                .get("/")
                .then()
                .log().all()
                .extract()
                .response();
    }
}

