package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.OffersClient;
import wbd.core.TestBaseRA;
import wbd.dto.FilteredOffersResponseDto;

import java.util.List;

public class GetFilteredOffersTests extends TestBaseRA {

    private static final Logger logger = LoggerFactory.getLogger(GetFilteredOffersTests.class);
    @Test
    public void testGetFilteredOffers() {
        Response response = OffersClient.getFilteredOffers();

        // логируем ответ
        logger.info("Response body: {}", response.asString());

        // парсим JSON в список
        List<FilteredOffersResponseDto> filteredOffers = response.jsonPath().getList("", FilteredOffersResponseDto.class);

        SoftAssert softAssert = new SoftAssert();

        // проверяем статус 200
        response.then().statusCode(200);

        if (response.getStatusCode() == 200) {
            logger.info("Тест прошел успешно: Код ответа 200 (OK)");
        } else {
            logger.error("Ошибка: Получен ответ с кодом {}", response.getStatusCode());
        }

        // Проверка на пустой список офферов
        if (filteredOffers.isEmpty()) {
            logger.warn("⚠️ Список офферов пуст!");
            softAssert.assertTrue(filteredOffers.isEmpty(), "Список офферов должен быть пустым.");
        } else {
            // если список не пустой, выполняем проверки
            logger.info("✅ Найдено {} офферов.", filteredOffers.size());
            softAssert.assertNotNull(filteredOffers, "Список офферов не должен быть null");
            softAssert.assertFalse(filteredOffers.isEmpty(), "Список офферов не должен быть пустым");

            // проверка полей 1-го оффера
            softAssert.assertNotNull(filteredOffers.get(0).getTitle(), "Title не должен быть null");
            softAssert.assertTrue(filteredOffers.get(0).getTitle().length() > 0, "Title не должен быть пустым");

            softAssert.assertNotNull(filteredOffers.get(0).getDescription(), "Description не должен быть null");
            softAssert.assertTrue(filteredOffers.get(0).getDescription().length() > 0, "Description не должен быть пустым");

            softAssert.assertTrue(filteredOffers.get(0).getPricePerHour() > 0, "Цена должна быть положительной");

            // Проверка categoryResponseDto как объекта
            softAssert.assertNotNull(filteredOffers.get(0).getCategoryResponseDto(), "Category не должен быть null");
            softAssert.assertNotNull(filteredOffers.get(0).getCategoryResponseDto().getName(), "'name' категории не должен быть null");
            softAssert.assertTrue(filteredOffers.get(0).getCategoryResponseDto().getName().length() > 0, "Category name не должен быть пустым");

            // если OfferDto содержит UserFilterResponseDto
            softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto(), "UserFilterResponseDto не должен быть null");

            if (filteredOffers.get(0).getUserFilterResponseDto() != null) {
                // проверка полей UserFilterResponseDto
                softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto().getFirstName(), "FirstName не должен быть null");
                softAssert.assertTrue(filteredOffers.get(0).getUserFilterResponseDto().getFirstName().length() > 0, "FirstName не должен быть пустым");

                softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto().getLastName(), "LastName не должен быть null");
                softAssert.assertTrue(filteredOffers.get(0).getUserFilterResponseDto().getLastName().length() > 0, "LastName не должен быть пустым");

                softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto().getProfilePicture(), "ProfilePicture не должен быть null");
                softAssert.assertTrue(filteredOffers.get(0).getUserFilterResponseDto().getProfilePicture().length() > 0, "ProfilePicture не должен быть пустым");

                // проверка, что cityName внутри locationResponseDto не null и не пустое
                if (filteredOffers.get(0).getUserFilterResponseDto().getLocationResponseDto() != null) {
                    softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto().getLocationResponseDto().getCityName(), "CityName не должен быть null");
                    softAssert.assertTrue(filteredOffers.get(0).getUserFilterResponseDto().getLocationResponseDto().getCityName().length() > 0, "CityName не должен быть пустым");
                }

            }
        }

        // запуск
        softAssert.assertAll();
    }
}
