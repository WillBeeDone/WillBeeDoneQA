package wbd.api.client.get_post;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiClient_GetOfferById {

    public static Response getOfferById(int offerId) {
        return RestAssured
                .given()
                .when()
                .get("/offers/" + offerId)
                .then()
                .log().all()
                .extract()
                .response();
    }
}
