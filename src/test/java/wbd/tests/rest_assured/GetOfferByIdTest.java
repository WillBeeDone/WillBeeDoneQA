package wbd.tests.rest_assured;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.dto.FilteredOffersResponseDto;
import wbd.utils.ApiClient_GetOfferById;

public class GetOfferByIdTest {

    @Test
    public void testGetOfferById() {
        int offerId = 1;  // допустим, оффер с ID 1 существует
        Response response = ApiClient_GetOfferById.getOfferById(offerId); // получаем оффер по ID

        System.out.println("Response body: " + response.asString());

        SoftAssert softAssert = new SoftAssert();

        // проверка статуса 200 OK
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        // парсим JSON в объект FilteredOffersResponseDto
        FilteredOffersResponseDto offer = response.as(FilteredOffersResponseDto.class);

        softAssert.assertNotNull(offer, "Offer не должен быть null");

        // все необходимые поля присутствуют
        softAssert.assertNotNull(offer.getTitle(), "Title не должен быть null");
        softAssert.assertTrue(offer.getTitle().length() > 0, "Title не должен быть пустым");

        softAssert.assertNotNull(offer.getDescription(), "Description не должен быть null");
        softAssert.assertTrue(offer.getDescription().length() > 0, "Description не должен быть пустым");

        softAssert.assertTrue(offer.getPricePerHour() > 0, "Цена должна быть положительной");

        softAssert.assertNotNull(offer.getCategoryResponseDto(), "Category не должен быть null");
        softAssert.assertTrue(offer.getCategoryResponseDto().length() > 0, "Category не должен быть пустым");

        // проверка UserFilterResponseDto (если оно есть)
        if (offer.getUserFilterResponseDto() != null) {
            softAssert.assertNotNull(offer.getUserFilterResponseDto().getFirstName(), "FirstName не должен быть null");
            softAssert.assertTrue(offer.getUserFilterResponseDto().getFirstName().length() > 0, "FirstName не должен быть пустым");

            softAssert.assertNotNull(offer.getUserFilterResponseDto().getLastName(), "LastName не должен быть null");
            softAssert.assertTrue(offer.getUserFilterResponseDto().getLastName().length() > 0, "LastName не должен быть пустым");

            softAssert.assertNotNull(offer.getUserFilterResponseDto().getProfilePicture(), "ProfilePicture не должен быть null");
            softAssert.assertTrue(offer.getUserFilterResponseDto().getProfilePicture().length() > 0, "ProfilePicture не должен быть пустым");

            softAssert.assertNotNull(offer.getUserFilterResponseDto().getLocationResponseDto(), "LocationResponseDto не должен быть null");
            softAssert.assertTrue(offer.getUserFilterResponseDto().getLocationResponseDto().length() > 0, "LocationResponseDto не должен быть пустым");
        }

        // проверка на наличие хотя бы одного изображения
        if (offer.getImages() != null && !offer.getImages().isEmpty()) {
            softAssert.assertNotNull(offer.getImages().get(0).getImageUrl(), "ImageUrl не должен быть null");
            softAssert.assertTrue(offer.getImages().get(0).getImageUrl().length() > 0, "ImageUrl не должен быть пустым");
        }

        // запуск
        softAssert.assertAll();
    }

    @Test
    public void testGetOfferByNonExistentId() {
        int nonExistentOfferId = 9999;  // допустим, что такого оффера нет
        Response response = ApiClient_GetOfferById.getOfferById(nonExistentOfferId); // оффер по несуществующему ID

        SoftAssert softAssert = new SoftAssert();

        // проверка, что сервер вернул 404 (Not Found)
        softAssert.assertEquals(response.getStatusCode(), 404, "Expected status code 404 for non-existent offer");

        softAssert.assertAll();
    }
}