package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.api.client.get.ApiClient_GetOffers;
import wbd.core.TestBaseRA;
import wbd.api.dto.OfferResponseDto;
import wbd.api.dto.OfferDto;

import java.util.List;

public class GetOffersTests extends TestBaseRA {

    @Test
    public void testGetOffers() {
        logger.info("Starting GetOffers test");
        logger.info("=============================================");

        // отправляем GET-запрос на получение всех офферов
        Response response = ApiClient_GetOffers.getOffers();
        logger.info("Response body: " + response.asString());
        SoftAssert softAssert = new SoftAssert();

        // проверяем статус-код 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        if (response.getStatusCode() == 200) {
            logger.info("✅ Test successful: Response code 200 (OK)");
        } else {
            logger.error("❌ Error: Received response with code " + response.getStatusCode());
        }

        // парсим JSON в объект OfferResponseDto
        OfferResponseDto offerResponse = response.as(OfferResponseDto.class);

        // проверяем общие параметры ответа
        softAssert.assertNotNull(offerResponse, "Response must not be null");
        softAssert.assertNotNull(offerResponse.getTotalPages(), "TotalPages must not be null");
        softAssert.assertNotNull(offerResponse.getTotalElements(), "TotalElements must not be null");
        softAssert.assertNotNull(offerResponse.isLast(), "Last must not be null");
        softAssert.assertNotNull(offerResponse.isFirst(), "First must not be null");
        softAssert.assertNotNull(offerResponse.getNumberOfElements(), "NumberOfElements must not be null");
        softAssert.assertNotNull(offerResponse.getSize(), "Size must not be null");
        softAssert.assertNotNull(offerResponse.getNumber(), "Number must not be null");
        softAssert.assertNotNull(offerResponse.isEmpty(), "Empty must not be null");

        // проверяем список офферов
        List<OfferDto> offers = offerResponse.getContent();
        softAssert.assertNotNull(offers, "Offers list must not be null");
        softAssert.assertFalse(offers.isEmpty(), "Offers list must not be empty");

        // если есть хотя бы один оффер — проверяем его данные
        if (!offers.isEmpty()) {
            OfferDto offer = offers.get(0);

            softAssert.assertNotNull(offer.getId(), "ID must not be null");
            softAssert.assertNotNull(offer.getTitle(), "Title must not be null");
            softAssert.assertFalse(offer.getTitle().isEmpty(), "Title must not be empty");
            softAssert.assertNotNull(offer.getDescription(), "Description must not be null");
            softAssert.assertFalse(offer.getDescription().isEmpty(), "Description must not be empty");
            softAssert.assertTrue(offer.getPricePerHour() > 0, "Price per hour must be positive");

            // проверяем категорию
            softAssert.assertNotNull(offer.getCategoryDto(), "Category must not be null");
            softAssert.assertNotNull(offer.getCategoryDto().getName(), "Category Name must not be null");

            // проверяем пользователя
            softAssert.assertNotNull(offer.getUserFilterResponseDto(), "User must not be null");
            if (offer.getUserFilterResponseDto() != null) {
                softAssert.assertNotNull(offer.getUserFilterResponseDto().getFirstName(), "FirstName must not be null");
                softAssert.assertFalse(offer.getUserFilterResponseDto().getFirstName().isEmpty(), "FirstName must not be empty");
                softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationDto(), "LocationResponseDto must not be null");
                if (offer.getUserFilterResponseDto().getLocationDto() != null) {
                    softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationDto().getCityName(), "CityName must not be null");
                    softAssert.assertFalse(offer.getUserFilterResponseDto().getLocationDto().getCityName().isEmpty(), "CityName must not be empty");
                }
            }

            logger.info("---------------------------------------------------------");
            for (OfferDto offerDto : offers) {
                logger.info("Offer => " + offerDto.getTitle() + ";");
            }
        }

        // выполняем все SoftAssert проверки
        softAssert.assertAll();
    }
}