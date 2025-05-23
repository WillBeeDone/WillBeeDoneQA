package wbd.api.client.get_post;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiClient_GetCategories {

    public static Response getCategories() {
        return RestAssured
                .given()
                .when()
                .get("/categories")
                .then()
                .log().all()
                .extract()
                .response();
    }
}