package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.core.TestBaseRA;

import static io.restassured.RestAssured.given;

public class ApiSecurityTests extends TestBaseRA {

    // тест для неавторизованного пользователя
    @Test
    public void testGetOfferWithoutToken() {

        logger.info("Start testing APISecurity");
        logger.info("=============================================");

        Response response = given()
                .when()
                .get("api/offers/1")  // запрос без авторизации
                .then()
                .statusCode(200)  // ожидаем успешный статус 200
                .log().all()
                .extract().response();

        // проверяем, что ответ не содержит данных пользователя (имя, фото, местоположение)
        String responseBody = response.getBody().asString();
        SoftAssert softAssert = new SoftAssert();

        // Ожидаем, что в ответе будет информации о пользователе
        softAssert.assertTrue(responseBody.contains("firstName"), "Имя исполнителя должно быть видно неавторизованному пользователю");
        softAssert.assertTrue(responseBody.contains("lastName"), "Фамилия исполнителя  должна быть видна неавторизованному пользователю");
        softAssert.assertTrue(responseBody.contains("profilePicture"), "Фото исполнителя  должно быть видно неавторизованному пользователю");
        softAssert.assertTrue(responseBody.contains("locationDto"), "Местоположение исполнителя должно быть видно неавторизованному пользователю");
        softAssert.assertTrue(responseBody.contains("title"), "Название предложения должно быть видно");
        softAssert.assertTrue(responseBody.contains("categoryDto"), "Категория предложения должна быть видна");
        softAssert.assertTrue(responseBody.contains("pricePerHour"), "Цена за час должна быть видна");
        softAssert.assertTrue(responseBody.contains("description"), "Описание предложения должно быть видно");

        softAssert.assertAll();
    }

}
