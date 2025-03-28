package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.core.TestBaseRA;
import wbd.api.dto.FilteredOffersResponseDto;
import wbd.api.client.ApiClient_GetFilterOffers;

import java.util.List;

public class GetFilteredOffersTests extends TestBaseRA {

    @Test
    public void testGetFilteredOffers() {

        logger.info("Start testing GetFilteredOffers");
        logger.info("=============================================");

        Response response = ApiClient_GetFilterOffers.getFilteredOffers(); // метод из ApiClient

        // логируем ответ
        logger.info("Response body: " + response.asString());

        // парсим JSON в список
        List<FilteredOffersResponseDto> filteredOffers = response.jsonPath().getList("", FilteredOffersResponseDto.class);

        SoftAssert softAssert = new SoftAssert();

        // проверяем статус 200
        response.then().statusCode(200);

        if (response.getStatusCode() == 200) {
            logger.info("Тест прошел успешно: Код ответа 200 (OK)");
        } else {
            logger.error("Ошибка: Получен ответ с кодом " + response.getStatusCode());
        }

        // Проверка на пустой список офферов
        if (filteredOffers.isEmpty()) {
            logger.info("⚠️ Список офферов пуст!");
            softAssert.assertTrue(filteredOffers.isEmpty(), "Список офферов должен быть пустым.");
        } else {
            // если список не пустой, выполняем проверки
            logger.info("✅ Найдено " + filteredOffers.size() + " офферов.");
            softAssert.assertNotNull(filteredOffers, "Список офферов не должен быть null");
            softAssert.assertFalse(filteredOffers.isEmpty(), "Список офферов не должен быть пустым");

            // проверка полей 1-го оффера
            softAssert.assertNotNull(filteredOffers.get(0).getTitle(), "Title не должен быть null");
            softAssert.assertTrue(!filteredOffers.get(0).getTitle().isEmpty(), "Title не должен быть пустым");

            softAssert.assertNotNull(filteredOffers.get(0).getDescription(), "Description не должен быть null");
            softAssert.assertTrue(!filteredOffers.get(0).getDescription().isEmpty(), "Description не должен быть пустым");

            softAssert.assertTrue(filteredOffers.get(0).getPricePerHour() > 0, "Цена должна быть положительной");

            // Проверка categoryResponseDto как объекта
            softAssert.assertNotNull(filteredOffers.get(0).getCategoryDto(), "Category не должен быть null");
            softAssert.assertNotNull(filteredOffers.get(0).getCategoryDto().getName(), "'name' категории не должен быть null");
            softAssert.assertTrue(!filteredOffers.get(0).getCategoryDto().getName().isEmpty(), "Category name не должен быть пустым");

            // если OfferDto содержит UserFilterResponseDto
            softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto(), "UserFilterResponseDto не должен быть null");

            if (filteredOffers.get(0).getUserFilterResponseDto() != null) {
                // проверка полей UserFilterResponseDto
                softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto().getFirstName(), "FirstName не должен быть null");
                softAssert.assertTrue(!filteredOffers.get(0).getUserFilterResponseDto().getFirstName().isEmpty(), "FirstName не должен быть пустым");

                softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto().getLastName(), "LastName не должен быть null");
                softAssert.assertTrue(!filteredOffers.get(0).getUserFilterResponseDto().getLastName().isEmpty(), "LastName не должен быть пустым");

                softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto().getProfilePicture(), "ProfilePicture не должен быть null");
                softAssert.assertTrue(!filteredOffers.get(0).getUserFilterResponseDto().getProfilePicture().isEmpty(), "ProfilePicture не должен быть пустым");

                // проверка, что cityName внутри locationResponseDto не null и не пустое
                if (filteredOffers.get(0).getUserFilterResponseDto().getLocationDto() != null) {
                    softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto().getLocationDto().getCityName(), "CityName не должен быть null");
                    softAssert.assertTrue(!filteredOffers.get(0).getUserFilterResponseDto().getLocationDto().getCityName().isEmpty(), "CityName не должен быть пустым");
                }

            }
        }
        // запуск
        softAssert.assertAll();
    }
}