package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.get_post.ApiClient_GetOfferById;
import wbd.api.dto.OfferByIdDto;
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

        int offerId = 4;  // допустим, оффер с ID 4 существует

        Response response = ApiClient_GetOfferById.getOfferById(offerId); // получаем оффер по ID
        logger.info("Response body: " + response.asString());

        // проверка статуса 200 OK
        int statusCode = response.getStatusCode();
        softAssert.assertEquals(statusCode, 200, "Expected status code 200");

        if (statusCode != 200) {
            logger.error("❌ Error: Received response with code " + statusCode);
            String errorMessage = response.jsonPath().getString("message");
            logger.info("Error message from response: " + errorMessage);
            return; // прерываем тест, смысла парсить нет
        }

        // парсим JSON в объект OfferByIdDto
        OfferByIdDto offer = response.as(OfferByIdDto.class);

        softAssert.assertNotNull(offer, "Offer must not be null");

        // используем метод для проверки title, description, pricePerHour и других строковых значений
        assertNotNullAndNotEmpty(offer.getTitle(), "Title");
        assertNotNullAndNotEmpty(offer.getDescription(), "Description");

        // проверка pricePerHour (должен быть больше 0)
        softAssert.assertTrue(offer.getPricePerHour() > 0, "The price has to be positive");

        // проверка categoryDto как объекта
        softAssert.assertNotNull(offer.getCategoryDto(), "Category must not be null");
        softAssert.assertNotNull(offer.getCategoryDto().getName(), "Category Name must not be null");
        softAssert.assertTrue(!offer.getCategoryDto().getName().isEmpty(), "Category name must not be empty");

        // проверка UserProfileResponseDto
        if (offer.getUserProfileResponseDto() != null) {
            assertNotNullAndNotEmpty(offer.getUserProfileResponseDto().getFirstName(), "FirstName");
            assertNotNullAndNotEmpty(offer.getUserProfileResponseDto().getLastName(), "LastName");
            assertNotNullAndNotEmpty(offer.getUserProfileResponseDto().getProfilePicture(), "ProfilePicture");
            assertNotNullAndNotEmpty(offer.getUserProfileResponseDto().getEmail(), "Email");
            assertNotNullAndNotEmpty(offer.getUserProfileResponseDto().getPhoneNumber(), "PhoneNumber");
            softAssert.assertNotNull(offer.getUserProfileResponseDto().getLocationDto(), "LocationResponseDto must not be null");

            if (offer.getUserProfileResponseDto().getLocationDto() != null) {
                assertNotNullAndNotEmpty(offer.getUserProfileResponseDto().getLocationDto().getCityName(), "CityName");
            }
        }

        // проверка изображений
        if (offer.getImages() != null && !offer.getImages().isEmpty()) {
            softAssert.assertNotNull(offer.getImages().get(0).getImageUrl(), "Image URL must not be null");
            softAssert.assertTrue(!offer.getImages().get(0).getImageUrl().isEmpty(), "Image URL should not be empty");
        }
        softAssert.assertAll();
    }

    // ================= негативные тесты ====================

    @Test
    public void testGetOfferById_InvalidId() {
        int invalidId = -1; // Некорректный ID
        Response response = ApiClient_GetOfferById.getOfferById(invalidId);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotEquals(response.getStatusCode(), 200, "Expected error for invalid ID");

        softAssert.assertAll();
    }

    @Test
    public void testGetOfferByNonExistentId_404() {
        int nonExistentOfferId = 9999;  // Предположим, что такого оффера нет
        Response response = ApiClient_GetOfferById.getOfferById(nonExistentOfferId);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), 404, "Expected 404 status for non-existent offer");

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
            logger.info("Test passed: Received 400 (Bad Request) status code");
        }
    }

}
