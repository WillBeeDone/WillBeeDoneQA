package wbd.tests.rest_assured;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import wbd.api.client.get_post.ApiClient_GetAllOffers;
import wbd.api.dto.AllOffersResponseDto;
import wbd.core.TestBaseRA;

import java.util.List;

import static io.restassured.RestAssured.given;

// проверяет дефолтный /offers без параметров, структуру и базовую логику DTO.
@Epic("Offers")
@Feature("Get All Offers")
public class GetAllOffersTests extends TestBaseRA {

    @Test(groups = "Positive")
    @Severity(SeverityLevel.NORMAL)
    @Story("Retrieve all offers without query parameters")
    @Description("Verify that API returns a non-empty list of offers with valid structure when no filters are applied")
    @TmsLink("")
    public void testGetAllOffers() {
        logger.info("start testing GetAllOffers");
        logger.info("=============================================");

        // отправляем GET-запрос на получение всех офферов
        Response response = ApiClient_GetAllOffers.getAllOffers();
        logger.info("response body: " + response.asString());

        // проверяем статус-код 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "expected status code 200");

        if (response.getStatusCode() == 200) {
            logger.info("✅ test successful: response code 200 (OK)");
        } else {
            logger.error("❌ error: received response with code " + response.getStatusCode());
        }

        // парсим JSON в список объектов AllOffersResponseDto
        List<AllOffersResponseDto> offers = response.jsonPath().getList("", AllOffersResponseDto.class);

        // проверяем, что список офферов не пустой
        softAssert.assertNotNull(offers, "offers list must not be null");
        softAssert.assertFalse(offers.isEmpty(), "offers list must not be empty");

        // если есть хотя бы один оффер — проверяем его данные
        if (!offers.isEmpty()) {
            AllOffersResponseDto offer = offers.get(0);

            // проверяем id
            softAssert.assertTrue(offer.getId() > 0, "id must be greater than 0");

            // проверяем title
            assertNotNullAndNotEmpty(offer.getTitle(), "title");

            // проверяем категорию
            softAssert.assertNotNull(offer.getCategoryDto(), "category must not be null");
            if (offer.getCategoryDto() != null) {
                assertNotNullAndNotEmpty(offer.getCategoryDto().getName(), "category name");
            }

            // проверяем pricePerHour
            softAssert.assertTrue(offer.getPricePerHour() > 0, "price per hour must be positive");

            // проверяем description
            assertNotNullAndNotEmpty(offer.getDescription(), "description");

            // проверяем пользователя
            softAssert.assertNotNull(offer.getUserFilterResponseDto(), "user must not be null");

            if (offer.getUserFilterResponseDto() != null) {
                // проверяем firstName
                assertNotNullAndNotEmpty(offer.getUserFilterResponseDto().getFirstName(), "firstName");

                // проверяем lastName
                assertNotNullAndNotEmpty(offer.getUserFilterResponseDto().getLastName(), "lastName");

                // проверяем profilePicture
                assertNotNullAndNotEmpty(offer.getUserFilterResponseDto().getProfilePicture(), "profilePicture");

                // проверяем locationDto
                softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationDto(), "location must not be null");

                if (offer.getUserFilterResponseDto().getLocationDto() != null) {
                    // проверяем cityName
                    assertNotNullAndNotEmpty(offer.getUserFilterResponseDto().getLocationDto().getCityName(), "cityName");
                }
            }

            logger.info("---------------------------------------------------------");
            for (AllOffersResponseDto offerDto : offers) {
                logger.info("offer => " + offerDto.getTitle() + ";");
            }
        }
        softAssert.assertAll();
    }

    //=========================== негативные тесты ===============================

    // баг-репорт QA-BugReport-5, сервер возвращает 200
    @Test(groups = "Negative")
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid cityName query parameter")
    @Description("Verify that sending a numeric value for cityName returns 400 Bad Request")
    @TmsLink("QA-BugReport-5")
    public void testGetAllOffersWithInvalidQueryParam_Location() {
        // отправляем GET-запрос с некорректным значением для cityName (например, числовая строка)
        Response response = given()
                .queryParam("cityName", 123)
                .when()
                .get("/offers/all")
                .then()
                .log().all()
                .extract()
                .response();

        // ожидается, что API вернет статус 400 Bad Request, так как передан некорректный параметр cityName
        softAssert.assertEquals(response.getStatusCode(), 400,
                "Expected 400 for invalid cityName in GET /offers/all");

        // проверка наличия сообщения об ошибке, если оно есть
        String errorMessage = response.jsonPath().getString("error");
        if (errorMessage != null) {
            softAssert.assertTrue(errorMessage.toLowerCase().contains("cityname"),
                    "Error message should mention 'cityName'");
        } else {
            logger.warn("⚠ Error message is null — server may not provide error details");
        }

        logger.info("Received status code " + response.getStatusCode() +
                " for GET /offers/all with invalid cityName");

        softAssert.assertAll();
    }

    // баг репорт QA-BugReport-3, сервер возвращает 200
    @Test(groups = "Negative")
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid category query parameter")
    @Description("Verify that using an unknown category returns 400 Bad Request")
    @TmsLink("QA-BugReport-3")
    public void testGetAllOffersWithInvalidQueryParam_Category() {
        // отправляем запрос через API-клиент с некорректным значением категории
        Response response = given()
                .queryParam("category", "nonexistent-category")  // Некорректная категория
                .when()
                .get("/offers/all")
                .then()
                .log().all()  // Логируем все данные для отладки
                .extract()
                .response();

        // проверяем, что возвращается ошибка 400 Bad Request при передаче некорректной категории
        softAssert.assertEquals(response.getStatusCode(), 400,
                "Expected status code 400 for invalid category in GET request to /offers/all");

        // проверяем, что ошибка содержит упоминание категории, если она есть в сообщении
        if (response.getStatusCode() == 400 && response.getContentType().contains("application/json")) {
            String errorMessage = response.jsonPath().getString("error");
            softAssert.assertTrue(errorMessage != null && errorMessage.toLowerCase().contains("category"),
                    "Error message should mention 'category'");
            logger.info("Received error message: " + errorMessage);
        }
        softAssert.assertAll();
    }

    // баг репорт QA-BugReport-2, сервер возвращает 200
    @Test(groups = "Negative")
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid pricePerHour query parameter")
    @Description("Verify that passing a non-numeric value for pricePerHour returns 400 Bad Request")
    @TmsLink("QA-BugReport-2")
    public void testGetAllOffersWithInvalidQueryParam_PricePerHour() {
        // отправляем запрос с некорректным значением для pricePerHour (например, строка вместо числа)
        Response response = given()
                .queryParam("pricePerHour", "invalid-price")  // Некорректное значение для pricePerHour
                .when()
                .get("/offers/all")  // Новый эндпоинт для получения офферов
                .then()
                .log().all()  // Логируем полный ответ для отладки
                .extract()
                .response();

        // Ожидаем, что API вернет статус 400 Bad Request
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected status code 400 for invalid pricePerHour");

        // Проверяем сообщение об ошибке, чтобы оно содержало информацию о некорректном параметре pricePerHour
        if (response.getStatusCode() == 400) {
            String errorMessage = response.jsonPath().getString("error");
            softAssert.assertTrue(errorMessage != null && errorMessage.toLowerCase().contains("priceperhour"),
                    "Error message should mention 'pricePerHour'");
        }

        softAssert.assertAll();
    }

}