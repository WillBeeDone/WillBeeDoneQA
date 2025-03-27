package wbd.api.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
public class CategoriesClient {
    public static Response getCategories() {
        return RestAssured
                .given()
                .when()
                .get("/categories")
                .then()
                .extract()
                .response();
    }
}
