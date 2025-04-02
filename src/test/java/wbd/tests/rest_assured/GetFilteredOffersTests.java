package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.get.ApiClient_GetFilterOffers;
import wbd.core.TestBaseRA;
import wbd.api.dto.FilteredOffersResponseDto;


import java.util.List;

public class GetFilteredOffersTests extends TestBaseRA {

    @Test
    public void testGetFilteredOffers() {

        logger.info("Start testing GetFilteredOffers");
        logger.info("=============================================");

        Response response = ApiClient_GetFilterOffers.getFilteredOffers(); // метод из ApiClient


        logger.info("Response body: " + response.asString());

        // парсим JSON в список
        List<FilteredOffersResponseDto> filteredOffers = response.jsonPath().getList("contain", FilteredOffersResponseDto.class);

        SoftAssert softAssert = new SoftAssert();

        // проверяем статус 200
        response.then().statusCode(200);

        if (response.getStatusCode() == 200) {
            logger.info("The test was successful: Response code 200 (OK)");
        } else {
            logger.error("Error: Received response with code " + response.getStatusCode());
        }

        // Проверка на пустой список офферов
        if (filteredOffers.isEmpty()) {
            logger.info("⚠️ The list of offerers is empty!");
            softAssert.assertTrue(filteredOffers.isEmpty(), "The offender list should be empty.");
        } else {
            // если список не пустой, выполняем проверки
            logger.info("✅ Found " + filteredOffers.size() + "offeers.");
            softAssert.assertNotNull(filteredOffers, "The list of offenders must not be null");
            softAssert.assertFalse(filteredOffers.isEmpty(), "The list of offenders should not be empty");

            // проверка полей 1-го оффера
            softAssert.assertNotNull(filteredOffers.get(0).getTitle(), "Title must not be null");
            softAssert.assertTrue(!filteredOffers.get(0).getTitle().isEmpty(), "Title should not be empty");

            softAssert.assertNotNull(filteredOffers.get(0).getDescription(), "Description must not be null");
            softAssert.assertTrue(!filteredOffers.get(0).getDescription().isEmpty(), "Description should not be empty");

            softAssert.assertTrue(filteredOffers.get(0).getPricePerHour() > 0, "The price has to be positive");

            // Проверка categoryResponseDto как объекта
            softAssert.assertNotNull(filteredOffers.get(0).getCategoryDto(), "Category must not be null");
            softAssert.assertNotNull(filteredOffers.get(0).getCategoryDto().getName(), "The 'name' of the category must not be null");
            softAssert.assertTrue(!filteredOffers.get(0).getCategoryDto().getName().isEmpty(), "Category name must not be empty");

            // если OfferDto содержит UserFilterResponseDto
            softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto(), "UserFilterResponseDto must not be null");

            if (filteredOffers.get(0).getUserFilterResponseDto() != null) {
                // проверка полей UserFilterResponseDto
                softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto().getFirstName(), "FirstName must not be null");
                softAssert.assertTrue(!filteredOffers.get(0).getUserFilterResponseDto().getFirstName().isEmpty(), "FirstName must not be empty");

                softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto().getLastName(), "LastName must not be null");
                softAssert.assertTrue(!filteredOffers.get(0).getUserFilterResponseDto().getLastName().isEmpty(), "LastName must not be empty");

                softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto().getProfilePicture(), "ProfilePicture must not be null");
                softAssert.assertTrue(!filteredOffers.get(0).getUserFilterResponseDto().getProfilePicture().isEmpty(), "ProfilePicture must not be empty");

                // проверка, что cityName внутри locationResponseDto не null и не пустое
                if (filteredOffers.get(0).getUserFilterResponseDto().getLocationDto() != null) {
                    softAssert.assertNotNull(filteredOffers.get(0).getUserFilterResponseDto().getLocationDto().getCityName(), "CityName must not be null");
                    softAssert.assertTrue(!filteredOffers.get(0).getUserFilterResponseDto().getLocationDto().getCityName().isEmpty(), "CityName must not be empty");
                }

            }
        }

        softAssert.assertAll();
    }
}