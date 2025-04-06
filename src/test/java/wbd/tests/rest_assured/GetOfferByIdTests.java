package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.get.ApiClient_GetOfferById;
import wbd.api.client.get.ApiClient_GetOffers;
import wbd.api.dto.OfferDto;
import wbd.api.dto.OfferResponseDto;
import wbd.core.TestBaseRA;

import static io.restassured.RestAssured.given;

// позитивный тест проверяет, что API корректно возвращает список всех офферов.
// негативные тесты предназначены для проверки ошибок, связанных с неверными или отсутствующими данными, которые могут быть получены от сервера при запросах по ID оффера.

public class GetOfferByIdTests extends TestBaseRA {

    // тест проверяет, что API корректно возвращает оффер по указанному ID (в данном случае, ID = 1). Мы проверяем, что оффер существует, и проверяем его поля, такие как title, description, pricePerHour, и категорию

    @Test
    public void testGetOfferById() {
        logger.info("Start testing GetOfferById");
        logger.info("=============================================");

        int offerId = 1;  // допустим, оффер с ID 1 существует

        Response response = ApiClient_GetOfferById.getOfferById(offerId); // получаем оффер по ID
        logger.info("Response body: " + response.asString());

        SoftAssert softAssert = new SoftAssert();

        // проверка статуса 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        if (response.getStatusCode() == 200) {
            logger.info("The test was successful: Response code 200 (OK)");
        } else {
            logger.error("Error: Received response with code " + response.getStatusCode());
        }

        // парсим JSON в объект OfferDto
        OfferDto offer = response.as(OfferDto.class);

        softAssert.assertNotNull(offer, "Offer must not be null");

        // все необходимые поля присутствуют
        softAssert.assertNotNull(offer.getTitle(), "Title must not be null");
        softAssert.assertTrue(!offer.getTitle().isEmpty(), "Title should not be empty");

        softAssert.assertNotNull(offer.getDescription(), "Description must not be null");
        softAssert.assertTrue(!offer.getDescription().isEmpty(), "Description should not be empty");

        softAssert.assertTrue(offer.getPricePerHour() > 0, "The price has to be positive");

        // проверка categoryDto как объекта
        softAssert.assertNotNull(offer.getCategoryDto(), "Category must not be null");
        softAssert.assertNotNull(offer.getCategoryDto().getName(), "Category Name must not be null");
        softAssert.assertTrue(!offer.getCategoryDto().getName().isEmpty(), "Category name must not be empty");

        // проверка UserFilterResponseDto (если оно есть)
        if (offer.getUserFilterResponseDto() != null) {
            softAssert.assertNotNull(offer.getUserFilterResponseDto().getFirstName(), "FirstName must not be null");
            softAssert.assertTrue(!offer.getUserFilterResponseDto().getFirstName().isEmpty(), "FirstName must not be empty");

            softAssert.assertNotNull(offer.getUserFilterResponseDto().getLastName(), "LastName must not be null");
            softAssert.assertTrue(!offer.getUserFilterResponseDto().getLastName().isEmpty(), "LastName must not be empty");

            softAssert.assertNotNull(offer.getUserFilterResponseDto().getProfilePicture(), "ProfilePicture must not be null");
            softAssert.assertTrue(!offer.getUserFilterResponseDto().getProfilePicture().isEmpty(), "ProfilePicture must not be empty");

            softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationDto(), "LocationResponseDto must not be null");

            if (offer.getUserFilterResponseDto().getLocationDto() != null) {
                softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationDto().getCityName(), "CityName must not be null");
                softAssert.assertTrue(!offer.getUserFilterResponseDto().getLocationDto().getCityName().isEmpty(), "CityName must not be empty");
            }
        }

        softAssert.assertAll();
    }

    // ================= негативные тесты ====================

    @Test
    public void testGetOfferById_InvalidId() {
        int invalidId = -1; // Некорректный ID
        Response response = ApiClient_GetOfferById.getOfferById(invalidId);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotEquals(response.getStatusCode(), 200, "Ожидается ошибка для некорректного ID");

        softAssert.assertAll();
    }

    @Test
    public void testGetOfferByNonExistentId_404() {
        int nonExistentOfferId = 9999;  // Предположим, что такого оффера нет
        Response response = ApiClient_GetOfferById.getOfferById(nonExistentOfferId);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), 404, "Ожидается статус 404 для несуществующего оффера");

        softAssert.assertAll();
    }

    @Test
    public void testGetOffersByInvalidId_400() {
        // Запрос с неверным форматом ID
        Response response = given()
                .when()
                .get("/offers/invalid-id")
                .then()
                .statusCode(400)  // Ожидаем ошибку 400
                .log().all()
                .extract().response();

        if (response.getStatusCode() == 400) {
            logger.info("Тест прошел успешно: Получен ответ с кодом 400 (Bad Request)");
        }
    }

}
