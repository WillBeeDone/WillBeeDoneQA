package wbd.api.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import wbd.api.dto.AuthRequestDto;
import wbd.api.dto.RefreshTokenRequestDto;

import static io.restassured.RestAssured.given;

public class AuthClient {

    public static Response register(AuthRequestDto body) {
        return given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/register")
                .then()
                .extract().response();
    }

    public static Response login(AuthRequestDto body) {
        System.out.println("Request to: " + RestAssured.baseURI + RestAssured.basePath + "/auth/login");
        return given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/auth/login")
                .then()
                .extract().response();
    }
    public static Response refreshToken(RefreshTokenRequestDto request) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/refresh")
                .then()
                .extract()
                .response();
    }
}