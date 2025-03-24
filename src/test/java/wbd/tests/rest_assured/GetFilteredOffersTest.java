package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.dto.FilteredOffersResponseDto;
import wbd.utils.ApiClient_GetFilterOffers;

import java.util.List;

public class GetFilteredOffersTest {

    @Test
    public void testGetFilteredOffers() {
        Response response = ApiClient_GetFilterOffers.getFilteredOffers(); // метод из ApiClient

        // логируем ответ
        System.out.println("Response body: " + response.asString());

        // парсим JSON в список
        List<FilteredOffersResponseDto> filteredOffers = response.jsonPath().getList("", FilteredOffersResponseDto.class);

        SoftAssert softAssert = new SoftAssert();

        // проверяем статус 200
        response.then().statusCode(200);

        // Проверка на пустой список офферов
        if (filteredOffers.isEmpty()) {
            System.out.println("⚠️ Список офферов пуст!");
            softAssert.assertTrue(filteredOffers.isEmpty(), "Список офферов должен быть пустым.");
        } else {
            // если список не пустой, выполняем проверки
            System.out.println("✅ Найдено " + filteredOffers.size() + " офферов.");
            softAssert.assertNotNull(filteredOffers, "Список офферов не должен быть null");
            softAssert.assertFalse(filteredOffers.isEmpty(), "Список офферов не должен быть пустым");

            // проверка полей 1-го оффера
            softAssert.assertNotNull(filteredOffers.get(0).getTitle(), "Title не должен быть null");
            softAssert.assertTrue(filteredOffers.get(0).getTitle().length() > 0, "Title не должен быть пустым");

            softAssert.assertNotNull(filteredOffers.get(0).getDescription(), "Description не должен быть null");
            softAssert.assertTrue(filteredOffers.get(0).getDescription().length() > 0, "Description не должен быть пустым");

            softAssert.assertTrue(filteredOffers.get(0).getPricePerHour() > 0, "Цена должна быть положительной");

            softAssert.assertNotNull(filteredOffers.get(0).getCategoryResponseDto(), "Category не должен быть null");
            softAssert.assertTrue(filteredOffers.get(0).getCategoryResponseDto().length() > 0, "Category не должен быть пустым");

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

                softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto().getLocationResponseDto(), "LocationResponseDto не должен быть null");
                softAssert.assertTrue(filteredOffers.get(0).getUserFilterResponseDto().getLocationResponseDto().length() > 0, "LocationResponseDto не должен быть пустым");
            }
        }

        // запуск
        softAssert.assertAll();
    }
}
