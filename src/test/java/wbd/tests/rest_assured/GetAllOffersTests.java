package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.get.ApiClient_GetAllOffers;
import wbd.core.TestBaseRA;
import wbd.api.dto.AllOffersResponseDto;

import java.util.List;

public class GetAllOffersTests extends TestBaseRA {

    @Test
    public void testGetAllOffers() {
        logger.info("Start testing GetAllOffers");
        logger.info("=============================================");

        // Отправляем GET-запрос на получение всех офферов
        Response response = ApiClient_GetAllOffers.getAllOffers();
        logger.info("Response body: " + response.asString());

        SoftAssert softAssert = new SoftAssert();

        // Проверяем статус-код 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        if (response.getStatusCode() == 200) {
            logger.info("✅ Тест успешен: Код ответа 200 (OK)");
        } else {
            logger.error("❌ Ошибка: Получен ответ с кодом " + response.getStatusCode());
        }

        // Парсим JSON в список объектов AllOffersResponseDto
        List<AllOffersResponseDto> offers = response.jsonPath().getList("", AllOffersResponseDto.class);

        // Проверяем, что список офферов не пустой
        softAssert.assertNotNull(offers, "Список офферов не должен быть null");
        softAssert.assertFalse(offers.isEmpty(), "Список офферов не должен быть пустым");

        // Если есть хотя бы один оффер — проверяем его данные
        if (!offers.isEmpty()) {
            AllOffersResponseDto offer = offers.get(0);

            softAssert.assertNotNull(offer.getTitle(), "Title не должен быть null");
            softAssert.assertFalse(offer.getTitle().isEmpty(), "Title не должен быть пустым");

            softAssert.assertNotNull(offer.getDescription(), "Description не должен быть null");
            softAssert.assertFalse(offer.getDescription().isEmpty(), "Description не должен быть пустым");

            softAssert.assertTrue(offer.getPricePerHour() > 0, "Цена должна быть положительной");

            // Проверяем категорию
            softAssert.assertNotNull(offer.getCategoryDto(), "Category не должен быть null");
            softAssert.assertNotNull(offer.getCategoryDto().getName(), "Category Name не должен быть null");

            // Проверяем пользователя
            softAssert.assertNotNull(offer.getUserFilterResponseDto(), "User не должен быть null");

            if (offer.getUserFilterResponseDto() != null) {
                softAssert.assertNotNull(offer.getUserFilterResponseDto().getFirstName(), "FirstName не должен быть null");
                softAssert.assertFalse(offer.getUserFilterResponseDto().getFirstName().isEmpty(), "FirstName не должен быть пустым");

                softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationDto(), "LocationResponseDto не должен быть null");

                if (offer.getUserFilterResponseDto().getLocationDto() != null) {
                    softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationDto().getCityName(), "CityName не должен быть null");
                    softAssert.assertFalse(offer.getUserFilterResponseDto().getLocationDto().getCityName().isEmpty(), "CityName не должен быть пустым");
                }
            }

            // Проверяем изображения (если они есть)
            List<String> images = offer.getImages(); // Список изображений (URL)

            if (images != null && !images.isEmpty()) {
                for (String imageUrl : images) {
                    softAssert.assertNotNull(imageUrl, "Image URL не должен быть null");
                    softAssert.assertFalse(imageUrl.isEmpty(), "Image URL не должен быть пустым");
                }
            } else {
                logger.warn("⚠️ Оффер не содержит изображений!");
            }

            logger.info("---------------------------------------------------------");
            for (AllOffersResponseDto offerDto : offers) {
                logger.info("Оффер => " + offerDto.getTitle() + ";");
            }
        }

        // Выполняем все SoftAssert проверки
        softAssert.assertAll();
    }
}
