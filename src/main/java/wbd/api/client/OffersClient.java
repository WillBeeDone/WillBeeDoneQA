package wbd.api.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import wbd.api.dto.AddOfferRequestDto;

public class OffersClient {

    public static Response createOffer(AddOfferRequestDto request, String accessToken) {
        return RestAssured
                .given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/users/offers")
                .then()
                .extract()
                .response();
    }

}
