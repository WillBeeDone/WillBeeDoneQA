package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.get.ApiClient_GetAllOffers;
import wbd.api.dto.AllOffersResponseDto;
import wbd.core.TestBaseRA;

import java.util.List;

import static io.restassured.RestAssured.given;

public class GetAllOffersTests extends TestBaseRA {

    @Test
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
            softAssert.assertNotNull(offer.getTitle(), "title must not be null");
            softAssert.assertFalse(offer.getTitle().isEmpty(), "title must not be empty");

            // проверяем категорию
            softAssert.assertNotNull(offer.getCategoryDto(), "category must not be null");
            softAssert.assertNotNull(offer.getCategoryDto().getName(), "category name must not be null");
            softAssert.assertFalse(offer.getCategoryDto().getName().isEmpty(), "category name must not be empty");

            // проверяем pricePerHour
            softAssert.assertTrue(offer.getPricePerHour() > 0, "price per hour must be positive");

            // проверяем description
            softAssert.assertNotNull(offer.getDescription(), "description must not be null");
            softAssert.assertFalse(offer.getDescription().isEmpty(), "description must not be empty");

            // проверяем пользователя
            softAssert.assertNotNull(offer.getUserFilterResponseDto(), "user must not be null");

            if (offer.getUserFilterResponseDto() != null) {
                // проверяем firstName
                softAssert.assertNotNull(offer.getUserFilterResponseDto().getFirstName(), "firstName must not be null");
                softAssert.assertFalse(offer.getUserFilterResponseDto().getFirstName().isEmpty(), "firstName must not be empty");

                // проверяем lastName
                softAssert.assertNotNull(offer.getUserFilterResponseDto().getLastName(), "lastName must not be null");
                softAssert.assertFalse(offer.getUserFilterResponseDto().getLastName().isEmpty(), "lastName must not be empty");

                // проверяем profilePicture
                softAssert.assertNotNull(offer.getUserFilterResponseDto().getProfilePicture(), "profilePicture must not be null");
                softAssert.assertFalse(offer.getUserFilterResponseDto().getProfilePicture().isEmpty(), "profilePicture must not be empty");

                // проверяем locationDto
                softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationDto(), "location must not be null");

                if (offer.getUserFilterResponseDto().getLocationDto() != null) {
                    // проверяем cityName
                    softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationDto().getCityName(), "cityName must not be null");
                    softAssert.assertFalse(offer.getUserFilterResponseDto().getLocationDto().getCityName().isEmpty(), "cityName must not be empty");
                }
            }

            logger.info("---------------------------------------------------------");
            for (AllOffersResponseDto offerDto : offers) {
                logger.info("offer => " + offerDto.getTitle() + ";");
            }
        }

        // выполняем все SoftAssert проверки
        softAssert.assertAll();
    }

    // баг-репорт, сервер возвращает 200
    @Test
    public void testGetAllOffersWithInvalidQueryParam_Location() {
        // отправляем GET-запрос с некорректным значением для cityName (например, числовая строка)
        Response response = given()
                .queryParam("cityName", "123")
                .when()
                .get("/offers/all")
                .then()
                .log().all()
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

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

    // баг репорт, сервер возвращает 200
    @Test
    public void testGetAllOffersWithInvalidQueryParam_Category() {
        // Отправляем запрос через API-клиент с некорректным значением категории
        Response response = given()
                .queryParam("category", "nonexistent-category")  // Некорректная категория
                .when()
                .get("/offers/all")
                .then()
                .log().all()  // Логируем все данные для отладки
                .extract()
                .response();

        SoftAssert softAssert = new SoftAssert();

        // Проверяем, что возвращается ошибка 400 Bad Request при передаче некорректной категории
        softAssert.assertEquals(response.getStatusCode(), 400,
                "Expected status code 400 for invalid category in GET request to /offers/all");

        // Проверяем, что ошибка содержит упоминание категории, если она есть в сообщении
        if (response.getStatusCode() == 400 && response.getContentType().contains("application/json")) {
            String errorMessage = response.jsonPath().getString("error");
            softAssert.assertTrue(errorMessage != null && errorMessage.toLowerCase().contains("category"),
                    "Error message should mention 'category'");
            logger.info("Received error message: " + errorMessage);
        }

        // Выполняем все проверки
        softAssert.assertAll();
    }

    @Test
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

        SoftAssert softAssert = new SoftAssert();

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