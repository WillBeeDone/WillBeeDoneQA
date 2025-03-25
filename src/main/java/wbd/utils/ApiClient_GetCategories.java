package wbd.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static wbd.core.ApplicationManager.BASE_URL;

public class ApiClient_GetCategories {

   // private static final String BASE_URL = "http://localhost:8080/api";

    public static Response getCategories() {
        return RestAssured
                .given()
                .baseUri(BASE_URL)
                .when()
                .get("/categories")
                .then()
                .extract()
                .response();
    }
}

